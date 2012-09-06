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

package btrpsl.element;

/**
 * @author Fabien Hermenier
 */
public abstract class DefaultBtrpOperand implements BtrpOperand, Cloneable {

    private String label = null;

    @Override
    public String label() {
        return label;
    }

    @Override
    public void setLabel(String lbl) {
        this.label = lbl;
    }


    @Override
    public BtrpOperand not() {
        throw new UnsupportedOperationException("operator '!' unsupported");
    }


    @Override
    public BtrpOperand power(BtrpOperand nb) {
        throw new UnsupportedOperationException("operator '^' unsupported");
    }

    @Override
    public BtrpOperand plus(BtrpOperand other) {
        throw new UnsupportedOperationException("operator '+' unsupported");
    }

    @Override
    public BtrpOperand minus(BtrpOperand other) {
        throw new UnsupportedOperationException("operator '-' unsupported");
    }

    @Override
    public BtrpOperand negate() {
        throw new UnsupportedOperationException("operator '-' unsupported");
    }

    @Override
    public BtrpOperand mult(BtrpOperand other) {
        throw new UnsupportedOperationException("operator '*' unsupported");
    }

    @Override
    public BtrpOperand div(BtrpOperand other) {
        throw new UnsupportedOperationException("operator '/' unsupported");
    }

    @Override
    public BtrpOperand remainder(BtrpOperand other) {
        throw new UnsupportedOperationException("operator '%' unsupported");
    }

    @Override
    public BtrpNumber eq(BtrpOperand other) {
        throw new UnsupportedOperationException("operator '==' unsupported");
    }

    @Override
    public BtrpNumber geq(BtrpOperand other) {
        throw new UnsupportedOperationException("operator '>=' unsupported");
    }

    @Override
    public BtrpNumber gt(BtrpOperand other) {
        throw new UnsupportedOperationException("operator '>' unsupported");
    }

    public abstract BtrpOperand clone();

    /**
     * Pretty textual representation of the element type.
     *
     * @return a String
     */
    public String prettyType() {
        return prettyType(degree(), type());
    }

    /**
     * Pretty textual representation of a given element type.
     *
     * @param degree 0 for a litteral, 1 for a set, 2 for a set of sets, ...
     * @param t      the litteral
     * @return a String
     */
    public static String prettyType(int degree, Type t) {
        StringBuilder b = new StringBuilder();
        for (int i = degree; i > 0; i--) {
            b.append("set<");
        }
        b.append(t);
        for (int i = 0; i < degree; i++) {
            b.append(">");
        }
        return b.toString();
    }
}
