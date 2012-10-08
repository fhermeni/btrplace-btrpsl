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
import entropy.vjob.Lonely;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for LonelyBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestLonelyBuilder {

    /**
     * A simple test that create a constraint.
     */
    public void validCreation() {
        LonelyBuilder mb = new LonelyBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.VM);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm3", 1, 1, 1)));
        params.add(s1);
        Lonely mc = mb.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(mc);
        Assert.assertEquals(mc.getAllVirtualMachines().size(), 3);
    }

    /**
     * Test with the set as a nodeset.
     */
    public void testWithTypeMismactch() {
        LonelyBuilder mb = new LonelyBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test with empty vmset
     */
    public void testWithEmptyFirstSet() {
        LonelyBuilder mb = new LonelyBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.VM);
        params.add(s1);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * lonely(vm1) should be transformed into lonely({vm1});
     */
    public void testWithSingleElement() {
        LonelyBuilder mb = new LonelyBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        Lonely mc = mb.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(mc);
        Assert.assertEquals(mc.getAllVirtualMachines().size(), 1);
    }
}
