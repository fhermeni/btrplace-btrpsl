/*
 * Copyright (c) 2012 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrplace.btrpsl.constraint;

import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.tree.BtrPlaceTree;
import btrplace.model.Node;
import btrplace.model.VM;
import btrplace.model.constraint.Ban;
import btrplace.model.constraint.SatConstraint;

import java.util.List;
import java.util.Set;

/**
 * Builder for {@link Ban} constraints.
 *
 * @author Fabien Hermenier
 */
public class BanBuilder extends DefaultSatConstraintBuilder {

    /**
     * Make a new builder.
     */
    public BanBuilder() {
        super("ban", new ConstraintParam[]{new SetOfParam("$v", 1, BtrpElement.Type.VM, false), new SetOfParam("$n", 1, BtrpOperand.Type.node, false)});
    }

    /**
     * Build a ban constraint.
     *
     * @param t    the current tree
     * @param args must be 2 operands, first contains virtual machines and the second nodes. Each set must not be empty
     * @return a constraint
     */
    @Override
    public SatConstraint buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (!checkConformance(t, args)) {
            return null;
        }
        @SuppressWarnings("unchecked") Set<VM>vms = (Set<VM>) params[0].transform(this, t, args.get(0));
        @SuppressWarnings("unchecked") Set<Node> ns = (Set<Node>) params[1].transform(this, t, args.get(1));
        if (vms != null && ns != null) {
            return new Ban(vms, ns);
        }
        return null;
    }
}
