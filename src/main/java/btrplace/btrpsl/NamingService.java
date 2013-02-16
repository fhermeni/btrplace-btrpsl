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

package btrplace.btrpsl;

import btrplace.btrpsl.element.BtrpElement;

/**
 * A service to declare a unique identifier for a VM or a node.
 * This will associate to each identifier a unique UUID that
 * can be used inside btrplace.
 *
 * @author Fabien Hermenier
 */
public interface NamingService {

    /**
     * Register an element.
     *
     * @param id the element identifier. Starts with a {@code \@} to indicate
     *           a node. Otherwise, the element will be considered as a virtual machine
     * @return the registered element if the operation succeed. {@code null} otherwise
     */
    BtrpElement register(String id) throws NamingServiceException;

    /**
     * Release an element.
     * Its name and its UUID will be available again for other elements.
     *
     * @param e the element to release
     * @return {@code true} if the element was registered and the operation succeeded
     */
    boolean release(BtrpElement e);

    /**
     * Get the element associated to a given identifier.
     *
     * @param n the element identifier
     * @return the matching element if any, {@code null} otherwise
     */
    BtrpElement resolve(String n);
}
