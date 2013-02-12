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

import btrplace.btrpsl.ScriptBuilder;
import btrplace.btrpsl.ScriptBuilderException;
import btrplace.model.constraint.Fence;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for FenceBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestFenceBuilder {

    @DataProvider(name = "badFences")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"fence(@N1,@N[1..10]);"},
                new String[]{"fence({@N1},@N[1..10]);"},
                new String[]{"fence({VM1},VM[1..5]);"},
                new String[]{"fence({VM1},@N[1..10],@N1);"},
                new String[]{"fence({VM1},@N[1..10],VM1);"},
                new String[]{"fence({VM1},@N[1..10],@N1);"},
                new String[]{"fence({VM1},{@N[1..5], @N[6..10]});"},
                new String[]{"fence({},@N[1..5]);"},
                new String[]{"fence(VM1,{});"},
                new String[]{"fence({},{});"},
        };
    }

    @Test(dataProvider = "badFences", expectedExceptions = {ScriptBuilderException.class})
    public void testBadSignatures(String str) throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        try {
            b.build("namespace testFenceBuilder; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str);
        } catch (ScriptBuilderException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodFences")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"fence(VM1,{@N1});", 1, 1},
                new Object[]{"fence({VM1},{@N1});", 1, 1},
                new Object[]{"fence(VM1,@N[1..10]);", 1, 10},
                new Object[]{"fence({VM1,VM2},@N[1..10]);", 2, 10},
        };
    }

    @Test(dataProvider = "goodFences")
    public void testGoodSignatures(String str, int nbVMs, int nbNodes) throws Exception {
        ScriptBuilder b = new ScriptBuilder();
        Fence x = (Fence) b.build("namespace testFenceBuilder; VM[1..10] : tiny;\n @N[1..20] : defaultNode;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedNodes().size(), nbNodes);
        Assert.assertEquals(x.getInvolvedVMs().size(), nbVMs);
    }
}
