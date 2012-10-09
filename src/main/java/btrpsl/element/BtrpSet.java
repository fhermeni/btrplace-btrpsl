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

import gnu.trove.THashSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Denotes a set of operand.
 * The set is homogeneous: every operand into the set have the same type and the same degree.
 * The degree of a set is then one greater than the operand into it.
 *
 * @author Fabien Hermenier
 */
public class BtrpSet extends DefaultBtrpOperand implements Cloneable {

    /**
     * The operands in the set.
     */
    private List<BtrpOperand> values;

    /**
     * The degree of the set.
     */
    private int degree;

    /**
     * The type of the operand into the set.
     */
    private Type t;

    @Override
    public Type type() {
        return t;
    }

    /**
     * Make a new set with a specific degree and type
     *
     * @param d the degree of the set
     * @param t the type of the set
     */
    public BtrpSet(int d, Type t) {
        values = new ArrayList<BtrpOperand>();
        this.degree = d;
        this.t = t;
    }

    @Override
    public BtrpSet plus(BtrpOperand s) {
        if (degree != s.degree() || t != s.type()) {
            throw new UnsupportedOperationException("Non-homogeneous union between a '" + prettyType() + "' and a '" + s.prettyType() + "'");
        }
        BtrpSet res = new BtrpSet(degree, t);
        THashSet<BtrpOperand> used = new THashSet<BtrpOperand>();
        for (BtrpOperand x : values) {
            res.add(x);
            used.add(x);
        }
        List<BtrpOperand> other = ((BtrpSet) s).values;
        for (BtrpOperand x : other) {
            if (!used.contains(x)) {
                res.add(x);
            }
        }
        return res;
    }

    @Override
    public BtrpSet minus(BtrpOperand s) {
        if (degree != s.degree() || t != s.type()) {
            throw new UnsupportedOperationException("Non-homogeneous subtraction between a '" + prettyType() + "' and a '" + s.prettyType() + "'");
        }
        BtrpSet res = new BtrpSet(degree, t);
        THashSet<BtrpOperand> used = new THashSet<BtrpOperand>();
        if (degree == s.degree()) {
            List<BtrpOperand> other = ((BtrpSet) s).values;
            used.addAll(other);
            for (BtrpOperand x : values) {
                if (!used.contains(x)) {
                    res.add(x);
                }
            }
        }
        return res;
    }

    /**
     * Get the number of operand in this set.
     *
     * @return a positive integer
     */
    public int size() {
        return values.size();
    }

    @Override
    public int degree() {
        return degree;
    }

    private void add(BtrpOperand s) {
        if (s.degree() != degree() - 1 || t != s.type()) {
            throw new UnsupportedOperationException("Cannot add a '" + s.prettyType() + "' to a '" + prettyType() + "'. Expect a '" + DefaultBtrpOperand.prettyType(degree() - 1, type()) + "'");
        }
        values.add(s);
    }

    @Override
    public BtrpSet remainder(BtrpOperand other) {
        if (other instanceof BtrpNumber) {
            BtrpNumber x = (BtrpNumber) other;
            if (!x.isInteger()) {
                throw new UnsupportedOperationException("Integer divider expected");
            }
            int size = x.getIntValue();
            if (size == 0) {
                throw new UnsupportedOperationException("cannot split into empty subsets");
            }
            BtrpSet res = new BtrpSet(degree + 1, t);
            res.t = t;
            BtrpSet s = new BtrpSet(degree, t);
            s.t = t;
            res.add(s);
            for (Iterator<BtrpOperand> ite = values.iterator(); ite.hasNext(); ) {
                BtrpOperand v = ite.next();
                s.add(v);
                if (s.size() == size && ite.hasNext()) {
                    s = new BtrpSet(degree, t);
                    res.add(s);
                }
            }
            return res;
        }
        throw new UnsupportedOperationException("Integer divider expected");
    }

    @Override
    public BtrpSet div(BtrpOperand nb) {
        if (nb instanceof BtrpNumber) {
            BtrpNumber x = (BtrpNumber) nb;
            if (!x.isInteger()) {
                throw new UnsupportedOperationException("Integer divider expected");
            }
            int s = x.getIntValue();
            if (s == 0) {
                throw new UnsupportedOperationException("Illegal division by 0");
            }
            if (s > size()) {
                throw new UnsupportedOperationException("Divider can not be greater than the set cardinality");
            }
            int card = (int) Math.ceil(size() * 1.0 / s);
            BtrpSet res = new BtrpSet(degree() + 1, t);
            BtrpSet cur = new BtrpSet(degree(), t);
            res.add(cur);
            for (int i = 0; i < size(); i++) {
                cur.add(values.get(i));
                if (cur.size() == card && i != size() - 1) {
                    cur = new BtrpSet(degree(), t);
                    res.add(cur);
                }
            }
            return res;
        }
        throw new UnsupportedOperationException("Integer divider expected");
    }

    @Override
    public BtrpSet mult(BtrpOperand s) {
        if (degree != s.degree() || t != s.type()) {
            throw new UnsupportedOperationException("Non-homogeneous cartesian product between a '" + prettyType() + "' and a '" + s.prettyType() + "'");
        }
        BtrpSet s2 = (BtrpSet) s;
        BtrpOperand[] mine = values.toArray(new BtrpOperand[values.size()]);
        BtrpOperand[] other = s2.values.toArray(new BtrpOperand[s2.size()]);
        BtrpSet res = new BtrpSet(degree + 1, t);
        THashSet<THashSet<BtrpOperand>> used = new THashSet<THashSet<BtrpOperand>>();
        if (s2.size() == 0) {
            return this.clone();
        }
        for (BtrpOperand i : mine) {
            THashSet<BtrpOperand> u = new THashSet<BtrpOperand>();
            u.add(i);
            for (BtrpOperand j : other) {
                if (u.add(j) && !used.contains(u)) {
                    BtrpSet cur = new BtrpSet(degree(), type());
                    cur.add(i);
                    cur.add(j);
                    res.add(cur);
                    used.add(u);
                }
            }
            u.clear();
        }
        return res;
    }

    @Override
    public BtrpSet power(BtrpOperand nb) {
        if (nb instanceof BtrpNumber) {
            BtrpNumber x = (BtrpNumber) nb;
            if (!((BtrpNumber) nb).isInteger()) {
                throw new UnsupportedOperationException("Integer divider expected");
            }
            int val = x.getIntValue();
            if (val != 2) {
                throw new UnsupportedOperationException("Unable to compute other than a power of 2");
            }
            return this.mult(this);
        }
        throw new UnsupportedOperationException("Integer divider expected");
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("{");
        for (Iterator ite = values.iterator(); ite.hasNext(); ) {
            buf.append(ite.next());
            if (ite.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    @Override
    public BtrpSet clone() {
        BtrpSet elems = new BtrpSet(degree, t);
        for (BtrpOperand e : values) {
            elems.add(e.clone());
        }
        return elems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BtrpSet that = (BtrpSet) o;

        return degree == that.degree && values.containsAll(that.values) && that.values.containsAll(values);
    }

    @Override
    public BtrpNumber eq(BtrpOperand other) {
        if (this.equals(other)) {
            return BtrpNumber.TRUE;
        }
        return BtrpNumber.FALSE;
    }

    @Override
    public int hashCode() {
        int result = values != null ? values.hashCode() : 0;
        result = 31 * result + degree;
        return result;
    }

    public List<BtrpOperand> getValues() {
        return values;
    }
}
