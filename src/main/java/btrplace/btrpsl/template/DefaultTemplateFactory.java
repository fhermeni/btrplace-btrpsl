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
import btrplace.btrpsl.UUIDPool;
import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Fabien Hermenier
 */
public class DefaultTemplateFactory implements TemplateFactory {

    private Map<String, Template> vmTpls;

    private Map<String, Template> nodeTpls;

    private boolean strict;

    private UUIDPool uuidPool;

    private NamingService namingServer;

    public DefaultTemplateFactory(UUIDPool uuidPool, NamingService srv) {
        this(uuidPool, srv, false);
    }

    public boolean isStrict() {
        return strict;
    }

    public DefaultTemplateFactory(UUIDPool uuidPool, NamingService srv, boolean strict) {
        this.uuidPool = uuidPool;
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
        BtrpOperand.Type t;
        if (fqn.startsWith("@")) {
            t = BtrpOperand.Type.node;
        } else {
            t = BtrpOperand.Type.VM;
        }
        UUID u = uuidPool.request();
        if (u == null) {
            throw new ElementBuilderException("No UUID left");
        }
        BtrpElement el = namingServer.bind(t, u, fqn);
        for (Map.Entry<String, String> attr : attrs.entrySet()) {
            String value = "true";
            if (attr.getValue() != null) {
                value = attr.getValue();
            }
            scr.getAttributes().put(u, attr.getKey(), value);
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
