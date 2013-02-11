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

    private Map<String, Template> tpls;

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
        tpls = new HashMap<String, Template>();
        this.strict = strict;
    }

    @Override
    public Set<String> getAvailables() {
        return tpls.keySet();
    }

    @Override
    public BtrpElement build(String tplName, String fqn, Map<String, String> attrs) throws ElementBuilderException {
        Template tpl = tpls.get(tplName);
        if (tpl == null) {
            if (strict) {
                throw new ElementBuilderException("Unknown template '" + tplName + "'");
            } else {
                return stubTemplate(tplName, fqn, attrs);
            }
        }
        return tpl.build(fqn, attrs);
    }

    private BtrpElement stubTemplate(String tplName, String fqn, Map<String, String> attrs) throws ElementBuilderException {
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
            el.addAttribute(attr.getKey(), value);
        }
        el.setTemplate(tplName);
        return el;
    }

    @Override
    public Template register(Template tpl) {
        return tpls.put(tpl.getIdentifier(), tpl);
    }
}
