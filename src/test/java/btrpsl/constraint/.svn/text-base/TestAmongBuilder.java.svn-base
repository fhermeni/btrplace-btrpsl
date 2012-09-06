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
import entropy.vjob.Among;
import entropy.vjob.Fence;
import entropy.vjob.PlacementConstraint;
import org.testng.Assert;
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

    public void testOk() {
        AmongBuilder b = new AmongBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet vms = new BtrpSet(1, BtrpOperand.Type.vm);
        vms.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        BtrpSet grps = new BtrpSet(2, BtrpOperand.Type.node);
        BtrpSet g1 = new BtrpSet(1, BtrpOperand.Type.node);
        BtrpSet g2 = new BtrpSet(1, BtrpOperand.Type.node);
        g1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        g2.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        grps.getValues().add(g1);
        grps.getValues().add(g2);
        params.add(vms);
        params.add(grps);
        try {
            PlacementConstraint c = b.buildConstraint(params);
            Assert.assertEquals(c.getClass(), Among.class);
            Assert.assertEquals(c.getAllVirtualMachines().size(), vms.size());
            Assert.assertEquals(((Among) c).getGroups().size(), grps.size());
        } catch (ConstraintBuilderException e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    /**
     * Test with no multi-nodeset.
     *
     * @throws ConstraintBuilderException the exception we expect
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithBadParamsNumber() throws ConstraintBuilderException {
        AmongBuilder b = new AmongBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet vms = new BtrpSet(1, BtrpOperand.Type.vm);
        vms.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        BtrpSet grps = new BtrpSet(2, BtrpOperand.Type.vm);
        BtrpSet g1 = new BtrpSet(1, BtrpOperand.Type.node);
        BtrpSet g2 = new BtrpSet(1, BtrpOperand.Type.node);
        g1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        g2.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        grps.getValues().add(g1);
        grps.getValues().add(g2);
        params.add(vms);
        b.buildConstraint(params);
    }

    /**
     * Test with the first param not a vmset.
     *
     * @throws ConstraintBuilderException the exception we expect
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithNoVMSet() throws ConstraintBuilderException {
        AmongBuilder b = new AmongBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet vms = new BtrpSet(1, BtrpOperand.Type.vm);
        vms.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        BtrpSet grps = new BtrpSet(2, BtrpOperand.Type.node);
        BtrpSet g1 = new BtrpSet(1, BtrpOperand.Type.node);
        BtrpSet g2 = new BtrpSet(1, BtrpOperand.Type.node);
        BtrpSet g3 = new BtrpSet(1, BtrpOperand.Type.node);
        g1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        g2.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        g3.getValues().add(new BtrpNode(new SimpleNode("N3", 1, 1, 1)));
        grps.getValues().add(g1);
        grps.getValues().add(g2);
        params.add(g3);
        params.add(grps);
        b.buildConstraint(params);
    }

    /**
     * The vmset is empty.
     *
     * @throws ConstraintBuilderException the exception we expect
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithEmptyVMSet() throws ConstraintBuilderException {
        AmongBuilder b = new AmongBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet vms = new BtrpSet(1, BtrpOperand.Type.vm);
        BtrpSet grps = new BtrpSet(2, BtrpOperand.Type.node);
        BtrpSet g1 = new BtrpSet(1, BtrpOperand.Type.node);
        BtrpSet g2 = new BtrpSet(1, BtrpOperand.Type.node);
        g1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        g2.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        grps.getValues().add(g1);
        grps.getValues().add(g2);
        params.add(vms);
        params.add(grps);
        b.buildConstraint(params);
    }

    /**
     * One of the nodeset composing the multiset if empty.
     *
     * @throws ConstraintBuilderException the exception we expect
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithEmptyNodeSet() throws ConstraintBuilderException {
        AmongBuilder b = new AmongBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet vms = new BtrpSet(1, BtrpOperand.Type.vm);
        vms.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        BtrpSet grps = new BtrpSet(2, BtrpOperand.Type.node);
        BtrpSet g2 = new BtrpSet(1, BtrpOperand.Type.node);
        BtrpSet g1 = new BtrpSet(1, BtrpOperand.Type.node);
        g2.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        grps.getValues().add(g1);
        grps.getValues().add(g2);
        params.add(vms);
        params.add(grps);
        b.buildConstraint(params);
    }

    /**
     * Test with a single node set. Check if a fence constraint is created.
     */
    public void testWithSingleGroup() {
        AmongBuilder b = new AmongBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet vms = new BtrpSet(1, BtrpOperand.Type.vm);
        vms.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        BtrpSet grps = new BtrpSet(2, BtrpOperand.Type.node);
        BtrpSet g2 = new BtrpSet(1, BtrpOperand.Type.node);
        g2.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        grps.getValues().add(g2);
        params.add(vms);
        params.add(grps);
        try {
            PlacementConstraint c = b.buildConstraint(params);
            Assert.assertEquals(c.getClass(), Fence.class);
            Assert.assertEquals(c.getAllVirtualMachines().size(), vms.size());
            Assert.assertEquals(c.getNodes().size(), g2.size());
        } catch (ConstraintBuilderException e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    public void testWithSingleNode() throws ConstraintBuilderException {
        AmongBuilder mb = new AmongBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet grps = new BtrpSet(2, BtrpOperand.Type.node);
        BtrpSet g2 = new BtrpSet(1, BtrpOperand.Type.node);
        g2.getValues().add(new BtrpNode(new SimpleNode("N2", 1, 1, 1)));
        BtrpSet g1 = new BtrpSet(1, BtrpOperand.Type.node);
        g1.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        grps.getValues().add(g2);
        grps.getValues().add(g1);
        params.add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        params.add(grps);

        Assert.assertEquals(mb.buildConstraint(params).getAllVirtualMachines().size(), 1);
    }

}
