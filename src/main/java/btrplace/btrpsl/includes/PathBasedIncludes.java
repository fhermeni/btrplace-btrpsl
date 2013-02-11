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

package btrplace.btrpsl.includes;

import btrplace.btrpsl.BtrpScript;
import btrplace.btrpsl.BtrpScriptBuilder;
import btrplace.btrpsl.BtrpScriptBuilderException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An implementation that loop for the searched vjob among several folders.
 * Similar to a shell path.
 *
 * @author Fabien Hermenier
 */
public class PathBasedIncludes implements Includes {

    /**
     * The folders to browse.
     */
    private List<File> paths;

    /**
     * The builder to create the vjobs.
     */
    private BtrpScriptBuilder builder;

    /**
     * Make a new instance that will browse a first folder.
     *
     * @param vBuilder the builder to parse the vjobs
     * @param path     the first folder to look into
     */
    public PathBasedIncludes(BtrpScriptBuilder vBuilder, File path) {
        if (!path.isDirectory()) {
            throw new IllegalArgumentException(path + " must be an existing directory");
        }
        this.paths = new LinkedList<File>();
        this.paths.add(path);
        this.builder = vBuilder;
    }

    /**
     * Get the vjob associated to a given identifier by browsing the given paths.
     * The first vjob having a matching identifier is selected, whatever the parsing process result will be
     *
     * @param name the identifier of the vjob
     * @return the vjob if found
     * @throws btrplace.btrpsl.BtrpScriptBuilderException
     *          if the builder was not able to parse the looked vjob
     */
    @Override
    public List<BtrpScript> getVJob(String name) throws BtrpScriptBuilderException {

        List<BtrpScript> vjobs = new ArrayList<BtrpScript>();
        if (!name.endsWith(".*")) {
            String toSearch = new StringBuilder(name.replaceAll("\\.", File.separator)).append(BtrpScript.EXTENSION).toString();
            for (File path : paths) {
                File f = new File(path.getPath() + File.separator + toSearch);
                if (f.exists()) {
                    vjobs.add(builder.build(f));
                    break;
                }
            }

        } else {

            //We need to consolidate the errors in allEx and rethrow it at the end if necessary
            BtrpScriptBuilderException allEx = null;
            String base = new StringBuilder(name.substring(0, name.length() - 2).replaceAll("\\.", File.separator)).toString();
            for (File path : paths) {
                File f = new File(path.getPath() + File.separator + base);
                if (f.isDirectory()) {
                    for (File sf : f.listFiles()) {
                        if (sf.getName().endsWith(BtrpScript.EXTENSION)) {

                            try {
                                vjobs.add(builder.build(sf));
                            } catch (BtrpScriptBuilderException ex) {
                                if (allEx == null) {
                                    allEx = ex;
                                } else {
                                    allEx.getErrorReporter().getErrors().addAll(ex.getErrorReporter().getErrors());
                                }
                            }
                        }
                    }
                }
            }
            if (allEx != null) {
                throw allEx;
            }
        }
        return vjobs;
    }

    /**
     * Add a new folder to browse.
     *
     * @param path the path to the folder
     *             {@code path} must be an existing folder
     * @return {@code true} if the path was added
     */
    public boolean addPath(File path) {
        return (path.isDirectory() && this.paths.add(path));
    }

    /**
     * Get all the given paths.
     *
     * @return a list of paths that may be empty
     */
    public List<File> getPaths() {
        return this.paths;
    }

    /**
     * Build a PathBasedIncludes from a sequence of paths.
     * Paths are separated by a {@link File#pathSeparator}
     *
     * @param vBuilder the vjob builder to make the vjobs
     * @param paths    the paths to consider
     * @return the includes or {@code null} if at least one path is not an existing directory
     */
    public static PathBasedIncludes fromPaths(BtrpScriptBuilder vBuilder, String paths) {
        if (!paths.contains(File.pathSeparator)) {
            File f = new File(paths);
            if (!f.isDirectory()) {
                return null;
            }
            return new PathBasedIncludes(vBuilder, f);
        }
        PathBasedIncludes incs = null;
        String[] toks = paths.split(File.pathSeparator);
        for (int i = 0; i < toks.length; i++) {
            File f = new File(toks[i]);
            if (f.exists() && !f.isDirectory()) {
                return null;
            }
            if (i == 0) {
                incs = new PathBasedIncludes(vBuilder, f);
            } else {
                incs.addPath(f);
            }
        }
        return incs;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Iterator<File> ite = paths.iterator(); ite.hasNext(); ) {
            b.append(ite.next().getPath());
            if (ite.hasNext()) {
                b.append(File.pathSeparatorChar);
            }
        }
        return b.toString();
    }
}
