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

package btrpsl.tree;

import btrpsl.BtrPlaceVJob;
import btrpsl.SemanticErrors;
import btrpsl.constraint.ConstraintBuilderException;
import btrpsl.constraint.ConstraintsCatalog;
import btrpsl.constraint.PlacementConstraintBuilder;
import btrpsl.element.BtrpOperand;
import btrpsl.element.IgnorableOperand;
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

    private BtrPlaceVJob vjob;

    private ConstraintsCatalog catalog;

    /**
     * Make a new Tree parser.
     *
     * @param t    the root symbol
     * @param cat  the catalog of available constraints
     * @param errs the errors to report
     */
    public ConstraintStatement(Token t, BtrPlaceVJob vjob, ConstraintsCatalog cat, SemanticErrors errs) {
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
            ignoreError("Unknown constraint '" + cname + "'. Availables are " + catalog.getAvailableConstraints());
        }

        //Get the params
        List<BtrpOperand> params = new ArrayList<BtrpOperand>();
        for (int i = 0; i < getChildCount(); i++) {
            BtrpOperand c = getChild(i).go(this);
            if (c == IgnorableOperand.getInstance()) {
                return c;
            } else {
                params.add(c);
            }
        }
        if (b != null) {
            try {
                vjob.addConstraint(b.buildConstraint(params));
            } catch (ConstraintBuilderException e) {
                return ignoreError(e.getMessage());
            }
        }
        //fTable.add(cname, params);
        return IgnorableOperand.getInstance();
    }
}
