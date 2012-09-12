/*
 * Copyright (c) Fabien Hermenier
 *
 *        This file is part of Entropy.
 *
 *        Entropy is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU Lesser General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *
 *        Entropy is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU Lesser General Public License for more details.
 *
 *        You should have received a copy of the GNU Lesser General Public License
 *        along with Entropy.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrpsl;

import btrpsl.constraint.ConstraintsCalalogBuilderException;
import btrpsl.constraint.ConstraintsCatalog;
import btrpsl.constraint.ConstraintsCatalogBuilderFromProperties;
import btrpsl.includes.PathBasedIncludes;
import btrpsl.platform.PlatformFactoryStub;
import btrpsl.template.VirtualMachineTemplateFactoryStub;
import ch.qos.logback.classic.Logger;
import entropy.PropertiesHelper;
import entropy.platform.PlatformFactory;
import entropy.template.VirtualMachineTemplateFactory;
import entropy.template.VirtualMachineTemplateFactoryBuilderException;
import entropy.template.VirtualMachineTemplateFactoryBuilderFromProperties;
import entropy.vjob.PlacementConstraint;
import entropy.vjob.builder.DefaultVJobFileSerializerFactory;
import entropy.vjob.builder.VJobElementBuilder;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Shell command to interpret a script and deploy VMs.
 *
 * @author Fabien Hermenier
 */
public final class BtrpLint {

    public static final String CMD_VALID = "valid";

    public static final String CMD_DEPS = "deps";

    /**
     * The default constraint catalog location.
     */
    public static final String CONFIG = "./config/catalog.properties";

    public static final String DEFAULT_LIB = "./lib";

    /**
     * The environment variable to check for includes if the flag {@link #TPL_FLAG} is not specified.
     */
    public static final String INC_ENV = "BTRP_INC";

    /**
     * The home directory of Btrplace.
     */
    public static final String HOME_ENV = "BTRP_HOME";

    /**
     * The flag to ask for help
     */
    public static final String USAGE_FLAG = "-h";

    /**
     * The flag to specify available templates.
     */
    public static final String TPL_FLAG = "-tpl";

    /**
     * The flag to specify the includes
     */
    public static final String INC_FLAG = "-I";

    /**
     * The flag to specify the constraint catalog.
     */
    public static final String CAT_FLAG = "-cat";

    /**
     * The flag to specify the output file.
     */
    public static final String OUTPUT_FLAG = "-o";

    /**
     * The flag to activate the verbose mode.
     */
    public static final String VERBOSE_FLAG = "-v";

    /**
     * The current version of the shell.
     */
    public static final String VERSION = "Btrplint version 0.105";

    /**
     * Print an error message then exit with the exit code {@code 1}.
     *
     * @param msg the error message to print
     */
    public static void fatal(String msg) {
        System.err.println(msg);
        System.exit(1);
    }

