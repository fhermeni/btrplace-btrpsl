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

import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.BtrpSet;
import btrplace.btrpsl.element.IgnorableOperand;
import btrplace.btrpsl.tree.BtrPlaceTree;

import java.util.ArrayList;
import java.util.List;

/**
 * A parameter for a constraint that denotes a list of elements.
 *
 * @author Fabien Hermenier
 */
public class ListOfParam implements ConstraintParam<List> {

    private String name;

    private boolean canBeEmpty = true;

    private BtrpOperand.Type type;

    private int depth;

    /**
     * Make a new parameter for a simple list, possibly empty, of elements.
     *
     * @param n the parameter name
     * @param t the type of the elements inside the list
     */
    public ListOfParam(String n, BtrpOperand.Type t) {
        this(n, 1, t, true);
    }

    /**
     * Make a new list parameter
     *
     * @param n          the parameter name
     * @param depth      the list depth
     * @param t          the type of the elements inside the list
     * @param canBeEmpty {@code true} to allow empty lists.
     */
    public ListOfParam(String n, int depth, BtrpOperand.Type t, boolean canBeEmpty) {
        this.name = n;
        this.canBeEmpty = canBeEmpty;
        this.type = t;
        this.depth = depth;
    }

    @Override
    public String prettySignature() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            b.append("seq<");
        }
        b.append(type);
        for (int i = 0; i < depth; i++) {
            b.append('>');
        }
        return b.toString();
    }

    @Override
    public String fullSignature() {
        return name + ": " + prettySignature();
    }

    @Override
    public List transform(SatConstraintBuilder cb, BtrPlaceTree tree, BtrpOperand op) {

        if (op == IgnorableOperand.getInstance()) {
            return null;
        }

        List s = makeList(depth, op);

        if (!canBeEmpty && s.isEmpty()) {
            tree.ignoreError("In '" + cb.getFullSignature() + "', '" + getName() + "' expects a non-empty set");
            return null;
        }
        return s;
    }

    private List makeList(int d, BtrpOperand o) {
        List<Object> h = new ArrayList<Object>();
        if (d == 0) {
            if (o.type() == BtrpOperand.Type.VM || o.type() == BtrpOperand.Type.node) {
                h.add(((BtrpElement) o).getUUID());
            }
        } else {
            if (o instanceof BtrpElement && d == 1) {
                h.add(((BtrpElement) o).getUUID());
            } else {
                BtrpSet x = (BtrpSet) o;
                if (d == 1) {

                    for (BtrpOperand op : x.getValues()) {
                        if (op.type() == BtrpOperand.Type.VM || op.type() == BtrpOperand.Type.node) {
                            h.add(((BtrpElement) op).getUUID());
                        }
                    }
                } else {
                    for (BtrpOperand op : x.getValues()) {
                        h.add(makeList(d - 1, op));
                    }
                }
            }
        }
        return h;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompatibleWith(BtrPlaceTree t, BtrpOperand o) {
        return (o == IgnorableOperand.getInstance() || (o.type() == type && (o.degree() == depth || (depth == 1 && o.degree() == 0))));
    }
}