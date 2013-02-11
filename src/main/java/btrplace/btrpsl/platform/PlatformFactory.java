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

package btrplace.btrpsl.platform;

import java.util.Set;

/**
 * A factory to create platform for nodes
 *
 * @author Fabien Hermenier
 */
public interface PlatformFactory {

    /**
     * Get the available hosting platform
     *
     * @return a set of identifiers that may be empty.
     */
    Set<String> getAvailables();

    /**
     * Get an hosting platform associated with the identifier.
     * The platform is cloned and options can be added without side effects
     *
     * @param k the identifier of the platform
     * @return a platform if the given identifier match an available platform or {@code null}
     */
    Platform getPlatform(String k);

}
