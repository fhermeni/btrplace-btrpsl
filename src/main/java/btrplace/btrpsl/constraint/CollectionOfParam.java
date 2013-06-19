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
import btrplace.btrpsl.element.IgnorableOperand;
import btrplace.btrpsl.tree.BtrPlaceTree;

import java.util.Collection;

/**
 * A parameter for a constraint that denotes a set of elements.
 *
 * @author Fabien Hermenier
 */
public abstract class CollectionOfParam<E extends Collection> implements ConstraintParam<E> {

    private String name;

    protected boolean canBeEmpty = true;

    protected BtrpOperand.Type type;

    protected int depth;

    /**
     * Make a new parameter for a simple set, possibly empty, of elements.
     *
     * @param n the parameter name
     * @param t the type of the element inside the set
     */
    public CollectionOfParam(String n, BtrpOperand.Type t) {
        this(n, 1, t, true);
    }

    /**
     * Make a new set parameter
     *
     * @param n          the parameter name
     * @param depth      the set depth
     * @param t          the type of the elements inside the set
     * @param canBeEmpty {@code true} to allow empty sets.
     */
    public CollectionOfParam(String n, int depth, BtrpOperand.Type t, boolean canBeEmpty) {
        this.name = n;
        this.canBeEmpty = canBeEmpty;
        this.type = t;
        this.depth = depth;
    }

    @Override
    public String prettySignature() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            b.append("collection<");
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
    public abstract E transform(SatConstraintBuilder cb, BtrPlaceTree tree, BtrpOperand op);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompatibleWith(BtrPlaceTree t, BtrpOperand o) {
        return (o == IgnorableOperand.getInstance() || (o.type() == type && (o.degree() == depth || (depth == 1 && o.degree() == 0))));
    }
}
