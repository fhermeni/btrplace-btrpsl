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

package btrplace.btrpsl.template;

import btrplace.btrpsl.Script;
import btrplace.btrpsl.element.BtrpElement;

import java.util.Map;
import java.util.Set;

/**
 * A factory of templates.
 * Allow to register several templates and to build element from the template name
 * and the element identifier.
 *
 * @author Fabien Hermenier
 */
public interface TemplateFactory {

    /**
     * Get the available templates.
     *
     * @return a set of templates identifier that may be empty
     */
    Set<String> getAvailables();

    /**
     * Build an element.
     *
     * @param scr     the script the element belongs to.
     * @param tplName the template name for the element
     * @param fqn     the element identifier
     * @param attrs   the attributes related to the element.
     * @return the builded element if succeed
     * @throws ElementBuilderException if an error occurred
     */
    BtrpElement build(Script scr, String tplName, String fqn, Map<String, String> attrs) throws ElementBuilderException;

    /**
     * Register a template.
     *
     * @param tpl the template to register
     * @return the template that was previously registered in place of {@code tpl}.
     */
    Template register(Template tpl);
}
