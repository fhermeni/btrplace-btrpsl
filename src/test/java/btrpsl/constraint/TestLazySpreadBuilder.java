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
import entropy.vjob.Spread;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for LazySpreadBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestLazySpreadBuilder {

    /**
     * Test lSpread({vm1,vm2,vm3})
     */
    public void testValid() {
        LazySpreadBuilder mb = new LazySpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.VM);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM2", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM3", 1, 1, 1)));
        params.add(s1);
        Spread sc = mb.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(sc);
        Assert.assertEquals(sc.getAllVirtualMachines().size(), 3);
        Assert.assertEquals(sc.getVirtualMachines(), sc.getAllVirtualMachines());
    }

    /**
     * Test spread({N1,N2,vm3}). must fail (not a vmset)
     */
    public void testTypeMismatch() {
        LazySpreadBuilder mb = new LazySpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        s1.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        params.add(s1);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test spread({vm1}, {vm2}). must fail: 2 params
     */
    public void testWithBadParamsNumbers() {
        LazySpreadBuilder mb = new LazySpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.VM);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.VM);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        params.add(s1);
        params.add(s2);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test lSpread({})
     */
    public void testWithEmptySet() {
        LazySpreadBuilder mb = new LazySpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.VM);
        params.add(s1);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test spread(vm1). must fail due to a single element as a parameter
     * instead of a set
     */
    public void testWithBadType() {
        LazySplitBuilder mb = new LazySplitBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }
}
