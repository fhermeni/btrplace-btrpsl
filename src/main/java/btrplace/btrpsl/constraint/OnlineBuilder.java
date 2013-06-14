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
import btrplace.model.Node;
import btrplace.model.constraint.Online;
import btrplace.model.constraint.SatConstraint;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A builder for {@link Online} constraints.
 *
 * @author Fabien Hermenier
 */
public class OnlineBuilder extends DefaultSatConstraintBuilder {

    /**
     * Make a new builder.
     */
    public OnlineBuilder() {
        super(new ConstraintParam[]{new SetOfParam("$n", 1, BtrpOperand.Type.node, false)});
    }

    @Override
    public String getIdentifier() {
        return "online";
    }

    /**
     * Build an online constraint.
     *
     * @param args must be 1 scriptset of node. The set must not be empty
     * @return a constraint
     */
    @Override
    public SatConstraint buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (checkConformance(t, args)) {
            @SuppressWarnings("unchecked") Set<Node> ns = (Set<Node>) params[0].transform(this, t, args.get(0));
            return (ns != null ? new Online(ns) : null);
        }
        return null;
    }
}
