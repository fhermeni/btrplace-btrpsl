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
        try {
            Capacity c = cb.buildConstraint(params);
            Assert.assertEquals(c.getNodes().size(), 3);
            Assert.assertEquals(c.getMaximumCapacity(), 7);
            Assert.assertTrue(c.getAllVirtualMachines().isEmpty());
            //System.err.println(c);
        } catch (ConstraintBuilderException e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    /**
     * Test with the set as a vmset.
     *
     * @throws ConstraintBuilderException the expected exception
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithTypeMismactch() throws ConstraintBuilderException {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        params.add(s1);
        params.add(new BtrpNumber(7, BtrpNumber.Base.base16));
        mb.buildConstraint(params);
    }

    /**
     * Test with empty node.
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithEmptyFirstSet() throws ConstraintBuilderException {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        params.add(s1);
        params.add(new BtrpNumber(7, BtrpNumber.Base.base8));
        mb.buildConstraint(params);
    }

    /**
     * Test without capacity
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithNoCapacity() throws ConstraintBuilderException {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        mb.buildConstraint(params);
    }

    /**
     * Test with bad capacity
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithBadCapacity() throws ConstraintBuilderException {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        params.add(new BtrpSet(1, BtrpOperand.Type.node));
        mb.buildConstraint(params);
    }

    /**
     * Test with bad capacity
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithNegativeCapacity() throws ConstraintBuilderException {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        params.add(new BtrpNumber(-7, BtrpNumber.Base.base16));
        mb.buildConstraint(params);
    }

    public void testWithSingleNode() throws ConstraintBuilderException {
        CapacityBuilder mb = new CapacityBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpNode n1 = new BtrpNode(new SimpleNode("N1", 1, 1, 1));
        params.add(n1);
        params.add(new BtrpNumber(15, BtrpNumber.Base.base16));
        Capacity c = mb.buildConstraint(params);
        Assert.assertEquals(c.getNodes().size(), 1);
        Assert.assertEquals(c.getMaximumCapacity(), 15);
    }
}
