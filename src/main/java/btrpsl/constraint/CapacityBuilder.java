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
import entropy.configuration.ManagedElementSet;
import entropy.configuration.Node;
import entropy.vjob.Capacity;

import java.util.List;

/**
 * A builder to make Capacity constraints.
 *
 * @author Fabien Hermenier
 */
public class CapacityBuilder implements PlacementConstraintBuilder {

    @Override
    public String getIdentifier() {
        return "capacity";
    }

    @Override
    public String getSignature() {
        return getIdentifier() + "(" + PlacementConstraintBuilders.prettyTypeDeclaration("$n", 1, BtrpOperand.Type.node)
                + ","
                + PlacementConstraintBuilders.prettyTypeDeclaration("$nb", 0, BtrpOperand.Type.number) + ")";
    }

    @Override
    public Capacity buildConstraint(List<BtrpOperand> args) throws ConstraintBuilderException {
        PlacementConstraintBuilders.ensureArity(this, args, 2);
        ManagedElementSet<Node> ns = PlacementConstraintBuilders.makeNodes(args.get(0), true);
        if (ns.isEmpty()) {
            throw new ConstraintBuilderException(args.get(0) + " is an empty set");
        }
        int v = PlacementConstraintBuilders.makeInt(args.get(1));
        if (v < 0) {
            throw new ConstraintBuilderException("capacity can not be negative (" + v + ")");
        }
        return new Capacity(ns, v);
    }
}
