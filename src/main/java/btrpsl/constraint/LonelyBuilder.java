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
import entropy.configuration.VirtualMachine;
import entropy.vjob.Lonely;

import java.util.List;

/**
 * A builder for the placement constraint Lonely.
 *
 * @author Fabien Hermenier
 */
public class LonelyBuilder implements PlacementConstraintBuilder {

    @Override
    public String getIdentifier() {
        return "lonely";
    }

    @Override
    public String getSignature() {
        return getIdentifier() + "(" + PlacementConstraintBuilders.prettyTypeDeclaration("$v", 1, BtrpOperand.Type.vm)
                + ")";
    }

    /**
     * Build a constraint.
     *
     * @param args the parameters of the constraint. Must be one non-empty set of virtual machines.
     * @return the constraint
     * @throws ConstraintBuilderException if an error occurred while building the constraint
     */
    @Override
    public Lonely buildConstraint(List<BtrpOperand> args) throws ConstraintBuilderException {
        PlacementConstraintBuilders.ensureArity(this, args, 1);
        ManagedElementSet<VirtualMachine> vms = PlacementConstraintBuilders.makeVMs(args.get(0), true);
        PlacementConstraintBuilders.noEmptySets(args.get(0), vms);
        return new Lonely(vms);
    }
}
