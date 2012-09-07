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
public class AmongBuilder extends DefaultPlacementConstraintBuilder {

    private static ConstraintParameter[] params = new ConstraintParameter[]{
            new ConstraintParameter(BtrpOperand.Type.vm, 1, "$v"),
            new ConstraintParameter(BtrpOperand.Type.node, 2, "$n")
    };

    @Override
    public String getIdentifier() {
        return "among";
    }

    @Override
    public ConstraintParameter[] getParameters() {
        return params;
    }

    /**
     * Build a constraint.
     *
     * @param args the argument. Must be a non-empty set of virtual machines and a multiset of nodes with
     *             at least two non-empty sets. If the multi set contains only one set, a {@code Fence} constraint is created
     * @return the constraint
     */
    @Override
    public PlacementConstraint buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (!checkConformance(t, args)) {
            return null;
        }
        ManagedElementSet<VirtualMachine> vms = PlacementConstraintBuilders.makeVMs(t, args.get(0));
        boolean ret = minCardinality(t, args.get(0), vms, 1);
        Set<ManagedElementSet<Node>> nss = PlacementConstraintBuilders.makeNodesSet(t, args.get(1));
        if (nss != null) {
            for (ManagedElementSet<Node> ns : nss) {
                if (ns.isEmpty()) {
                    t.ignoreError(getSignature() + " does not expect empty set of nodes. Provided: '" + args.get(1) + "'");
                    ret = false;
                }
            }
        }
        ret &= minCardinality(t, args.get(1), nss, 1);
        if (nss != null && nss.size() == 1) {
            return (ret && vms != null) ? new Fence(vms, nss.iterator().next()) : null;
        }

        return (ret && vms != null && nss != null ? new Among(vms, nss) : null);
    }
}