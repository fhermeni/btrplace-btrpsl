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

import java.util.UUID;

/**
 * An interface to allow to retrieve and release UUID that
 * are the basic element identifiers in btrplace.
 *
 * @author Fabien Hermenier
 */
public interface UUIDPool {


    /**
     * Get a UUID.
     *
     * @return the UUID if possible, {@code null} if there is no UUIDs available
     */
    UUID request();

    /**
     * Release a UUID that will be available again.
     */
    void release(UUID u);
}
