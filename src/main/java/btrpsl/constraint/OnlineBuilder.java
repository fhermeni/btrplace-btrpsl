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
import entropy.vjob.Online;

import java.util.List;

/**
 * A builder to create Online constraints.
 *
 * @author Fabien Hermenier
 */
public class OnlineBuilder extends DefaultPlacementConstraintBuilder {

    public OnlineBuilder() {
        super(new ConstraintParam[]{new SetOfNodesParam("$n")});
    }
    @Override
    public String getIdentifier() {
        return "online";
    }

    /**
     * Build an online constraint.
     *
     * @param args must be 1 VJobset of node. The set must not be empty
     * @return a constraint
     */
    @Override
    public Online buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (checkConformance(t, args)) {
            @SuppressWarnings("unchecked") ManagedElementSet<Node> ns = (ManagedElementSet<Node>)params[0].transform(t, args.get(0));
            return (ns != null ? new Online(ns) : null);
        }
        return null;
    }
}
