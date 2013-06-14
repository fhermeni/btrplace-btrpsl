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
import btrplace.json.AbstractJSONObjectConverter;
import btrplace.json.JSONConverterException;
import btrplace.json.model.AttributesConverter;
import btrplace.json.model.constraint.SatConstraintsConverter;
import btrplace.model.constraint.SatConstraint;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.Set;

/**
 * An object to serialize Script using the JSON format.
 *
 * @author Fabien Hermenier
 */
public class JSONScriptConverter extends AbstractJSONObjectConverter<Script> {

    /**
     * Unsupported
     */
    @Override
    public Script fromJSON(JSONObject in) throws JSONConverterException {
        throw new UnsupportedOperationException();
    }

    @Override
    public JSONObject toJSON(Script script) throws JSONConverterException {
        JSONObject ob = new JSONObject();
        ob.put("namespace", script.getNamespace());
        ob.put("localName", script.getlocalName());

        AttributesConverter attrsConv = new AttributesConverter();
        SatConstraintsConverter cstrsConv = new SatConstraintsConverter();

        //The declared VMs and nodes
        ob.put("vms", writeElements(script.getVMs()));
        ob.put("nodes", writeElements(script.getNodes()));

        //The element attributes
        ob.put("attributes", attrsConv.toJSON(script.getAttributes()));

        //The constraints
        JSONArray cstrs = new JSONArray();
        ob.put("constraints", cstrs);
        for (SatConstraint cstr : script.getConstraints()) {
            cstrs.add(cstrsConv.toJSON(cstr));
        }

        return ob;
    }

    private JSONArray writeElements(Set<BtrpElement> elems) {
        JSONArray els = new JSONArray();
        for (BtrpElement bel : elems) {
            JSONObject jel = new JSONObject();
            jel.put("id", bel.getElement().id());
            jel.put("name", bel.getName());
            els.add(jel);
        }
        return els;
    }

}
