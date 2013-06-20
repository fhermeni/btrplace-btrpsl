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

import btrplace.btrpsl.InMemoryNamingService;
import btrplace.btrpsl.ScriptBuilder;
import btrplace.btrpsl.ScriptBuilderException;
import btrplace.model.DefaultModel;
import btrplace.model.constraint.Killed;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link Killed}.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestKilledBuilder {

    @DataProvider(name = "badKilleds")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"killed({});"},
                new String[]{"killed({@N1});"},
                new String[]{"killed({VM[1..5]});"},
        };
    }

    @Test(dataProvider = "badKilleds", expectedExceptions = {ScriptBuilderException.class})
    public void testBadSignatures(String str) throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder(new InMemoryNamingService(new DefaultModel()));
        try {
            b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;" + str);
        } catch (ScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodKilleds")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{">>killed(VM1);", 1},
                new Object[]{"killed(VM[1..10]);", 10}
        };
    }

    @Test(dataProvider = "goodKilleds")
    public void testGoodSignatures(String str, int nbNodes) throws Exception {
        ScriptBuilder b = new ScriptBuilder(new InMemoryNamingService(new DefaultModel()));
        Killed x = (Killed) b.build("namespace test; VM[1..10] : tiny;\n@N[1..20] : defaultNode;" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedVMs().size(), nbNodes);
        Assert.assertEquals(x.isContinuous(), false);
    }
}
