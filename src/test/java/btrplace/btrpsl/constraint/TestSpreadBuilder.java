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
import btrplace.model.constraint.Spread;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for SpreadBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestSpreadBuilder {

    @DataProvider(name = "badContinuousSpreads")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"spread({VM1,VM2},{VM3});"},
                new String[]{"spread({});"},
                new String[]{"spread(@N[1..10]);"},
                new String[]{"spread(VMa);"},
                new String[]{"spread();"},
        };
    }

    @Test(dataProvider = "badContinuousSpreads", expectedExceptions = {BtrpScriptBuilderException.class})
    public void testBadSignatures(String str) throws BtrpScriptBuilderException {
        BtrpScriptBuilder b = new BtrpScriptBuilder();
        try {
            b.build("namespace testContinuousSpreadBuilder; VM[1..10] : tiny;\n" + str);
        } catch (BtrpScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodContinuousSpreads")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"spread({VM1});", 1},
                new Object[]{"spread(VM1);", 1},
                new Object[]{"spread(VM[1..5]);", 5},
        };
    }

    @Test(dataProvider = "goodContinuousSpreads")
    public void testGoodSignatures(String str, int nbVMs) throws Exception {
        BtrpScriptBuilder b = new BtrpScriptBuilder();
        Spread x = (Spread) b.build("namespace testContinuousSpreadBuilder; VM[1..10] : tiny;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedVMs().size(), nbVMs);
    }
}
