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
import btrplace.model.constraint.Offline;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link btrplace.btrpsl.constraint.OfflineBuilder}.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestOfflineBuilder {

    @DataProvider(name = "badOfflines")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"offline({});"},
                new String[]{"offline({VM7});"},
                new String[]{"offline({@N[1..5]});"},
        };
    }

    @Test(dataProvider = "badOfflines", expectedExceptions = {BtrpScriptBuilderException.class})
    public void testBadSignatures(String str) throws BtrpScriptBuilderException {
        BtrpScriptBuilder b = new BtrpScriptBuilder();
        try {
            b.build("namespace testOfflineBuilder; VM[1..10] : tiny;\n@N[1..20] : defaultNode;" + str);
        } catch (BtrpScriptBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodOfflines")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"offline(@N1);", 1},
                new Object[]{"offline(@N[1..10]);", 10}
        };
    }

    @Test(dataProvider = "goodOfflines")
    public void testGoodSignatures(String str, int nbNodes) throws Exception {
        BtrpScriptBuilder b = new BtrpScriptBuilder();
        Offline x = (Offline) b.build("namespace testOfflineBuilder; VM[1..10] : tiny;\n@N[1..20] : defaultNode;" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getInvolvedNodes().size(), nbNodes);
    }
}
