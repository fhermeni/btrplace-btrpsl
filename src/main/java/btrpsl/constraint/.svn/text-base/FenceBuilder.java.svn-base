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
import entropy.vjob.Fence;

import java.util.List;

/**
 * A builder to make Fence constraint.
 *
 * @author Fabien Hermenier
 */
public class FenceBuilder implements PlacementConstraintBuilder {

    @Override
    public String getIdentifier() {
        return "fence";
    }

    @Override
    public String getSignature() {
        return "fence(<vmset>, <nodeset>)";
    }

    /**
     * Build a constraint.
     *
     * @param args the parameters to use. Must be 2 non-empty set. One of virtual machines and one of nodes.
     * @return a constraint
     * @throws ConstraintBuilderException if an error occurred while building the constraint.
     */
    @Override
    public Fence buildConstraint(List<BtrpOperand> args) throws ConstraintBuilderException {
        PlacementConstraintBuilders.ensureArity(this, args, 2);
        ManagedElementSet<VirtualMachine> vms = PlacementConstraintBuilders.makeVMs(args.get(0), true);
        PlacementConstraintBuilders.noEmptySets(args.get(0), vms);
        ManagedElementSet<Node> ns = PlacementConstraintBuilders.makeNodes(args.get(1), true);
        PlacementConstraintBuilders.noEmptySets(args.get(1), ns);
        PlacementConstraintBuilders.noEmptySets(args.get(1), vms);
        return new Fence(vms, ns);
    }
}
