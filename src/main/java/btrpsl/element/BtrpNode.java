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

import entropy.configuration.Node;

/**
 * An operand that point to a node.
 *
 * @author Fabien Hermenier
 */
public class BtrpNode extends BtrpElement implements Cloneable {

    private Node node;

    /**
     * Make a new operand.
     *
     * @param n the associated node
     */
    public BtrpNode(Node n) {
        this.node = n;
    }

    @Override
    public Node getElement() {
        return node;
    }

    @Override
    public BtrpNode clone() {
        return new BtrpNode(node);
    }

    @Override
    public Type type() {
        return Type.node;
    }

    @Override
    public String toString() {
        return node.getName();
    }
}
