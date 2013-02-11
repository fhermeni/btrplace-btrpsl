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

package btrplace.btrpsl.includes;

import btrplace.btrpsl.BtrpScript;
import btrplace.btrpsl.BtrpScriptBuilderException;

import java.util.List;

/**
 * Denotes a library that is used to get the vjobs required from the 'use' statement.
 *
 * @author Fabien Hermenier
 */
public interface Includes {

    /**
     * Get a list of vjob from an identifier.
     * If the identifier ends with the '.*' wildcard, then any vjob matching this wildcard will be returned.
     * Otherwise, only the first vjob matching the identifier will be returned if it exists.
     *
     * @param name the identifier of the vjob
     * @return A list containing the matched vjob, may be empty.
     * @throws btrplace.btrpsl.BtrpScriptBuilderException
     *          if an error occurred while parsing the founded vjob
     */
    List<BtrpScript> getVJob(String name) throws BtrpScriptBuilderException;
}
