/*
 * Copyright (c) Fabien Hermenier
 *
 *        This file is part of Entropy.
 *
 *        Entropy is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU Lesser General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *
 *        Entropy is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU Lesser General Public License for more details.
 *
 *        You should have received a copy of the GNU Lesser General Public License
 *        along with Entropy.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrpsl.constraint;

import btrpsl.BtrPlaceVJobBuilder;
import btrpsl.BtrpPlaceVJobBuilderException;
import btrpsl.element.BtrpNode;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.BtrpVirtualMachine;
import btrpsl.template.VirtualMachineTemplateFactoryStub;
import entropy.configuration.Configuration;
import entropy.configuration.SimpleConfiguration;
import entropy.configuration.SimpleNode;
import entropy.configuration.SimpleVirtualMachine;
import entropy.vjob.Ban;
import entropy.vjob.LazySplit;
import entropy.vjob.Split;
import entropy.vjob.builder.DefaultVJobElementBuilder;
import entropy.vjob.builder.VJobElementBuilder;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for LazySplitBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestSplitBuilder {

    private static final VJobElementBuilder defaultEb = new DefaultVJobElementBuilder(new VirtualMachineTemplateFactoryStub());

    @DataProvider(name = "badSplits")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"split({VM1},{VM2},{VM3});"},
                new String[]{"split({VM1},{});"},
                new String[]{"split({},{VM1});"},
                new String[]{"split(@N[1..5],@VM[1..5]);"},
                new String[]{"split(VM[1..5],@N[1..5]);"},
                new String[]{"split({VM[1..5]},{VM1});"},
                new String[]{"split(VM[1..5],{{VM1}});"},
        };
    }

    @Test(dataProvider = "badSplits", expectedExceptions = {BtrpPlaceVJobBuilderException.class})
    public void testBadSignatures(String str) throws BtrpPlaceVJobBuilderException {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
            cfg.addOnline(new SimpleNode("N" + i, 50, 50, 50));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new LazySplitBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            b.build("namespace testSplitBuilder; VM[1..10] : tiny;\n" + str);
        } catch (BtrpPlaceVJobBuilderException ex) {
            System.out.println(str + " " + ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodSplits")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"split(VM1,VM2);", 1, 1},
                new Object[]{"split({VM1},VM2);", 1, 1},
                new Object[]{"split(VM[1..5] - {VM2},{VM2});", 4, 1},
        };
    }

    @Test(dataProvider = "goodSplits")
    public void testGoodSignatures(String str, int nbVMs1, int nbVMs2) throws Exception {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
            cfg.addOnline(new SimpleNode("N" + i, 50, 50, 50));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new LazySplitBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        LazySplit x = (LazySplit) b.build("namespace testSplitBuilder; VM[1..10] : tiny;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getFirstSet().size(), nbVMs1);
        Assert.assertEquals(x.getSecondSet().size(), nbVMs2);
        Assert.assertEquals(x.getAllVirtualMachines().size(), nbVMs2 + nbVMs1);
    }
}
