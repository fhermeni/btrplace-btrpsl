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

import btrpsl.element.*;
import entropy.configuration.SimpleNode;
import entropy.configuration.SimpleVirtualMachine;
import entropy.vjob.Capacity;
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
public class TestCapacityBuilder {

    /**
     * A simple test that create a constraint.
     */
    public void validCreation() {
        CapacityBuilder cb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        s1.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        s1.getValues().add(new BtrpNode(new SimpleNode("N3", 1, 1, 1)));
        params.add(s1);
        params.add(new BtrpNumber(7, BtrpNumber.Base.base10));
        Capacity c = cb.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getNodes().size(), 3);
        Assert.assertEquals(c.getMaximumCapacity(), 7);
        Assert.assertTrue(c.getAllVirtualMachines().isEmpty());
        //System.err.println(c);
    }

    /**
     * Test with the set as a vmset.
     */
    public void testWithTypeMismactch() {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        params.add(s1);
        params.add(new BtrpNumber(7, BtrpNumber.Base.base16));
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test with empty node.
     */
    public void testWithEmptyFirstSet() {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        params.add(s1);
        params.add(new BtrpNumber(7, BtrpNumber.Base.base8));
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test without capacity
     */
    public void testWithNoCapacity() {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test with bad capacity
     */
    public void testWithBadCapacity() {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        params.add(new BtrpSet(1, BtrpOperand.Type.node));
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test with bad capacity
     */
    public void testWithNegativeCapacity() {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        params.add(new BtrpNumber(-7, BtrpNumber.Base.base16));
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    public void testWithSingleNode() {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpNode n1 = new BtrpNode(new SimpleNode("N1", 1, 1, 1));
        params.add(n1);
        params.add(new BtrpNumber(15, BtrpNumber.Base.base16));
        Capacity c = mb.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getNodes().size(), 1);
        Assert.assertEquals(c.getMaximumCapacity(), 15);
    }
}
