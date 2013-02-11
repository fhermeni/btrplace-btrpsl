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
import btrplace.btrpsl.element.DefaultBtrpOperand;
import btrplace.btrpsl.element.IgnorableOperand;
import btrplace.btrpsl.tree.BtrPlaceTree;

import java.util.List;

/**
 * A toolkit class to ease the implementation of {@link PlacementConstraintBuilder}.
 *
 * @author Fabien Hermenier
 */
public abstract class DefaultPlacementConstraintBuilder implements PlacementConstraintBuilder {


    protected final ConstraintParam[] params;

    public DefaultPlacementConstraintBuilder(ConstraintParam[] ps) {
        params = ps;
    }

    @Override
    public ConstraintParam[] getParameters() {
        return params;
    }

    @Override
    public String getSignature() {
        StringBuilder b = new StringBuilder();
        b.append(getIdentifier()).append('(');
        for (int i = 0; i < params.length; i++) {
            b.append(params[i].prettySignature());
            if (i != params.length - 1) {
                b.append(", ");
            }
        }
        b.append(')');
        return b.toString();
    }

    @Override
    public String getFullSignature() {
        StringBuilder b = new StringBuilder();
        b.append(getIdentifier()).append('(');
        for (int i = 0; i < params.length; i++) {
            b.append(params[i].fullSignature());
            if (i != params.length - 1) {
                b.append(", ");
            }
        }
        b.append(')');
        return b.toString();
    }

    public boolean checkConformance(BtrPlaceTree t, List<BtrpOperand> ops) {
        //Arity error
        if (ops.size() != getParameters().length) {
            t.ignoreError("'" + pretty(ops) + "' cannot be casted to '" + getSignature() + "'");
            return false;
        }

        //Type checking
        for (int i = 0; i < ops.size(); i++) {
            BtrpOperand o = ops.get(i);
            ConstraintParam p = params[i];
            if (!p.isCompatibleWith(t, o)) {
                if (o != IgnorableOperand.getInstance()) {
                    t.ignoreError("'" + pretty(ops) + "' cannot be casted to '" + getSignature() + "'");
                }
                return false;
            }
        }
        return true;
    }

    private String pretty(List<BtrpOperand> ops) {
        StringBuilder b = new StringBuilder();
        b.append(getIdentifier()).append('(');
        for (int i = 0; i < ops.size(); i++) {
            b.append(DefaultBtrpOperand.prettyType(ops.get(i)));
            if (i != ops.size() - 1) {
                b.append(", ");
            }
        }
        b.append(")");
        return b.toString();
    }
}