    public static void main(String[] args) {

        if (args.length > 0 && args[0].equals(USAGE_FLAG)) {
            usage();
            System.exit(0);
        }
        if (args.length == 0 || args.length < 2) {
            usage();
            System.exit(1);
        }

        String home = ".";

        String includes = null;
        String templates = null;
        String script = args[args.length - 1];
        String catalog = null;
        String output = null;
        String action = args[0];
        int verbose = 0;


        String h = System.getenv(INC_ENV);
        if (h != null && !new File(h).isDirectory()) {
            fatal("Environment variable " + HOME_ENV + " does not pointing to an existing directory");
        } else if (h != null) {
            home = ".";
        }


        for (int i = 1; i < args.length - 1; i++) {
            if (args[i].equals(INC_FLAG)) {
                includes = args[++i];
            } else if (args[i].equals(TPL_FLAG)) {
                templates = args[++i];
            } else if (args[i].equals(CAT_FLAG)) {
                catalog = args[++i];
            } else if (args[i].equals(OUTPUT_FLAG)) {
                output = args[++i];
            } else if (args[i].equals(VERBOSE_FLAG)) {
                verbose++;
            } else {
                fatal("Unknown option '" + args[i] + "'");
            }
        }

        if (verbose < 2) {
            Logger logger = (Logger) LoggerFactory.getLogger("BtrPlaceVJobBuilder");
            logger.detachAppender("console-debug");
        }

        //VM Templates
        VirtualMachineTemplateFactory tplF = null;

        if (templates == null) {
            tplF = new VirtualMachineTemplateFactoryStub();
        } else {
            if (!new File(templates).exists()) {
                fatal("Unable to read template file '" + templates + "'");
            }
            try {
                tplF = new VirtualMachineTemplateFactoryBuilderFromProperties(templates).build();
            } catch (VirtualMachineTemplateFactoryBuilderException ex) {
                fatal("Error while reading '" + templates + ": " + ex.getMessage());
            }
        }

        //Catalog stuff
        if (catalog == null) {
            catalog = home + File.separator + CONFIG;
        }

        ConstraintsCatalog cat = null;

        try {
            cat = new ConstraintsCatalogBuilderFromProperties(new PropertiesHelper(catalog)).build();
        } catch (ConstraintsCalalogBuilderException e) {
            fatal("Unable to read the constraint catalog: " + e.getMessage());
        } catch (IOException e) {
            fatal("Unable to read the constraint catalog: " + e.getMessage());
        }

        //Builder stuff
        PlatformFactory plts = new PlatformFactoryStub();
        VJobElementBuilder eb = new MockVJobElementBuilder(tplF, plts);

        BtrPlaceVJobBuilder builder = new BtrPlaceVJobBuilder(eb, cat);

        //Include stuff
        if (includes == null) {
            includes = System.getenv(INC_ENV);
            if (includes == null) {
                includes = ".:" + DEFAULT_LIB;
                if (System.getenv(HOME_ENV) != null) {
                    includes = includes + ":" + home + File.separator + "lib";
                }
            }
        }

        PathBasedIncludes inc = PathBasedIncludes.fromPaths(builder, includes);
        if (inc == null) {
            fatal("Unable to set '" + includes + "' as includes");
        }
        builder.setIncludes(inc);
        try {
            BtrPlaceVJob vjob = builder.build(new File(script));
            if (action.equals(CMD_DEPS)) {
                vjob.prettyDependencies();
            } else if (action.equals(CMD_VALID)) {
                if (verbose > 0) {
                    for (PlacementConstraint c : vjob.getConstraints()) {
                        System.out.println(c);
                    }
                }
                if (output != null) {
                    try {
                        DefaultVJobFileSerializerFactory.getInstance().write(vjob, output);
                    } catch (IOException e) {
                        fatal(e.getMessage());
                    }
                }
            } else {
                System.err.println("Unsupported action '" + action + "'");
            }

        } catch (BtrpPlaceVJobBuilderException ex) {
            fatal(ex.getMessage());
        }
    }

    /**
     * Print the usage of the command.
     */
    public static void usage() {
        StringBuilder b = new StringBuilder();
        b.append(BtrpLint.VERSION).append("\n\n");
        b.append("btrplint " + USAGE_FLAG).append("\n");
        b.append("\tprint this screen").append("\n");
        b.append("btrplint " + CMD_VALID + " [" + VERBOSE_FLAG + "] [" + INC_FLAG + " includes] [" + CAT_FLAG + " catalog] [" + TPL_FLAG + " templates] [" + OUTPUT_FLAG + " output ] script").append("\n");
        b.append("\tcheck the script validity. A optional conversion to XML or protobuf format can be performed").append("\n");
        b.append("btrplint " + CMD_DEPS + " [" + VERBOSE_FLAG + "] [" + INC_FLAG + " includes] [" + CAT_FLAG + " catalog] [" + TPL_FLAG + " templates] script").append("\n");
        b.append("\tprint the script dependencies as a tree").append("\n");

        b.append("\nOptions:").append("\n");
        b.append(VERBOSE_FLAG + " verbose output. One '-v' print the parsed vjob. More print additional debug informations").append("\n");
        b.append(INC_FLAG + " path to the includes. If omitted, look at $" + INC_ENV + ". Otherwise, look at '.', './lib', and $" + DEFAULT_LIB + " if exists").append("\n");
        b.append(CAT_FLAG + " config files specifying the available constraints. If omitted, look at " + CONFIG).append("\n");
        b.append(TPL_FLAG + " a Entropy properties file to describe the available templates. If omitted, the existence of required templates is not checked").append("\n");
        b.append(OUTPUT_FLAG + " the output file. Use '.xml' as a file extension for an XML output or '.pbd' as a file extension for a protobuf output").append("\n");
        System.out.println(b.toString());
    }
}
