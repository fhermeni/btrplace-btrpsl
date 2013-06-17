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

import btrplace.json.JSONConverterException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link JSONScriptConverter}.
 *
 * @author Fabien Hermenier
 */
public class JSONScriptConverterTest {

    @Test
    public void testSimple() throws ScriptBuilderException, JSONConverterException {
        String script = "namespace clients.fhermeni.app1;\n" +
                "\n" +
                "@N[0x0..0xF] : defaultNodes;\n" +
                "VM[1..10] : micro<clone,start=8>;\n" +
                "VM[11..25] : small<start=12,stop=7>;\n\n" +
                "lonely(VM[1..7]);\n" +
                ">>spread(VM[3..8]);\n" +
                "$grps = VM[1..25] % 5;\n" +
                "for $g in $grps {\n" +
                "\tpreserve($g,\"cpu\",5);\n" +
                "}\n";
        //System.out.println(script);
        ScriptBuilder builder = new ScriptBuilder();
        Script s = builder.build(script);
        JSONScriptConverter conv = new JSONScriptConverter();
        JSONObject ob = conv.toJSON(s);
        System.out.println(ob.toString());

        Assert.assertEquals(ob.get("namespace"), "clients.fhermeni");
        Assert.assertEquals(ob.get("localName"), "app1");
        Assert.assertEquals(((JSONArray) ob.get("constraints")).size(), 7);
        Assert.assertEquals(((JSONArray) ob.get("vms")).size(), 25);
        Assert.assertEquals(((JSONArray) ob.get("nodes")).size(), 16);
    }
}
