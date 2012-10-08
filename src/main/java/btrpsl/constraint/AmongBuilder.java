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
import entropy.vjob.PlacementConstraint;

import java.util.List;
import java.util.Set;

/**
 * A builder to make Among constraint.
 *
 * @author Fabien Hermenier
 */
public class AmongBuilder extends DefaultPlacementConstraintBuilder {

    public AmongBuilder() {
        super(new ConstraintParam[]{new SetOfVMsParam("$v", false), new SetSetOfNodesParam("$ns", false, false)});
    }
    @Override
    public String getIdentifier() {
        return "among";
    }

    /**
     * Build a constraint.
     *
     * @param t    the current tree
     * @param args the argument. Must be a non-empty set of virtual machines and a multiset of nodes with
     *             at least two non-empty sets. If the multi set contains only one set, a {@code Fence} constraint is created
     * @return the constraint
     */
    @Override
    public PlacementConstraint buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (checkConformance(t, args)) {
            @SuppressWarnings("unchecked") Set<ManagedElementSet<Node>> nss = (Set<ManagedElementSet<Node>>) params[1].transform(this, t, args.get(1));
            @SuppressWarnings("unchecked") ManagedElementSet<VirtualMachine> vms = (ManagedElementSet<VirtualMachine>) params[0].transform(this, t, args.get(0));
            return (vms != null && nss != null ? new Among(vms, nss) : null);
        }
        return null;
    }
}