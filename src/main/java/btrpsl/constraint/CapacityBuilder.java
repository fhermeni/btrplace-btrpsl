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

package btrpsl.constraint;

import btrpsl.element.BtrpOperand;
import btrpsl.tree.BtrPlaceTree;
import entropy.configuration.ManagedElementSet;
import entropy.configuration.Node;
import entropy.vjob.Capacity;

import java.util.List;

/**
 * A builder to make Capacity constraints.
 *
 * @author Fabien Hermenier
 */
public class CapacityBuilder extends DefaultPlacementConstraintBuilder {

    public CapacityBuilder() {
        super(new ConstraintParam[] {new SetOfNodesParam("$n", false), new IntParam("$nb")});
    }

    @Override
    public String getIdentifier() {
        return "capacity";
    }

    @Override
    public Capacity buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (!checkConformance(t, args)) {
            return null;
        }
        @SuppressWarnings("unchecked") ManagedElementSet<Node> ns = (ManagedElementSet<Node>) params[0].transform(t, args.get(0));
        @SuppressWarnings("unchecked") Integer v = (Integer)params[1].transform(t, args.get(1));
        if (v < 0) {
            t.ignoreError("Parameter '" + params[1].getName() + "' expects a positive integer");
            v = null;
        }
        return (ns != null && v != null ? new Capacity(ns, v) : null);
    }
}
