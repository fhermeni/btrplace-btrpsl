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

import java.util.HashSet;
import java.util.Set;

/**
 * A parameter for a constraint that denotes a set of nodes.
 *
 * @author Fabien Hermenier
 */
public class SetOfParam implements ConstraintParam<Set> {

    private String name;

    private boolean canBeEmpty = true;

    private BtrpOperand.Type type;

    private int depth;

    public SetOfParam(String n, BtrpOperand.Type t) {
        this(n, 1, t, true);
    }

    public SetOfParam(String n, int depth, BtrpOperand.Type t, boolean canBeEmpty) {
        this.name = n;
        this.canBeEmpty = canBeEmpty;
        this.type = t;
        this.depth = depth;
    }

    @Override
    public String prettySignature() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            b.append("set<");
        }
        b.append(type);
        for (int i = 0; i < depth; i++) {
            b.append(">");
        }
        return b.toString();
    }

    @Override
    public String fullSignature() {
        return name + ": " + prettySignature();
    }

    @Override
    public Set transform(PlacementConstraintBuilder cb, BtrPlaceTree tree, BtrpOperand op) {

        if (op == IgnorableOperand.getInstance()) {
            return null;
        }

        Set s = makeSet(depth, op);

        if (!canBeEmpty && s.isEmpty()) {
            tree.ignoreError("In '" + cb.getFullSignature() + "', '" + getName() + "' expects a non-empty set");
            return null;
        }
        return s;
    }

    private Set makeSet(int d, BtrpOperand o) {
        Set<Object> h = new HashSet<Object>();
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
                        h.add(makeSet(d - 1, op));
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
