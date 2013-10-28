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

import btrplace.json.JSONConverterException;
import btrplace.json.model.view.ModelViewConverter;
import btrplace.model.Element;
import btrplace.model.Node;
import btrplace.model.VM;
import net.minidev.json.JSONObject;

import java.util.Map;

/**
 * Converter to serialize/un-serialize {@link NamingService}.
 *
 * @author Fabien Hermenier
 */
public class InMemoryNamingServiceConverter extends ModelViewConverter<NamingService> {

    @Override
    public Class<NamingService> getSupportedView() {
        return NamingService.class;
    }

    @Override
    public String getJSONId() {
        return NamingService.ID;
    }

    @Override
    public NamingService fromJSON(JSONObject in) throws JSONConverterException {
        if (!in.containsKey("id") || !in.get("id").equals(NamingService.ID)) {
            throw new JSONConverterException("Missing or incorrect value for attribute 'id'. Expecting '" + NamingService.ID);
        }
        InMemoryNamingService ns = new InMemoryNamingService();
        for (Map.Entry<String, Object> e : in.entrySet()) {
            String n = e.getKey();
            if (n.equals("id")) {
                continue;
            }
            try {
                if (n.startsWith("@")) {
                    Node node = getOrMakeNode(Integer.parseInt(e.getValue().toString()));
                    ns.register(e.getKey(), node);
                } else {
                    VM vm = getOrMakeVM(Integer.parseInt(e.getValue().toString()));
                    ns.register(e.getKey(), vm);
                }
            } catch (NamingServiceException ex) {
                throw new JSONConverterException(ex.getMessage());
            }
        }
        return ns;
    }

    @Override
    public JSONObject toJSON(NamingService ns) throws JSONConverterException {
        JSONObject res = new JSONObject();
        res.put("id", getJSONId());
        for (Element e : ns.getRegisteredElements()) {
            String s = ns.resolve(e);
            res.put(s, e.id());
        }
        return res;
    }
}
