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
import entropy.configuration.VirtualMachine;
import entropy.vjob.Among;
import entropy.vjob.Fence;
import entropy.vjob.PlacementConstraint;

import java.util.List;
import java.util.Set;

/**
 * A builder to make Among constraint.
 *
 * @author Fabien Hermenier
 */
public class AmongBuilder implements PlacementConstraintBuilder {

    @Override
    public String getIdentifier() {
        return "among";
    }

    @Override
    public String getSignature() {
        return getIdentifier() + "(" + PlacementConstraintBuilders.prettyTypeDeclaration("$v", 1, BtrpOperand.Type.vm)
                + ","
                + PlacementConstraintBuilders.prettyTypeDeclaration("$n", 2, BtrpOperand.Type.node) + ")";
    }

    /**
     * Build a constraint.
     *
     * @param args the argument. Must be a non-empty set of virtual machines and a multiset of nodes with
     *             at least two non-empty sets. If the multi set contains only one set, a {@code Fence} constraint is created
     * @return the constraint
     * @throws ConstraintBuilderException if an error occurred while building the constraint
     */
    @Override
    public PlacementConstraint buildConstraint(List<BtrpOperand> args) throws ConstraintBuilderException {
        PlacementConstraintBuilders.ensureArity(this, args, 2);
        ManagedElementSet<VirtualMachine> vms = PlacementConstraintBuilders.makeVMs(args.get(0), true);
        PlacementConstraintBuilders.noEmptySets(args.get(0), vms);
        Set<ManagedElementSet<Node>> nss = PlacementConstraintBuilders.makeNodesSet(args.get(1));
        PlacementConstraintBuilders.noEmptySets(args.get(1), nss);
        for (ManagedElementSet<Node> s : nss) {
            PlacementConstraintBuilders.noEmptySets(args.get(1), s);
        }
        if (nss.size() == 1) {
            return new Fence(vms, nss.iterator().next());
        }
        return new Among(vms, nss);
    }
}