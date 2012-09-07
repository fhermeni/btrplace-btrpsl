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
import entropy.vjob.Ban;

import java.util.List;

/**
 * A builder to create Ban constraints.
 *
 * @author Fabien Hermenier
 */
public class BanBuilder implements PlacementConstraintBuilder {

    @Override
    public String getIdentifier() {
        return "ban";
    }

    @Override
    public String getSignature() {
        return getIdentifier() + "(" + PlacementConstraintBuilders.prettyTypeDeclaration("$v", 1, BtrpOperand.Type.vm)
                + ","
                + PlacementConstraintBuilders.prettyTypeDeclaration("$n", 1, BtrpOperand.Type.node) + ")";
    }

    /**
     * Build a ban constraint.
     *
     * @param args must be 2 VJobset, first contains virtual machines and the second nodes. Each set must not be empty
     * @return a constraint
     * @throws ConstraintBuilderException if arguments are not compatible with the constraint
     */
    @Override
    public Ban buildConstraint(List<BtrpOperand> args) throws ConstraintBuilderException {
        PlacementConstraintBuilders.ensureArity(this, args, 2);
        ManagedElementSet<VirtualMachine> vms = PlacementConstraintBuilders.makeVMs(args.get(0), true);
        PlacementConstraintBuilders.noEmptySets(args.get(0), vms);

        ManagedElementSet<Node> ns = PlacementConstraintBuilders.makeNodes(args.get(1), true);
        PlacementConstraintBuilders.noEmptySets(args.get(1), ns);
        return new Ban(vms, ns);
    }
}
