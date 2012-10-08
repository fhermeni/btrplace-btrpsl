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
import entropy.configuration.VirtualMachine;
import entropy.vjob.LazySplit;

import java.util.List;

/**
 * A builder for LazySplitBuilder.
 *
 * @author Fabien Hermenier
 */
public class LazySplitBuilder extends DefaultPlacementConstraintBuilder {

    public LazySplitBuilder() {
        super(new ConstraintParam[]{new SetOfVMsParam("$v1", false), new SetOfVMsParam("$v2", false)});
    }

    @Override
    public String getIdentifier() {
        return "split";
    }

    /**
     * Build a constraint.
     *
     * @param args the parameters of the constraint. Must be 2 non-empty set of virtual machines.
     * @return the constraint
     */
    @Override
    public LazySplit buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (checkConformance(t, args)) {
            @SuppressWarnings("unchecked") ManagedElementSet<VirtualMachine> vms1 = (ManagedElementSet<VirtualMachine>)params[0].transform(this, t, args.get(0));
            @SuppressWarnings("unchecked") ManagedElementSet<VirtualMachine> vms2 = (ManagedElementSet<VirtualMachine>)params[1].transform(this, t, args.get(1));
            return (vms1 != null && vms2 != null ? new LazySplit(vms1, vms2) : null);
        }
        return null;
    }
}
