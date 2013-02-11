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

package btrplace.btrpsl.tree;

import btrplace.btrpsl.ErrorReporter;
import btrplace.btrpsl.element.BtrpNumber;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.IgnorableOperand;
import org.antlr.runtime.Token;

/**
 * Logical or operand between two expressions
 *
 * @author Fabien Hermenier
 */
public class OrOperator extends BtrPlaceTree {

    /**
     * Make a new operator
     *
     * @param t    the 'OR' token
     * @param errs the list of errors.
     */
    public OrOperator(Token t, ErrorReporter errs) {
        super(t, errs);
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        BtrpOperand l = getChild(0).go(this);
        BtrpOperand r = getChild(1).go(this);

        if (l == IgnorableOperand.getInstance() || r == IgnorableOperand.getInstance()) {
            return IgnorableOperand.getInstance();
        }
        if (!(l instanceof BtrpNumber)) {
            return ignoreError("Expression expected, but was '" + l + "'");
        }
        if (!(r instanceof BtrpNumber)) {
            return ignoreError("Expression expected, but was '" + r + "'");
        }

        boolean bl = ((BtrpNumber) l).getIntValue() != BtrpNumber.FALSE.getIntValue();
        boolean br = ((BtrpNumber) r).getIntValue() != BtrpNumber.FALSE.getIntValue();
        return (bl || br) ? BtrpNumber.TRUE : BtrpNumber.FALSE;
    }
}
