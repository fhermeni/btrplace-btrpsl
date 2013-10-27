/*
 * Copyright (c) 2013 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
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
import btrplace.model.Element;
import btrplace.model.Node;
import btrplace.model.VM;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic non-persistent implementation of a {@link NamingService}.
 *
 * @author Fabien Hermenier
 */
public class InMemoryNamingService extends NamingService {

    private Map<String, BtrpElement> resolve;

    private Map<Element, String> rev;

    /**
     * Make a new service.
     */
    public InMemoryNamingService() {
        resolve = new HashMap<>();
        rev = new HashMap<>();
    }

    @Override
    public BtrpElement register(String id, Element e) throws NamingServiceException {
        if (resolve.containsKey(id)) {
            throw new NamingServiceException(id, " Name already registered");
        }

        BtrpElement be;
        //Naming consistency
        if (e instanceof Node) {
            if (!id.startsWith("@")) {
                throw new NamingServiceException(id, "Node labels must start with a '@'");
            }
            be = new BtrpElement(BtrpOperand.Type.node, id, e);
        } else if (e instanceof VM) {
            be = new BtrpElement(BtrpOperand.Type.VM, id, e);
        } else {
            throw new NamingServiceException(id, "Unsupported type of element " + e.getClass().getSimpleName());
        }
        resolve.put(id, be);
        rev.put(e, id);
        return be;
    }

    /*@Override
    public BtrpElement declare(String n) throws NamingServiceException {
        if (resolve.containsKey(n)) {
            throw new NamingServiceException(n, " Name already registered");
        }

        BtrpElement be;
        if (n.startsWith("@")) {
            Node e = getModel().newNode();
            if (e == null) {
                throw new NamingServiceException(n, " No UUID left");
            }
            be = register(n, e);
            // By default, the node will be offline
            getModel().getMapping().addOfflineNode(e);

        } else {
            VM e = getModel().newVM();
            if (e == null) {
                throw new NamingServiceException(n, " No UUID left");
            }
            be = register(n, e);
            //By default, the VM is set to the ready state
            getModel().getMapping().addReadyVM((VM) e);
        }
        return be;
    }         */

    @Override
    public String resolve(Element el) {
        return rev.get(el);
    }

    @Override
    public BtrpElement resolve(String n) {
        return resolve.get(n);
    }

    @Override
    public InMemoryNamingService clone() {
        InMemoryNamingService cpy = new InMemoryNamingService();
        for (Map.Entry<String, BtrpElement> e : resolve.entrySet()) {
            cpy.resolve.put(e.getKey(), e.getValue());
        }
        for (Map.Entry<Element, String> e : rev.entrySet()) {
            cpy.rev.put(e.getKey(), e.getValue());
        }
        return cpy;
    }

    @Override
    public boolean substituteVM(VM curId, VM nextId) {
        String fqn = rev.get(curId);
        if (fqn != null) {
            rev.put(nextId, fqn);
            resolve.put(fqn, new BtrpElement(BtrpOperand.Type.VM, fqn, nextId));
        }
        throw new UnsupportedOperationException();
    }

}
