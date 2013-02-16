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
 * Basic non-persistent implementation of a {@link NamingService}.
 *
 * @author Fabien Hermenier
 */
public class InMemoryNamingService implements NamingService {

    private Map<String, BtrpElement> resolve;

    private UUIDPool uuidPool;

    /**
     * Make a new service
     *
     * @param p the pool of UUIDs to rely on
     */
    public InMemoryNamingService(UUIDPool p) {
        resolve = new HashMap<String, BtrpElement>();
        uuidPool = p;

    }

    @Override
    public BtrpElement register(String n) throws NamingServiceException {
        if (resolve.containsKey(n)) {
            throw new NamingServiceException(n, " Name already registered");
        }
        UUID u = uuidPool.request();
        if (u == null) {
            throw new NamingServiceException(n, " No UUID left");
        }

        BtrpOperand.Type t;
        if (n.startsWith("@")) {
            t = BtrpOperand.Type.node;
        } else {
            t = BtrpOperand.Type.VM;
        }
        BtrpElement e = new BtrpElement(t, n, u);
        resolve.put(n, e);
        return e;
    }

    @Override
    public boolean release(BtrpElement e) {
        if (resolve.remove(e.getName()) != null) {
            uuidPool.release(e.getUUID());
            return true;
        }
        return false;
    }

    @Override
    public BtrpElement resolve(String n) {
        return resolve.get(n);
    }
}
