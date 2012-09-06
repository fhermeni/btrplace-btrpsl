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

package btrpsl.template;

import entropy.configuration.SimpleVirtualMachine;
import entropy.configuration.VirtualMachine;
import entropy.template.VirtualMachineBuilderException;
import entropy.template.VirtualMachineTemplate;

import java.util.Map;

/**
 * A fake template used when no real template validation is required.
 *
 * @author Fabien Hermenier
 */
public class VirtualMachineTemplateStub implements VirtualMachineTemplate {

    /**
     * Identifier of the template.
     */
    private String name;

    /**
     * Make a new template with a given identifier.
     *
     * @param name the template identifier.
     */
    public VirtualMachineTemplateStub(String name) {
        this.name = name;
    }

    @Override
    public VirtualMachine build(String name, Map<String, String> options) throws VirtualMachineBuilderException {
        VirtualMachine vm = new SimpleVirtualMachine(name, 0, 0, 0);
        vm.setTemplate(name);
        for (Map.Entry<String, String> e : options.entrySet()) {
            vm.addOption(e.getKey(), e.getValue());
        }
        return vm;
    }

    @Override
    public String getIdentifier() {
        return name;
    }
}
