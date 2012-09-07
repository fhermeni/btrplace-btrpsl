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
import entropy.vjob.Offline;

import java.util.List;

/**
 * A builder to create Offline constraints.
 *
 * @author Fabien Hermenier
 */
public class OfflineBuilder implements PlacementConstraintBuilder {

    @Override
    public String getIdentifier() {
        return "offline";
    }

    @Override
    public String getSignature() {
        return getIdentifier() + "(" + PlacementConstraintBuilders.prettyTypeDeclaration("$n", 1, BtrpOperand.Type.node) + ")";
    }


    /**
     * Build an offline constraint.
     *
     * @param args must be 1 VJobset of node. The set must not be empty
     * @return a constraint
     * @throws ConstraintBuilderException if arguments are not compatible with the constraint
     */
    @Override
    public Offline buildConstraint(List<BtrpOperand> args) throws ConstraintBuilderException {
        PlacementConstraintBuilders.ensureArity(this, args, 1);
        ManagedElementSet<Node> ns = PlacementConstraintBuilders.makeNodes(args.get(0), true);
        PlacementConstraintBuilders.noEmptySets(args.get(0), ns);
        return new Offline(ns);
    }
}
