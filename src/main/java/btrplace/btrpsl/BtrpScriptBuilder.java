/*
 * Copyright (c) 2012 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrplace.btrpsl;

import btrplace.btrpsl.constraint.ConstraintsCatalog;
import btrplace.btrpsl.constraint.DefaultConstraintsCatalog;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.BtrpSet;
import btrplace.btrpsl.includes.Includes;
import btrplace.btrpsl.template.DefaultTemplateFactory;
import btrplace.btrpsl.template.TemplateFactory;
import btrplace.btrpsl.tree.BtrPlaceTree;
import btrplace.btrpsl.tree.BtrPlaceTreeAdaptor;
import org.antlr.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Build VJobs from textual descriptions.
 * For file based parsing, a LRU cache is used to prevent useless parsing. If the
 * last modification date of the vjob has not changed since its last parsing, the cached version
 * is returned.
 *
 * @author Fabien Hermenier
 */
public class BtrpScriptBuilder {

    /**
     * The date of last modification for the file. The key is the hashcode of the file path.
     */
    private Map<Integer, Long> dates;

    public static final int DEFAULT_CACHE_SIZE = 100;

    private LinkedHashMap<String, BtrpScript> cache;

    public static final Logger LOGGER = LoggerFactory.getLogger("BtrpScriptBuilder");

    private ConstraintsCatalog catalog;

    private Includes includes;

    private TemplateFactory tpls;

    private UUIDPool uuidPool;

    /**
     * The builder to use to make ErrorReporter.
     */
    private ErrorReporterBuilder errBuilder = new PlainTextErrorReporterBuilder();

    private NamingService namingService;

    public BtrpScriptBuilder() {
        this(DEFAULT_CACHE_SIZE);
    }

    /**
     * Make a new builder.
     *
     * @param cacheSize the size of the cache
     */
    public BtrpScriptBuilder(final int cacheSize) {
        this.namingService = new NamingService();
        catalog = new DefaultConstraintsCatalog();
        this.uuidPool = new InMemoryUUIDPool();
        this.tpls = new DefaultTemplateFactory(uuidPool, namingService, false);
        this.dates = new HashMap<Integer, Long>();
        this.cache = new LinkedHashMap<String, BtrpScript>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, BtrpScript> stringBtrPlaceVJobEntry) {
                return size() == cacheSize;
            }
        };
    }

    /**
     * Get the possibles vjobs that can be included
     *
     * @return an includes.
     */
    public Includes getIncludes() {
        return this.includes;
    }

    /**
     * Set the include library.
     *
     * @param incs the library to add
     */
    public void setIncludes(Includes incs) {
        this.includes = incs;
    }

    public BtrpScript build(File f) throws BtrpScriptBuilderException {
        int k = f.getAbsolutePath().hashCode();
        if (dates.containsKey(k) && dates.get(k) == f.lastModified() && cache.containsKey(f.getPath())) {
            LOGGER.debug("get '" + f.getName() + "' from the cache");
            return cache.get(f.getPath());
        } else {
            LOGGER.debug(f.getName() + " is built from the file");
            dates.put(k, f.lastModified());
            String name = f.getName();
            try {
                BtrpScript v = null;
                try {
                    v = build(new ANTLRFileStream(f.getAbsolutePath()));
                    if (v != null && !name.equals(v.getlocalName() + ".btrp")) {
                        throw new BtrpScriptBuilderException(errBuilder.build(v));
                    }

                } catch (BtrpScriptBuilderException e) {
                    if (v != null && !name.equals(v.getlocalName() + BtrpScript.EXTENSION)) {
                        e.getErrorReporter().append(0, 0, "the script '" + v.getlocalName() + "' must be declared in a file named '" + v.getlocalName() + BtrpScript.EXTENSION + " was '" + name + "'");
                    }
                    throw e;
                }
                cache.put(f.getPath(), v);
                return v;
            } catch (IOException e) {
                throw new BtrpScriptBuilderException(e.getMessage(), e);
            }
        }
    }

    /**
     * Build a VJob from a String.
     *
     * @param description the description of the vjob
     * @return the builded vjob
     * @throws BtrpScriptBuilderException if an error occurred while buildeing the vjob
     */
    public BtrpScript build(String description) throws BtrpScriptBuilderException {
        return build(new ANTLRStringStream(description));
    }

    /**
     * Internal method to build a vjob from a stream.
     *
     * @param cs the stream to analyze
     * @return the  builded vjob
     * @throws BtrpScriptBuilderException in an error occurred while building the vjob
     */
    private BtrpScript build(CharStream cs) throws BtrpScriptBuilderException {

        BtrpScript v = new BtrpScript();

        ANTLRBtrplaceSL2Lexer lexer = new ANTLRBtrplaceSL2Lexer(cs);

        ErrorReporter errorReporter = errBuilder.build(v);

        lexer.setErrorReporter(errorReporter);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ANTLRBtrplaceSL2Parser parser = new ANTLRBtrplaceSL2Parser(tokens);
        parser.setErrorReporter(errorReporter);

        SymbolsTable t = new SymbolsTable();
        //Declare the ME variable
        BtrpSet me = new BtrpSet(1, BtrpOperand.Type.VM);
        me.setLabel(SymbolsTable.ME);
        t.declareImmutable(me.label(), me);


        parser.setTreeAdaptor(new BtrPlaceTreeAdaptor(v, namingService, tpls, errorReporter, t, includes, catalog));


        try {
            BtrPlaceTree tree = (BtrPlaceTree) parser.vjob_decl().getTree();
            //First pass, expand range
            if (tree != null && tree.token != null) {
                try {
                    tree.go(tree); //Single instruction
                } catch (UnsupportedOperationException e) {
                    errorReporter.append(0, 0, e.getMessage());
                }
            } else {
                for (int i = 0; i < tree.getChildCount(); i++) {
                    try {
                        tree.getChild(i).go(tree);
                    } catch (UnsupportedOperationException e) {
                        e.printStackTrace();
                        errorReporter.append(0, 0, e.getMessage());
                    }
                }
            }
        } catch (RecognitionException e) {
            throw new BtrpScriptBuilderException(e.getMessage(), e);
        }
        if (!errorReporter.getErrors().isEmpty()) {
            throw new BtrpScriptBuilderException(errorReporter);
        }
        return v;
    }

    public String getAssociatedExtension() {
        return "btrp";
    }

    /**
     * Indicate the {@link ErrorReporter} to instantiate before parsing
     * a VJob.
     *
     * @param b the builder to use
     */
    public void setErrorReporterBuilder(ErrorReporterBuilder b) {
        this.errBuilder = b;
    }

    public void setUUIDPool(UUIDPool p) {
        this.uuidPool = p;
    }

    public UUIDPool getUUIDPool() {
        return this.uuidPool;
    }

    public NamingService getNamingService() {
        return this.namingService;
    }

    public void setNamingService(NamingService srv) {
        this.namingService = srv;
    }
}
