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
import entropy.vjob.ContinuousSpread;

import java.util.List;

/**
 * A builder to make ContinuousSpread constraints.
 *
 * @author Fabien Hermenier
 */
public class ContinuousSpreadBuilder extends DefaultPlacementConstraintBuilder {

    public ContinuousSpreadBuilder() {
        super(new ConstraintParam[]{new SetOfVMsParam("$v", false)});
    }
    @Override
    public String getIdentifier() {
        return "spread";
    }

    @Override
    public ContinuousSpread buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (checkConformance(t, args)) {
            @SuppressWarnings("unchecked") ManagedElementSet<VirtualMachine> vms = (ManagedElementSet<VirtualMachine>) params[0].transform(this, t, args.get(0));
            return (vms != null ? new ContinuousSpread(vms) : null);
        }
        return null;
    }
}
