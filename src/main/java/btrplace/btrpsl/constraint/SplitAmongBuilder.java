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

import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.tree.BtrPlaceTree;
import btrplace.model.SatConstraint;
import btrplace.model.constraint.SplitAmong;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A builder for SplitAmong constraints.
 *
 * @author Fabien Hermenier
 */
public class SplitAmongBuilder extends DefaultPlacementConstraintBuilder {

    public SplitAmongBuilder() {
        super(new ConstraintParam[]{new SetOfParam("$vms", 2, BtrpOperand.Type.VM, false), new SetOfParam("$ns", 2, BtrpOperand.Type.node, false)});
    }

    @Override
    public String getIdentifier() {
        return "splitAmong";
    }

    /**
     * Build a constraint.
     *
     * @param args the parameters of the constraint. Must be 2 non-empty set of virtual machines.
     * @return the constraint
     */
    @Override
    public SatConstraint buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (checkConformance(t, args)) {
            @SuppressWarnings("unchecked") Set<Set<UUID>> vs = (Set<Set<UUID>>) params[0].transform(this, t, args.get(0));
            @SuppressWarnings("unchecked") Set<Set<UUID>> ps = (Set<Set<UUID>>) params[1].transform(this, t, args.get(1));
            return (vs != null && ps != null ? new SplitAmong(vs, ps, false) : null);
        }
        return null;
    }
}
