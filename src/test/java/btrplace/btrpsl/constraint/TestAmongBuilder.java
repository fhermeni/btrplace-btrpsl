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

package btrplace.btrpsl.constraint;

import btrplace.btrpsl.BtrpScriptBuilder;
import btrplace.btrpsl.BtrpScriptBuilderException;
import btrplace.model.constraint.Among;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for AmongBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestAmongBuilder {

    @DataProvider(name = "badAmongs")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"among(VM1,@N[1..10]);"},
                new String[]{"among(@N1,{@N[1..3],@N[4..6]});"},
                new String[]{"among({}, {@N[1..3],@N[4..6]});"},
                new String[]{"among({VM1},{@N[1..3],@N[4..6]},{VM2});"},
                new String[]{"among({VM1},{@N[1..3],{@N[4..6]}});"},
                new String[]{"among(VM[1..5],{@N[1..10], VM[6..10]});"},
                new String[]{"among(VM[1..6],{});"},
                new String[]{"among({},{});"},
        };
    }

    @Test(dataProvider = "badAmongs", expectedExceptions = {BtrpScriptBuilderException.class})
    public void testBadSignatures(String str) throws BtrpScriptBuilderException {
        BtrpScriptBuilder b = new BtrpScriptBuilder();
        try {
            b.build("namespace testAmongBuilder; VM[1..10] : tiny;\n" + str);
        } catch (BtrpScriptBuilderException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodAmongs")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"among(VM1,{{@N1},{@N2}});", 1, 1, 1},
                new Object[]{"among({VM1},{@N[1..5],@N[6..10]});", 1, 5, 5},
                new Object[]{"among(VM[1..5],@N[1..10] / 2);", 5, 5, 5}
        };
    }

    @Test(dataProvider = "goodAmongs")
    public void testGoodSignatures(String str, int nbVMs, int nbNs1, int nbNs2) throws Exception {
        BtrpScriptBuilder b = new BtrpScriptBuilder();
        Among x = (Among) b.build("namespace testAmongBuilder; VM[1..10] : tiny;\n@N[1..10] : defaultNode;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getGroupsOfNodes().iterator().next().size(), nbNs1);
        Assert.assertEquals(x.getInvolvedNodes().size(), nbNs1 + nbNs2);
        Assert.assertEquals(x.getInvolvedVMs().size(), nbVMs);
    }
}
