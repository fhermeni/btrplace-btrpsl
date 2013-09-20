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

package btrplace.btrpsl.constraint;

import btrplace.btrpsl.ScriptBuilder;
import btrplace.btrpsl.ScriptBuilderException;
import btrplace.model.DefaultModel;
import btrplace.model.constraint.SingleRunningCapacity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link SingleRunningCapacityBuilder}.
 *
 * @author Fabien Hermenier
 */
@Test
public class SingleRunningCapacityBuilderTest {

    @DataProvider(name = "badSingleRunningCapacities")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"singleRunningCapacity({@N1,@N2},-1);"},
                new String[]{">>singleRunningCapacity({@N1,@N2},1.2);"},
                new String[]{"singleRunningCapacity({},5);"},
                new String[]{"singleRunningCapacity(@N[1,3,5]);"},
                new String[]{">>singleRunningCapacity(@N[1,3,5,15]);"},
                new String[]{"singleRunningCapacity(VM[1..3],3);"},
                new String[]{"singleRunningCapacity(5);"},
        };
    }

    @Test(dataProvider = "badSingleRunningCapacities", expectedExceptions = {ScriptBuilderException.class})
    public void testBadSignatures(String str) throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder(new DefaultModel());
        try {
            b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str);
        } catch (ScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodSingleRunningCapacities")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{">>singleRunningCapacity(@N1,3);", 1, 3},
                new Object[]{"singleRunningCapacity(@N[1..4],7);", 4, 7},
                new Object[]{">>singleRunningCapacity(@N[1..3],7-5%2);", 3, 6},
        };
    }

    @Test(dataProvider = "goodSingleRunningCapacities")
    public void testGoodSignatures(String str, int nbNodes, int capa) throws Exception {
        ScriptBuilder b = new ScriptBuilder(new DefaultModel());
        SingleRunningCapacity x = (SingleRunningCapacity) b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedNodes().size(), nbNodes);
        Assert.assertEquals(x.getAmount(), capa);
        Assert.assertEquals(x.isContinuous(), !str.startsWith(">>"));
    }
}
