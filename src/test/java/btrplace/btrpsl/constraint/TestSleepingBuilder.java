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
import btrplace.model.constraint.Sleeping;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link SleepingBuilder}.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestSleepingBuilder {

    @DataProvider(name = "badsleepings")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"sleeping({});"},
                new String[]{"sleeping({@N1});"},
                new String[]{"sleeping({VM[1..5]});"},
        };
    }

    @Test(dataProvider = "badsleepings", expectedExceptions = {ScriptBuilderException.class})
    public void testBadSignatures(String str) throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        try {
            b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;" + str);
        } catch (ScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodsleepings")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"sleeping(VM1);", 1},
                new Object[]{">>sleeping(VM[1..10]);", 10}
        };
    }

    @Test(dataProvider = "goodsleepings")
    public void testGoodSignatures(String str, int nbNodes) throws Exception {
        ScriptBuilder b = new ScriptBuilder();
        Sleeping x = (Sleeping) b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedVMs().size(), nbNodes);
        Assert.assertEquals(x.isContinuous(), false);
    }
}
