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
import entropy.vjob.Among;
import entropy.vjob.Ban;
import entropy.vjob.PlacementConstraint;
import entropy.vjob.builder.DefaultVJobElementBuilder;
import entropy.vjob.builder.VJobElementBuilder;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for AmongBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestAmongBuilder {

    private static final VJobElementBuilder defaultEb = new DefaultVJobElementBuilder(new VirtualMachineTemplateFactoryStub());

    @DataProvider(name = "badAmongs")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"among(VM1,@N[1..10]);"},
                new String[]{"among(@N1,{@N[1..3],@N[4..6]});"},
                new String[]{"among({}, {@N[1..3],@N[4..6]});"},
                new String[]{"among({VM1},{@N[1..3],@N[4..6]},{VM2});"},
                new String[]{"among({VM1},{@N[1..3],{@N[4..6]}});"},
                new String[]{"among(VM[1..5],{@N[1..10], VM[6..10]});"},
                new String[]{"among(VM[1..6],{});"},
                new String[]{"among({},{});"},
        };
    }

    @Test(dataProvider = "badAmongs", expectedExceptions = {BtrpPlaceVJobBuilderException.class})
    public void testBadSignatures(String str) throws BtrpPlaceVJobBuilderException {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
            cfg.addOnline(new SimpleNode("N" + i, 50, 50, 50));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new AmongBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            b.build("namespace testAmongBuilder; VM[1..10] : tiny;\n" + str);
        } catch (BtrpPlaceVJobBuilderException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @DataProvider(name = "goodAmongs")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"among(VM1,{{@N1},{@N2}});", 1, 1, 1},
                new Object[]{"among({VM1},{@N[1..5],@N[6..10]});", 1, 5, 5},
                new Object[]{"among(VM[1..5],@N[1..10] / 2);", 5, 5, 5}
        };
    }

    @Test(dataProvider = "goodAmongs")
    public void testGoodSignatures(String str, int nbVMs, int nbNs1, int nbNs2) throws Exception {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
            cfg.addOnline(new SimpleNode("N" + i, 50, 50, 50));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new AmongBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        Among x = (Among) b.build("namespace testAmongBuilder; VM[1..10] : tiny;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getGroups().iterator().next().size(), nbNs1);
        Assert.assertEquals(x.getNodes().size(), nbNs1 + nbNs2);
        Assert.assertEquals(x.getAllVirtualMachines().size(), nbVMs);
    }
}
