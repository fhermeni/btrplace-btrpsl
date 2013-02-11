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

import btrplace.btrpsl.element.BtrpNumber;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.IgnorableOperand;
import btrplace.btrpsl.tree.BtrPlaceTree;

/**
 * A parameter for a constraint that denotes an integer.
 *
 * @author Fabien Hermenier
 */
public class IntParam implements ConstraintParam<Integer> {

    private String name;

    public IntParam(String n) {
        this.name = n;
    }

    @Override
    public String prettySignature() {
        return "number";
    }

    @Override
    public String fullSignature() {
        return name + ": number";
    }

    @Override
    public Integer transform(PlacementConstraintBuilder cb, BtrPlaceTree tree, BtrpOperand op) {
        if (op == IgnorableOperand.getInstance()) {
            throw new UnsupportedOperationException();
        }
        return ((BtrpNumber) op).getIntValue();

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompatibleWith(BtrPlaceTree t, BtrpOperand o) {
        return o == IgnorableOperand.getInstance() || (o.type() == BtrpOperand.Type.number && o.degree() == 0);
    }
}
