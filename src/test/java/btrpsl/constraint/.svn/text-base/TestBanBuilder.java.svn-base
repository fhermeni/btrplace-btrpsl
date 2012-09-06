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
import entropy.vjob.Ban;
import org.testng.Assert;
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
        try {
            Ban c = b.buildConstraint(params);
            Assert.assertEquals(c.getVirtualMachines().size(), 1);
            Assert.assertEquals(c.getNodes().size(), 1);
            //System.out.println(c);
        } catch (ConstraintBuilderException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testWithSingleElements() {
        BanBuilder b = new BanBuilder();

        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        params.add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        try {
            Ban c = b.buildConstraint(params);
            Assert.assertEquals(c.getVirtualMachines().size(), 1);
            Assert.assertEquals(c.getNodes().size(), 1);
        } catch (ConstraintBuilderException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithBadParamsNumber() throws ConstraintBuilderException {
        BanBuilder b = new BanBuilder();

        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        params.add(s1);
        b.buildConstraint(params);
    }

    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithEmptyVMSet() throws ConstraintBuilderException {
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BanBuilder b = new BanBuilder();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        s2.getValues().add(new BtrpNode(new SimpleNode("N1", 1, 1, 1)));
        params.add(s1);
        params.add(s2);
        b.buildConstraint(params);
    }

    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithEmptyNodeset() throws ConstraintBuilderException {
        BanBuilder b = new BanBuilder();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.node);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(s1);
        params.add(s2);
        b.buildConstraint(params);
    }

    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithTypeMismatch() throws ConstraintBuilderException {
        BanBuilder b = new BanBuilder();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("VM1", 1, 1, 1)));
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(s1);
        params.add(s1);
        b.buildConstraint(params);
    }
}
