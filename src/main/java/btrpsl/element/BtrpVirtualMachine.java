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

import entropy.configuration.VirtualMachine;

/**
 * An operand that point to a virtual machine.
 *
 * @author Fabien Hermenier
 */
public class BtrpVirtualMachine extends BtrpElement implements Cloneable {

    private VirtualMachine vm;

    /**
     * Make a new operand.
     *
     * @param vm the associated virtual machine
     */
    public BtrpVirtualMachine(VirtualMachine vm) {
        this.vm = vm;
    }

    @Override
    public VirtualMachine getElement() {
        return this.vm;
    }

    @Override
    public Type type() {
        return Type.VM;
    }

    @Override
    public BtrpVirtualMachine clone() {
        return new BtrpVirtualMachine(vm);
    }

    @Override
    public String toString() {
        return vm.getName();
    }

}
