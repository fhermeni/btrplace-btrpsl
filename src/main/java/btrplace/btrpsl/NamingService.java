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
import btrplace.model.Model;
import btrplace.model.view.ModelView;

/**
 * A service to declare VMs and track their fully-qualified name
 *
 * @author Fabien Hermenier
 */
public abstract class NamingService implements ModelView {

    public static final String ID = "btrpsl.ns";

    private Model model;

    /**
     * Make a new service.
     *
     * @param mo the model to associate to the service
     */
    public NamingService(Model mo) {
        model = mo;
    }

    /**
     * Register an element.
     *
     * @param id the element identifier. Starts with a {@code \@} to indicate
     *           a node. Otherwise, the element will be considered as a virtual machine
     * @return the registered element if the operation succeed. {@code null} otherwise
     */
    public abstract BtrpElement register(String id) throws NamingServiceException;

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
     * @return a new service
     */
    public abstract NamingService clone();

    /**
     * Get the fully qualified name of a given model element.
     *
     * @param el the element
     * @return a String if the name can be resolved
     */
    public abstract String resolve(Element el);

    /**
     * Get the underlying model.
     *
     * @return the model
     */
    public Model getModel() {
        return model;
    }

}
