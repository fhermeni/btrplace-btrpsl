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

import btrplace.btrpsl.Script;
import btrplace.btrpsl.ScriptBuilderException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A basic include mechanism where all the VJObs are added manually.
 *
 * @author Fabien Hermenier
 */
public class BasicIncludes implements Includes {
    private Map<String, Script> hash;

    public BasicIncludes() {
        this.hash = new HashMap<String, Script>();
    }

    @Override
    public List<Script> getVJob(String name) throws ScriptBuilderException {

        List<Script> vjobs = new ArrayList<Script>();
        if (!name.endsWith(".*")) {
            if (hash.containsKey(name)) {
                vjobs.add(hash.get(name));
            }
        } else {
            String base = name.substring(0, name.length() - 2);
            for (Map.Entry<String, Script> e : hash.entrySet()) {
                if (e.getKey().startsWith(base)) {
                    vjobs.add(e.getValue());
                }
            }
        }
        return vjobs;
    }

    /**
     * Add a vjob into the set of included vjobs.
     *
     * @param vjob the vjob to add
     */
    public void add(Script vjob) {
        this.hash.put(vjob.id(), vjob);
    }
}
