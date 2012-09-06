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

import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.BtrpVirtualMachine;
import entropy.configuration.SimpleVirtualMachine;
import entropy.vjob.ContinuousSpread;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for ContinuousSpreadBuilder.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestContinuousSpreadBuilder {

    /**
     * Test cSpread({vm1,vm2,vm3})
     */
    public void testValid() {
        ContinuousSpreadBuilder mb = new ContinuousSpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm3", 1, 1, 1)));
        params.add(s1);
        try {
            ContinuousSpread sc = mb.buildConstraint(params);
            Assert.assertEquals(sc.getAllVirtualMachines().size(), 3);
            Assert.assertEquals(sc.getVirtualMachines().size(), 3);
        } catch (ConstraintBuilderException e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    /**
     * Test spread({vm1}, {vm2}). must fail: 2 params
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithBadParamsNumber() throws ConstraintBuilderException {
        ContinuousSpreadBuilder mb = new ContinuousSpreadBuilder();
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
     * Test spread({}). must fail (empty set)
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithEmptySet() throws ConstraintBuilderException {
        ContinuousSpreadBuilder mb = new ContinuousSpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.vm);
        params.add(s1);
        mb.buildConstraint(params);
    }

    /**
     * Test spread(vm1). must fail due to a single element as a parameter
     * instead of a set
     */
    @Test(expectedExceptions = {ConstraintBuilderException.class})
    public void testWithBadType() throws ConstraintBuilderException {
        ContinuousSpreadBuilder mb = new ContinuousSpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        mb.buildConstraint(params);
    }
}
