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

import btrpsl.element.BtrpNode;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.BtrpVirtualMachine;
import entropy.configuration.SimpleNode;
import entropy.configuration.SimpleVirtualMachine;
import entropy.vjob.Fence;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for FenceBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestFenceBuilder {

    /**
     * Test fence({vm1,vm2},{n2,n3}}
     */
    public void testValid() {
        FenceBuilder mb = new FenceBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        s2.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        s2.getValues().add(new BtrpNode(new SimpleNode("N3", 1, 1, 1)));
        params.add(s1);
        params.add(s2);
        Fence f = mb.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(f);
        Assert.assertEquals(f.getAllVirtualMachines().size(), 2);
        Assert.assertEquals(f.getVirtualMachines(), f.getAllVirtualMachines());
        Assert.assertEquals(f.getNodes().size(), 2);
    }

    public void testWithSingleElements() {
        FenceBuilder mb = new FenceBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        params.add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        Fence f = mb.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(f);
        Assert.assertEquals(f.getAllVirtualMachines().size(), 1);
        Assert.assertEquals(f.getVirtualMachines(), f.getAllVirtualMachines());
        Assert.assertEquals(f.getNodes().size(), 1);
    }

    /**
     * Test fence(vmset).
     */
    public void testBadParamsNumber() {
        FenceBuilder mb = new FenceBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        params.add(s1);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test fence(pset, pset).
     */
    public void testTypeMismatch() {
        FenceBuilder mb = new FenceBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        s1.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        params.add(s1);

        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        s2.getValues().add(new BtrpNode(new SimpleNode("N3", 1, 1, 1)));
        s2.getValues().add(new BtrpNode(new SimpleNode("N4", 1, 1, 1)));
        params.add(s2);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test fence({}, nodeset).
     */
    public void testEmptyVMSet() {
        FenceBuilder mb = new FenceBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        params.add(s1);

        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        s2.getValues().add(new BtrpNode(new SimpleNode("N3", 1, 1, 1)));
        s2.getValues().add(new BtrpNode(new SimpleNode("N4", 1, 1, 1)));
        params.add(s2);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test fence(vmset, {}}).
     */
    public void testEmptyNodeSet() {
        FenceBuilder mb = new FenceBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        params.add(s1);

        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        params.add(s2);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }
}
