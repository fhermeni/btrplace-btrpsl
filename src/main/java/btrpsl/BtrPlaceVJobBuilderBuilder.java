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
import entropy.PropertiesHelper;
import entropy.vjob.builder.VJobBuilderBuilder;
import entropy.vjob.builder.VJobBuilderBuilderException;
import entropy.vjob.builder.VJobElementBuilder;

import java.io.File;
import java.io.IOException;

/**
 * Builder to integrate BtrPlaceVJobBuilder to Entropy.
 *
 * @author Fabien Hermenier
 */
public class BtrPlaceVJobBuilderBuilder implements VJobBuilderBuilder {

    public static final String PROPERTIES = "config/btrpVjobs.properties";

    /**
     * The properties file.
     */
    private String file;

    /**
     * Build the vjob builder using the default properties file {@link #PROPERTIES}.
     */
    public BtrPlaceVJobBuilderBuilder() {
        this(PROPERTIES);
    }

    /**
     * Build the vjob builder using a specific properties file.
     *
     * @param file the properties file
     */
    public BtrPlaceVJobBuilderBuilder(String file) {
        this.file = file;
    }

    /**
     * Get the used properties file.
     *
     * @return the path to the properties file
     */
    public String getFile() {
        return this.file;
    }

    @Override
    public BtrPlaceVJobBuilder build(VJobElementBuilder eb) throws VJobBuilderBuilderException {
        try {
            PropertiesHelper propHelper = new PropertiesHelper(this.file);
            ConstraintsCatalog c = new ConstraintsCatalogBuilderFromProperties(propHelper).build();
            BtrPlaceVJobBuilder vb = new BtrPlaceVJobBuilder(eb, c);
            String paths = propHelper.getOptionalProperty("includes", System.getProperty("user.dir"));
            PathBasedIncludes includes = new PathBasedIncludes(vb, new File(paths));
            vb.setIncludes(includes);
            return vb;
        } catch (IOException e) {
            throw new VJobBuilderBuilderException(e.getMessage(), e);
        } catch (ConstraintsCalalogBuilderException e) {
            throw new VJobBuilderBuilderException("Unable to build the protobuf vjob builder:" + e.getMessage(), e);
        }
    }
}
