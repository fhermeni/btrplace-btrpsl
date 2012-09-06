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
import entropy.vjob.Gather;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for GatherBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestGatherBuilder {

    /**
     * Test gather({vm1,vm2,vm3})
     */
    public void testValid() {
        GatherBuilder mb = new GatherBuilder();
        BtrpSet vms = new BtrpSet(1, BtrpOperand.Type.vm);
        vms.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        vms.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        vms.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm3", 1, 1, 1)));
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(vms);
        try {
            Gather sc = mb.buildConstraint(params);
            Assert.assertEquals(sc.getAllVirtualMachines().size(), 3);
            Assert.assertEquals(sc.getVirtualMachines(), sc.getAllVirtualMachines());
        } catch (ConstraintBuilderException e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    /**
     * Test gather({N1,N2,vm3}). must fail (not a vmset)
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testBadSignature1() throws ConstraintBuilderException {
        GatherBuilder mb = new GatherBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        s1.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        params.add(s1);
        mb.buildConstraint(params);
    }

    /**
     * Test gather({vm1}, {vm2}). must fail: 2 params
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testBadParamNumbers() throws ConstraintBuilderException {
        GatherBuilder mb = new GatherBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.vm);
        s2.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        params.add(s1);
        params.add(s2);
        mb.buildConstraint(params);
    }

    /**
     * Test gather({}). must fail due to an empty set
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testEmptySet() throws ConstraintBuilderException {
        GatherBuilder mb = new GatherBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet vms = new BtrpSet(1, BtrpOperand.Type.vm);
        params.add(vms);
        mb.buildConstraint(params);
    }

    /**
     * Test gather(vm1). must fail due to a single element as a parameter
     * instead of a set
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithBadType() throws ConstraintBuilderException {
        GatherBuilder mb = new GatherBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        mb.buildConstraint(params);
    }
}
