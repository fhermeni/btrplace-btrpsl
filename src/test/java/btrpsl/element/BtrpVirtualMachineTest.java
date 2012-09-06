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

package btrpsl.element;

import entropy.configuration.SimpleVirtualMachine;
import entropy.configuration.VirtualMachine;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link BtrpVirtualMachine}
 *
 * @author Fabien Hermenier
 */
@Test
public class BtrpVirtualMachineTest {

    public void basic() {
        VirtualMachine vm = new SimpleVirtualMachine("VM1", 1, 1, 1);
        BtrpVirtualMachine b = new BtrpVirtualMachine(vm);
        Assert.assertEquals(b.getElement(), vm);
        Assert.assertEquals(b.toString(), vm.getName());
        Assert.assertEquals(b.type(), BtrpOperand.Type.vm);
        Assert.assertEquals(b.degree(), 0);
        Assert.assertEquals(b.clone(), b);
        Assert.assertEquals(b.prettyType(), "vm");
    }
}
