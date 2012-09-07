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

    private static ConstraintParameter[] params = new ConstraintParameter[]{
            new ConstraintParameter(BtrpOperand.Type.vm, 1, "$v"),
            new ConstraintParameter(BtrpOperand.Type.node, 1, "$n")
    };

    @Override
    public ConstraintParameter[] getParameters() {
        return params;
    }

    @Override
    public String getIdentifier() {
        return "ban";
    }

    /**
     * Build a ban constraint.
     *
     * @param t    the current tree
     * @param args must be 2 VJobset, first contains virtual machines and the second nodes. Each set must not be empty
     * @return a constraint
     */
    @Override
    public Ban buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (!checkConformance(t, args)) {
            return null;
        }
        ManagedElementSet<VirtualMachine> vms = PlacementConstraintBuilders.makeVMs(t, args.get(0));
        boolean ret = minCardinality(t, args.get(0), vms, 1);
        ManagedElementSet<Node> ns = PlacementConstraintBuilders.makeNodes(t, args.get(1));
        ret &= minCardinality(t, args.get(1), ns, 1);
        return (ret && vms != null && ns != null ? new Ban(vms, ns) : null);
    }
}
