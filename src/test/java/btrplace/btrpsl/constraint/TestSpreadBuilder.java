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
import btrplace.model.constraint.Spread;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link SpreadBuilder}.
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

    @Test(dataProvider = "badContinuousSpreads", expectedExceptions = {ScriptBuilderException.class})
    public void testBadSignatures(String str) throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        try {
            b.build("namespace test; VM[1..10] : tiny;\n" + str);
        } catch (ScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodContinuousSpreads")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{">>spread({VM1});", 1, false},
                new Object[]{"spread(VM1);", 1, true},
                new Object[]{">>spread(VM[1..5]);", 5, false},
        };
    }

    @Test(dataProvider = "goodContinuousSpreads")
    public void testGoodSignatures(String str, int nbVMs, boolean c) throws Exception {
        ScriptBuilder b = new ScriptBuilder();
        Spread x = (Spread) b.build("namespace test; VM[1..10] : tiny;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedVMs().size(), nbVMs);
        Assert.assertEquals(x.isContinuous(), c);
    }
}
