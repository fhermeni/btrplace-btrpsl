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
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.VM);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm3", 1, 1, 1)));
        params.add(s1);
        ContinuousSpread sc = mb.buildConstraint(new MockBtrPlaceTree(), params);
        Assert.assertNotNull(sc);
        Assert.assertEquals(sc.getAllVirtualMachines().size(), 3);
        Assert.assertEquals(sc.getVirtualMachines().size(), 3);
    }

    /**
     * Test spread({vm1}, {vm2}). must fail: 2 params
     */
    public void testWithBadParamsNumber() {
        ContinuousSpreadBuilder mb = new ContinuousSpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.VM);
        s1.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm1", 1, 1, 1)));
        BtrpSet s2 = new BtrpSet(1, BtrpOperand.Type.VM);
        s2.getValues().add(new BtrpVirtualMachine(new SimpleVirtualMachine("vm2", 1, 1, 1)));
        params.add(s1);
        params.add(s2);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test spread({}). must fail (empty set)
     */
    public void testWithEmptySet() {
        ContinuousSpreadBuilder mb = new ContinuousSpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        BtrpSet s1 = new BtrpSet(1, BtrpOperand.Type.VM);
        params.add(s1);
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }

    /**
     * Test spread(n1) fail cause bad type.
     */
    public void testWithBadType() {
        ContinuousSpreadBuilder mb = new ContinuousSpreadBuilder();
        List<BtrpOperand> params = new LinkedList<BtrpOperand>();
        params.add(new BtrpNode(new SimpleNode("n1", 1, 1, 1)));
        Assert.assertNull(mb.buildConstraint(new MockBtrPlaceTree(), params));
    }
}
