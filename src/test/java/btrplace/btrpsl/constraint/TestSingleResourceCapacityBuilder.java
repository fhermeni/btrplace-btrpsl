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
import btrplace.model.constraint.SingleResourceCapacity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link SingleResourceCapacityBuilder}.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestSingleResourceCapacityBuilder {

    @DataProvider(name = "badSingleResources")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"singleResourceCapacity({@N1,@N2},\"foo\", -1);"},
                new String[]{"singleResourceCapacity({@N1,@N2},\"foo\", 1.7);"},
                new String[]{">>singleResourceCapacity({},\"foo\", 5);"},
                new String[]{"singleResourceCapacity(@N[1,3,5]);"},
                new String[]{">>singleResourceCapacity(\"foo\");"},
                new String[]{"singleResourceCapacity(VM[1..3],\"foo\", 3);"},
                new String[]{"singleResourceCapacity(5);"},
                new String[]{"singleResourceCapacity(\"bar\", \"foo\", 5);"},
        };
    }

    @Test(dataProvider = "badSingleResources", expectedExceptions = {ScriptBuilderException.class})
    public void testBadSignatures(String str) throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        try {
            b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str);
        } catch (ScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodSingleResources")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{">>singleResourceCapacity(@N1,\"foo\", 3);", 1, "foo", 3},
                new Object[]{"singleResourceCapacity(@N[1..4],\"foo\", 7);", 4, "foo", 7},
                new Object[]{">>singleResourceCapacity(@N[1..3],\"bar\", 7-5%2);", 3, "bar", 6},
        };
    }

    @Test(dataProvider = "goodSingleResources")
    public void testGoodSignatures(String str, int nbNodes, String rcId, int capa) throws Exception {
        ScriptBuilder b = new ScriptBuilder();
        SingleResourceCapacity x = (SingleResourceCapacity) b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedNodes().size(), nbNodes);
        Assert.assertEquals(x.getResource(), rcId);
        Assert.assertEquals(x.getAmount(), capa);
        Assert.assertEquals(x.isContinuous(), !str.startsWith(">>"));
    }
}
