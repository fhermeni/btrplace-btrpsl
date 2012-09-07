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

    private static ConstraintParameter[] params = new ConstraintParameter[]{
            new ConstraintParameter(BtrpOperand.Type.node, 1, "$n"),
            new ConstraintParameter(BtrpOperand.Type.number, 0, "$nb")
    };

    @Override
    public ConstraintParameter[] getParameters() {
        return params;
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
        ManagedElementSet<Node> ns = PlacementConstraintBuilders.makeNodes(t, args.get(0));
        boolean ret = minCardinality(t, args.get(0), ns, 1);
        Integer v = PlacementConstraintBuilders.makeInt(args.get(1));
        if (v != null && v < 0) {
            t.ignoreError(getSignature() + " expects a positive integer");
            v = null;
        }
        return (ret && ns != null && v != null ? new Capacity(ns, v) : null);
    }
}
