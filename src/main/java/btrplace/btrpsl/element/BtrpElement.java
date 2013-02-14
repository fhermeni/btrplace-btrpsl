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

package btrplace.btrpsl.element;


import java.util.UUID;

/**
 * Denotes either a VM or a node.
 * An element as a unique name but also a unique UUID that will be used by btrplace.
 *
 * @author Fabien Hermenier
 */
public class BtrpElement extends DefaultBtrpOperand implements Cloneable {

    private String name;

    private UUID uuid;

    private Type t;

    private String tpl;

    /**
     * Make a new element.
     *
     * @param t    the element type. Either {@link Type#VM} or {@link Type#node}.
     * @param name the element name
     * @param uuid the element uuid
     */
    public BtrpElement(Type t, String name, UUID uuid) {
        this.name = name;
        this.t = t;
        this.uuid = uuid;
    }

    /**
     * Get the element identifier.
     *
     * @return a non null String.
     */
    public String getElement() {
        return name;
    }

    /**
     * Get the element UUID.
     *
     * @return a UUID
     */
    public UUID getUUID() {
        return uuid;
    }

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
    public Type type() {
        return t;
    }

    @Override
    public BtrpNumber eq(BtrpOperand other) {
        if (this.equals(other)) {
            return BtrpNumber.TRUE;
        }
        return BtrpNumber.FALSE;
    }

    @Override
    public BtrpElement clone() {
        return new BtrpElement(t, name, uuid);
    }

    public void setTemplate(String t) {
        tpl = t;
    }

    public String getTemplate() {
        return tpl;
    }

    @Override
    public String toString() {
        return getElement();
    }
}
