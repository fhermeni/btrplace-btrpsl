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
import entropy.vjob.builder.DefaultVJobElementBuilder;
import entropy.vjob.builder.VJobElementBuilder;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for Ban.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestBanBuilder {

    public void testGoodBuild() {
        BanBuilder b = new BanBuilder();

        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        s2.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        params.add(s2);
        Ban c = b.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getVirtualMachines().size(), 1);
        Assert.assertEquals(c.getNodes().size(), 1);
    }

    public void testWithSingleElements() {
        BanBuilder b = new BanBuilder();

        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        params.add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        Ban c = b.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getVirtualMachines().size(), 1);
        Assert.assertEquals(c.getNodes().size(), 1);
    }

    public void testWithBadParamsNumber() {
        BanBuilder b = new BanBuilder();

        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        params.add(s1);
        Assert.assertNull(b.buildConstraint(new MockBtrPlaceTree(), params));
    }

    public void testWithEmptyVMSet() {
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BanBuilder b = new BanBuilder();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        s2.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        params.add(s2);
        Assert.assertNull(b.buildConstraint(new MockBtrPlaceTree(), params));
    }

    public void testWithEmptyNodeset() {
        BanBuilder b = new BanBuilder();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(s1);
        params.add(s2);
        Assert.assertNull(b.buildConstraint(new MockBtrPlaceTree(), params));
    }

    public void testWithTypeMismatch() {
        BanBuilder b = new BanBuilder();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(s1);
        params.add(s1);
        Assert.assertNull(b.buildConstraint(new MockBtrPlaceTree(), params));
    }

    private static final VJobElementBuilder defaultEb = new DefaultVJobElementBuilder(new VirtualMachineTemplateFactoryStub());

    @DataProvider(name = "badBans")
    public Object[][] getBadSignatures() {
        return new String[][]{
                new String[]{"ban(@N1,@N[1..10]);"},
                new String[]{"ban({VM1},VM[1..5]);"},
                new String[]{"ban({VM1},@N[1..10],@N1);"},
                new String[]{"ban({VM1},@N[1..10],VM1);"},
                new String[]{"ban({VM1},@N[1..10],@N1);"},
                new String[]{"ban({VM1},{@N[1..5], @N[6..10]});"},
                new String[]{"ban({},@N[1..5]);"},
                new String[]{"ban(VM1,{});"},
                new String[]{"ban({},{});"},
        };
    }

    @Test(dataProvider = "badBans", expectedExceptions = {BtrpPlaceVJobBuilderException.class})
    public void testBadSignatures(String str) throws BtrpPlaceVJobBuilderException {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
            cfg.addOnline(new SimpleNode("N" + i, 50, 50, 50));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new BanBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        b.build("namespace foo; VM[1..10] : tiny;\n" + str);
    }

    @DataProvider(name = "goodBans")
    public Object[][] getGoodSignatures() {
        return new Object[][]{
                new Object[]{"ban(VM1,{@N1});", 1, 1},
                new Object[]{"ban({VM1},{@N1});", 1, 1},
                new Object[]{"ban(VM1,@N[1..10]);", 1, 10},
                new Object[]{"ban({VM1,VM2},@N[1..10]);", 2, 10},
        };
    }

    @Test(dataProvider = "goodBans")
    public void testGoodSignatures(String str, int nbVMs, int nbNodes) throws Exception {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
            cfg.addOnline(new SimpleNode("N" + i, 50, 50, 50));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new BanBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        Ban x = (Ban) b.build("namespace foo; VM[1..10] : tiny;\n" + str).getConstraints().iterator().next();
        Assert.assertEquals(x.getNodes().size(), nbNodes);
        Assert.assertEquals(x.getVirtualMachines().size(), nbVMs);
    }
}
