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

package btrplace.btrpsl.template;

import btrplace.btrpsl.NamingService;
import btrplace.btrpsl.Script;
import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Fabien Hermenier
 */
public class DefaultTemplateFactory implements TemplateFactory {

    private Map<String, Template> vmTpls;

    private Map<String, Template> nodeTpls;

    private boolean strict;

    private NamingService namingServer;

    public DefaultTemplateFactory(NamingService srv) {
        this(srv, false);
    }

    public boolean isStrict() {
        return strict;
    }

    public DefaultTemplateFactory(NamingService srv, boolean strict) {
        this.namingServer = srv;
        vmTpls = new HashMap<String, Template>();
        nodeTpls = new HashMap<String, Template>();
        this.strict = strict;
    }

    @Override
    public Set<String> getAvailables() {
        return vmTpls.keySet();
    }

    @Override
    public BtrpElement build(Script scr, String tplName, String fqn, Map<String, String> attrs) throws ElementBuilderException {
        Template tpl = null;
        BtrpOperand.Type t;
        if (fqn.startsWith("@")) {
            tpl = nodeTpls.get(tplName);
            t = BtrpOperand.Type.node;
        } else {
            tpl = vmTpls.get(tplName);
            t = BtrpOperand.Type.VM;
        }
        if (tpl == null) {
            if (strict) {
                throw new ElementBuilderException("Unknown " + t + " template '" + tplName + "'");
            } else {
                return stubTemplate(scr, tplName, fqn, attrs);
            }
        }
        return tpl.build(scr, fqn, attrs);
    }

    private BtrpElement stubTemplate(Script scr, String tplName, String fqn, Map<String, String> attrs) throws ElementBuilderException {
        BtrpElement el = namingServer.register(fqn);
        for (Map.Entry<String, String> attr : attrs.entrySet()) {
            String value = "true";
            if (attr.getValue() != null) {
                value = attr.getValue();
            }
            scr.getAttributes().put(el.getUUID(), attr.getKey(), value);
        }
        el.setTemplate(tplName);
        return el;
    }

    @Override
    public Template register(Template tpl) {
        if (tpl.getElementType() == BtrpOperand.Type.VM) {
            return vmTpls.put(tpl.getIdentifier(), tpl);
        } else if (tpl.getElementType() == BtrpOperand.Type.node) {
            return nodeTpls.put(tpl.getIdentifier(), tpl);
        }
        return null;
    }
}
