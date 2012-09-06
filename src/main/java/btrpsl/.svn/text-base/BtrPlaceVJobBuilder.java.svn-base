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

import btrpsl.constraint.ConstraintsCatalog;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.includes.Includes;
import btrpsl.tree.BtrPlaceTree;
import btrpsl.tree.BtrPlaceTreeAdaptor;
import entropy.vjob.builder.VJobBuilder;
import entropy.vjob.builder.VJobElementBuilder;
import gnu.trove.TIntLongHashMap;
import org.antlr.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
public class BtrPlaceVJobBuilder implements VJobBuilder {

    /**
     * The date of last modification for the file. The key is the hashcode of the file path.
     */
    private TIntLongHashMap dates;

    public static final int DEFAULT_CACHE_SIZE = 100;

    private LinkedHashMap<String, BtrPlaceVJob> cache;

    /**
     * The builder to instantiate new elements.
     */
    private VJobElementBuilder elemBuilder;

    public static final Logger LOGGER = LoggerFactory.getLogger("BtrPlaceVJobBuilder");

    private ConstraintsCatalog catalog;

    private Includes includes;


    /**
     * Make a new builder.
     * The vjob cache has a size of {@value #DEFAULT_CACHE_SIZE}
     *
     * @param eBuilder the builder to instantiate new elements
     * @param c        the catalog of available constraints
     */
    public BtrPlaceVJobBuilder(VJobElementBuilder eBuilder, ConstraintsCatalog c) {
        this(eBuilder, c, DEFAULT_CACHE_SIZE);
    }

    public void setElementBuilder(VJobElementBuilder eb) {
        this.elemBuilder = eb;
    }

    /**
     * Make a new builder.
     *
     * @param eBuilder  the builder to instantiate new elements
     * @param c         the catalog of available constraints
     * @param cacheSize the size of the cache
     */
    public BtrPlaceVJobBuilder(VJobElementBuilder eBuilder, ConstraintsCatalog c, final int cacheSize) {
        elemBuilder = eBuilder;
        catalog = c;
        this.dates = new TIntLongHashMap();
        this.cache = new LinkedHashMap<String, BtrPlaceVJob>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, BtrPlaceVJob> stringBtrPlaceVJobEntry) {
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

    /**
     * Get the builder to make managed elements.
     *
     * @return the builder
     */
    public VJobElementBuilder getElementBuilder() {
        return elemBuilder;
    }

    @Override
    public BtrPlaceVJob build(File f) throws BtrpPlaceVJobBuilderException {
        int k = f.getAbsolutePath().hashCode();
        if (dates.containsKey(k) && dates.get(k) == f.lastModified() && cache.containsKey(f.getPath())) {
            LOGGER.debug("get '" + f.getName() + "' from the cache");
            return cache.get(f.getPath());
        } else {
            LOGGER.debug(f.getName() + " is built from the file");
            dates.put(k, f.lastModified());
            try {
                String name = f.getName();
                if (!name.endsWith(".btrp")) {
                    throw new BtrpPlaceVJobBuilderException("Btrplace scripts must end with '.btrp'. Got '" + name + "'");
                }
                String woExt = f.getName().substring(0, f.getName().length() - 5);
                BtrPlaceVJob v = build(new ANTLRFileStream(f.getAbsolutePath()));
                String last;
                if (woExt.contains(".")) {
                    last = woExt.substring(woExt.lastIndexOf('.'), woExt.length());
                } else {
                    last = woExt;
                }
                if (!v.getlocalName().equals(last)) {
                    throw new BtrpPlaceVJobBuilderException("Error, the vjob local name (" + v.getlocalName() + ") does not match the file name (" + last + ")");
                }
                cache.put(f.getPath(), v);
                return v;
            } catch (IOException e) {
                throw new BtrpPlaceVJobBuilderException(e.getMessage(), e);
            }
        }
    }

    /**
     * Build a VJob from a String.
     *
     * @param description the description of the vjob
     * @return the builded vjob
     * @throws BtrpPlaceVJobBuilderException if an error occurred while buildeing the vjob
     */
    public BtrPlaceVJob build(String description) throws BtrpPlaceVJobBuilderException {
        return build(new ANTLRStringStream(description));
    }

    /**
     * Internal method to build a vjob from a stream.
     *
     * @param cs the stream to analyze
     * @return the  builded vjob
     * @throws BtrpPlaceVJobBuilderException in an error occurred while building the vjob
     */
    private BtrPlaceVJob build(CharStream cs) throws BtrpPlaceVJobBuilderException {
        ANTLRBtrplaceSL2Lexer lexer = new ANTLRBtrplaceSL2Lexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ANTLRBtrplaceSL2Parser parser = new ANTLRBtrplaceSL2Parser(tokens);


        SymbolsTable t = new SymbolsTable();
        //Declare the ME variable
        BtrpSet me = new BtrpSet(1, BtrpOperand.Type.vm);
        me.setLabel(SymbolsTable.ME);
        t.declareImmutable(me.label(), me);
        BtrPlaceVJob v = new BtrPlaceVJob();
        final SemanticErrors errs = new SemanticErrors(v);
        parser.setTreeAdaptor(new BtrPlaceTreeAdaptor(v, errs, t, elemBuilder, includes, catalog));


        try {
            BtrPlaceTree tree = (BtrPlaceTree) parser.vjob_decl().getTree();
            //First pass, expand range
            if (tree != null && tree.token != null) {
                try {
                    tree.go(tree); //Single instruction
                } catch (UnsupportedOperationException e) {
                    errs.append(e.getMessage());
                }
            } else {
                for (int i = 0; i < tree.getChildCount(); i++) {
                    try {
                        tree.getChild(i).go(tree);
                    } catch (UnsupportedOperationException e) {
                        errs.append(e.getMessage());
                    }
                }
            }
        } catch (RecognitionException e) {
            throw new BtrpPlaceVJobBuilderException(e.getMessage(), e);
        }
        if (errs.size() > 0) {
            throw new BtrpPlaceVJobBuilderException(errs.toString());
        }
        return v;
    }

    @Override
    public String getAssociatedExtension() {
        return "btrp";
    }
}
