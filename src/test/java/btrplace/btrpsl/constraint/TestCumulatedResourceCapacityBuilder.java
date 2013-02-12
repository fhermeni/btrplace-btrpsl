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
import btrplace.model.constraint.CumulatedResourceCapacity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link CumulatedResourceCapacityBuilder}.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestCumulatedResourceCapacityBuilder {

    @DataProvider(name = "badCumulatedResources")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"cumulatedResourceCapacity({@N1,@N2},\"foo\", -1);"},
                new String[]{"cumulatedResourceCapacity({},\"foo\", 5);"},
                new String[]{"cumulatedResourceCapacity(@N[1,3,5]);"},
                new String[]{"cumulatedResourceCapacity(\"foo\");"},
                new String[]{"cumulatedResourceCapacity(VM[1..3],\"foo\", 3);"},
                new String[]{"cumulatedResourceCapacity(@N[1..3],\"foo\", 3.2);"},
                new String[]{"cumulatedResourceCapacity(5);"},
                new String[]{"cumulatedResourceCapacity(\"bar\", \"foo\", 5);"},
        };
    }

    @Test(dataProvider = "badCumulatedResources", expectedExceptions = {ScriptBuilderException.class})
    public void testBadSignatures(String str) throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        try {
            b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str);
        } catch (ScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodCumulatedResources")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"cumulatedResourceCapacity(@N1,\"foo\", 3);", 1, "foo", 3},
                new Object[]{"cumulatedResourceCapacity(@N[1..4],\"foo\", 7);", 4, "foo", 7},
                new Object[]{"cumulatedResourceCapacity(@N[1..3],\"bar\", 7-5%2);", 3, "bar", 6},
        };
    }

    @Test(dataProvider = "goodCumulatedResources")
    public void testGoodSignatures(String str, int nbNodes, String rcId, int capa) throws Exception {
        ScriptBuilder b = new ScriptBuilder();
        CumulatedResourceCapacity x = (CumulatedResourceCapacity) b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedNodes().size(), nbNodes);
        Assert.assertEquals(x.getResource(), rcId);
        Assert.assertEquals(x.getAmount(), capa);
    }
}
