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
import btrplace.model.constraint.Overbook;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link OverbookBuilder}.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestOverbookBuilder {

    @DataProvider(name = "badOverbooks")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"overbook({@N1,@N2},-1);"},
                new String[]{"overbook(\"foo\",-1);"},
                new String[]{"overbook({},5);"},
                new String[]{"overbook(@N[1,3,5]);"},
                new String[]{"overbook(@N[1,3,5,15],\"foo\");"},
                new String[]{"overbook(5);"},
        };
    }

    @Test(dataProvider = "badOverbooks", expectedExceptions = {ScriptBuilderException.class})
    public void testBadSignatures(String str) throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        try {
            b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str);
        } catch (ScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodOverbooks")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"overbook(@N1,\"foo\",3);", 1, "foo", 3},
                new Object[]{"overbook(@N[1..4],\"bar\", 7.5);", 4, "bar", 7.5},
        };
    }

    @Test(dataProvider = "goodOverbooks")
    public void testGoodSignatures(String str, int nbNodes, String rcId, double ratio) throws Exception {
        ScriptBuilder b = new ScriptBuilder();
        Overbook x = (Overbook) b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedNodes().size(), nbNodes);
        Assert.assertEquals(x.getResource(), rcId);
        Assert.assertEquals(x.getRatio(), ratio);
    }
}
