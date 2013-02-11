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
import btrplace.btrpsl.element.BtrpOperand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Fabien Hermenier
 */
public class NamingService {

    private Map<UUID, String> rev;

    private Map<String, BtrpElement> resolve;

    public NamingService() {
        rev = new HashMap<UUID, String>();
        resolve = new HashMap<String, BtrpElement>();
    }

    public BtrpElement bind(BtrpOperand.Type t, UUID u, String n) {
        if (rev.containsKey(u)) {
            return null;
        }
        BtrpElement e = new BtrpElement(t, n, u);
        resolve.put(n, e);
        rev.put(u, n);
        return e;
    }

    public boolean unbind(UUID u) {
        String n = rev.remove(u);
        return n != null && resolve.remove(n) != null;
    }

    public BtrpElement resolve(String n) {
        return resolve.get(n);
    }
}
