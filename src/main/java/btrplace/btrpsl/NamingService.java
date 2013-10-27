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
import btrplace.model.Element;
import btrplace.model.view.ModelView;

/**
 * A service to declare VMs and track their fully-qualified name
 *
 * @author Fabien Hermenier
 */
public abstract class NamingService implements ModelView {

    static final String ID = "btrpsl.ns";

    static final String NAMESPACE_ATTRIBUTE = "btrpsl.ns";

    static final String ID_ATTRIBUTE = "btrpsl.id";

    /**
     * Declare an element.
     *
     * @param id the element identifier. Starts with a {@code \@} to indicate
     *           a node. Otherwise, the element will be considered as a virtual machine
     * @return the registered element if the operation succeed. {@code null} otherwise
     */
    //public abstract BtrpElement declare(String id) throws NamingServiceException;
    public abstract BtrpElement register(String id, Element e) throws NamingServiceException;

    /**
     * Synchronise the NamingService with the model attributes.
     * First, all the registered elements presents in the model
     * have their attributes updated.
     * Then, the reverse
     */
    /*public void sync(Model m) throws NamingServiceException {
        Attributes attrs = m.getAttributes();
        for (Node n : m.getMapping().getAllNodes()) {
            String id = attrs.getString(n, NamingService.ID_ATTRIBUTE);
            if (id != null) {
                register(id, n);
            }
        }
        for (VM v : m.getMapping().getAllVMs()) {
            String id = attrs.getString(v, NamingService.ID_ATTRIBUTE);
            String ns = attrs.getString(v, NamingService.NAMESPACE_ATTRIBUTE);
            if (id != null && ns != null) {
                register(ns + "." + id, v);
            }
        }
    }       */

    /**
     * Get the element associated to a given identifier.
     *
     * @param n the element identifier
     * @return the matching element if any, {@code null} otherwise
     */
    public abstract BtrpElement resolve(String n);

    @Override
    public String getIdentifier() {
        return ID;
    }

    /**
     * Clone the service.
     *
     * @return a new wservice
     */
    public abstract NamingService clone();

    /**
     * Get the fully qualified name of a given model element.
     *
     * @param el the element
     * @return a String if the name can be resolved
     */
    public abstract String resolve(Element el);
}
