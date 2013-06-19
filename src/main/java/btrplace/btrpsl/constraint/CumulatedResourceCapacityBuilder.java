/*
 * Copyright (c) 2013 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
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
import btrplace.model.constraint.CumulatedResourceCapacity;
import btrplace.model.constraint.SatConstraint;

import java.util.List;
import java.util.Set;

/**
 * A builder for {@link CumulatedResourceCapacity} constraints.
 *
 * @author Fabien Hermenier
 */
public class CumulatedResourceCapacityBuilder extends DefaultSatConstraintBuilder {

    /**
     * Make a new builder.
     */
    public CumulatedResourceCapacityBuilder() {
        super("cumulatedResourceCapacity", new ConstraintParam[]{new SetOfParam("$n", 1, BtrpOperand.Type.node, false), new StringParam("$rcId"), new NumberParam("$nb")});
    }

    @Override
    public SatConstraint buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        if (!checkConformance(t, args)) {
            return null;
        }
        @SuppressWarnings("unchecked") Set<Node> ns = (Set<Node>) params[0].transform(this, t, args.get(0));
        @SuppressWarnings("unchecked") String rcId = (String) params[1].transform(this, t, args.get(1));
        @SuppressWarnings("unchecked") Number v = (Number) params[2].transform(this, t, args.get(2));

        if (v.doubleValue() < 0) {
            t.ignoreError("Parameter '" + params[2].getName() + "' expects a positive integer (" + v + " given)");
            v = null;
        }
        if (v != null && Math.rint(v.doubleValue()) != v.doubleValue()) {
            t.ignoreError("Parameter '" + params[2].getName() + "' expects an integer, not a real number (" + v + " given)");
            v = null;
        }

        return (ns != null && v != null ? new CumulatedResourceCapacity(ns, rcId, v.intValue()) : null);
    }
}
