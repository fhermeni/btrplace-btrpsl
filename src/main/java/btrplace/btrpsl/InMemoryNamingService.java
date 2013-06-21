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
import btrplace.model.Model;
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
     * Make a new service
     *
     * @param p the model to rely on
     */
    public InMemoryNamingService(Model p) {
        super(p);
        resolve = new HashMap<>();
        rev = new HashMap<>();
    }

    @Override
    public BtrpElement register(String n) throws NamingServiceException {
        if (resolve.containsKey(n)) {
            throw new NamingServiceException(n, " Name already registered");
        }

        BtrpElement be;
        Element e;
        BtrpOperand.Type t;
        if (n.startsWith("@")) {
            t = BtrpOperand.Type.node;
            e = getModel().newNode();

        } else {
            t = BtrpOperand.Type.VM;
            e = getModel().newVM();
        }

        if (e == null) {
            throw new NamingServiceException(n, " No UUID left");
        }

        be = new BtrpElement(t, n, e);
        resolve.put(n, be);
        rev.put(e, n);
        return be;
    }

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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean substituteVM(VM curId, VM nextId) {
        throw new UnsupportedOperationException();
    }
}
