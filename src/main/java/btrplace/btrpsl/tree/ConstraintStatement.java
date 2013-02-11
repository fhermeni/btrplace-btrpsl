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

package btrplace.btrpsl.tree;

import btrplace.btrpsl.BtrpScript;
import btrplace.btrpsl.ErrorReporter;
import btrplace.btrpsl.constraint.ConstraintsCatalog;
import btrplace.btrpsl.constraint.PlacementConstraintBuilder;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.IgnorableOperand;
import btrplace.model.SatConstraint;
import org.antlr.runtime.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree to build a constraint. Root
 * of the tree is the constraint identifier while childs are the parameters.
 *
 * @author Fabien Hermenier
 */
public class ConstraintStatement extends BtrPlaceTree {

    private BtrpScript vjob;

    private ConstraintsCatalog catalog;

    /**
     * Make a new Tree parser.
     *
     * @param t    the root symbol
     * @param cat  the catalog of available constraints
     * @param errs the errors to report
     */
    public ConstraintStatement(Token t, BtrpScript vjob, ConstraintsCatalog cat, ErrorReporter errs) {
        super(t, errs);
        this.catalog = cat;
        this.vjob = vjob;
    }

    /**
     * Build the constraint.
     * The constraint is builded if it exists in the catalog and if the parameters
     * are compatible with the constraint signature.
     *
     * @param parent the parent of the root
     * @return {@code Content.empty} if the constraint is successfully builded.
     *         {@code Content.ignore} if an error occured (the error is already reported)
     */
    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        String cname = getText();

        if (catalog == null) {
            return ignoreError("No constraints available");
        }
        PlacementConstraintBuilder b = catalog.getConstraint(cname);
        if (b == null) {
            ignoreError("Unknown constraint '" + cname + "'");
        }

        //Get the params
        List<BtrpOperand> params = new ArrayList<BtrpOperand>();
        for (int i = 0; i < getChildCount(); i++) {
            params.add(getChild(i).go(this));
        }
        if (b != null) {
            SatConstraint c = b.buildConstraint(this, params);
            if (c != null) {
                vjob.addConstraint(c);
            }
        }
        return IgnorableOperand.getInstance();
    }
}
