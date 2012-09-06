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

import entropy.configuration.ManagedElement;

/**
 * Denotes either a virtual machinee or a node.
 *
 * @author Fabien Hermenier
 */
public abstract class BtrpElement extends DefaultBtrpOperand implements Cloneable {

    /**
     * Get the {@link ManagedElement} related to this element.
     *
     * @return a non null {@link ManagedElement}
     */
    public abstract ManagedElement getElement();

    /**
     * Check the equality of two elements.
     * Both are equals if they are an instance of a same class and if they contains
     * the same element.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BtrpElement that = (BtrpElement) o;
        return getElement().equals(that.getElement());
    }

    @Override
    public int hashCode() {
        return label() == null ? getElement().hashCode() : label().hashCode();
    }

    /**
     * @return {@code 0}
     */
    @Override
    public int degree() {
        return 0;
    }

    @Override
    public BtrpNumber eq(BtrpOperand other) {
        if (this.equals(other)) {
            return BtrpNumber.TRUE;
        }
        return BtrpNumber.FALSE;
    }

    public abstract BtrpElement clone();
}
