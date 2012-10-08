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
import entropy.vjob.Ban;

import java.util.List;

/**
 * A builder to create Ban constraints.
 *
 * @author Fabien Hermenier
 */
public class BanBuilder extends DefaultPlacementConstraintBuilder {

    public BanBuilder() {
        super(new ConstraintParam[]{new SetOfVMsParam("$v", false), new SetOfNodesParam("$n", false)});
    }

    @Override
    public String getIdentifier() {
        return "ban";
    }

    /**
     * Build a ban constraint.
     *
     * @param t    the current tree
     * @param args must be 2 operands, first contains virtual machines and the second nodes. Each set must not be empty
     * @return a constraint
     */
    @Override
    public Ban buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (!checkConformance(t, args)) {
            return null;
        }
        @SuppressWarnings("unchecked") ManagedElementSet<VirtualMachine> vms = (ManagedElementSet<VirtualMachine>)params[0].transform(t, args.get(0));
        @SuppressWarnings("unchecked") ManagedElementSet<Node> ns = (ManagedElementSet<Node>) params[1].transform(t, args.get(1));
        if (vms != null && ns != null) {
            return new Ban(vms, ns);
        }
        return null;
    }
}
