/*
 * Copyright (c) Fabien Hermenier
 *
 *         This file is part of Entropy.
 *
 *         Entropy is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU Lesser General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         Entropy is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *         GNU Lesser General Public License for more details.
 *         You should have received a copy of the GNU Lesser General Public License
 *         along with Entropy.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrpsl.tree;

import btrpsl.ANTLRBtrplaceSL2Lexer;
import btrpsl.SemanticErrors;
import btrpsl.element.BtrpNumber;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.BtrpString;
import org.antlr.runtime.Token;

/**
 * A part of an enumeration.
 * Can be a single element or a range of numbers.
 * Returns a set of string
 *
 * @author Fabien Hermenier
 */
public class Range extends BtrPlaceTree {

    /**
     * Make a new tree
     *
     * @param payload the root token
     * @param errors  the errors to report
     */
    public Range(Token payload, SemanticErrors errors) {
        super(payload, errors);
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {

        BtrpSet s = new BtrpSet(1, BtrpOperand.Type.string);
        if (getChildCount() == 1) {
            if (getChild(0).getType() != ANTLRBtrplaceSL2Lexer.IDENTIFIER) {
                BtrpOperand o = getChild(0).go(this);
                if (o.degree() > 0) {
                    return ignoreError(o + ": sets are not allowed in an enumeration");
                }
                s.getValues().add(new BtrpString(getChild(0).go(this).toString()));
            } else {
                s.getValues().add(new BtrpString(getChild(0).getText()));
            }
        } else if (getChildCount() == 2) {
            BtrpOperand first = getChild(0).go(this);
            BtrpOperand last = getChild(1).go(this);
            if (first.type() != BtrpOperand.Type.number || last.type() != BtrpOperand.Type.number) {
                return ignoreError("Bounds must be numbers");
            }
            BtrpNumber begin = (BtrpNumber) first;
            BtrpNumber end = (BtrpNumber) last;
            if (!begin.isInteger() || !end.isInteger()) {
                return ignoreError("Bounds must be integers");
            }

            if (begin.getBase() != end.getBase()) {
                return ignoreError("bounds must be expressed in the same base");
            }

            int from = Math.min(begin.getIntValue(), end.getIntValue());
            int to = Math.max(begin.getIntValue(), end.getIntValue());
            for (int i = from; i <= to; i++) {
                BtrpNumber bi = new BtrpNumber(i, begin.getBase()); //Keep the base
                s.getValues().add(new BtrpString(bi.toString()));
            }
        } else {
            return ignoreError("Bug in range");
        }

        return s;
    }
}
