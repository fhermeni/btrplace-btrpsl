/*
 * Copyright (c) 2012 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrplace.btrpsl;

import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.template.Template;

import java.util.Map;
import java.util.UUID;

/**
 * @author Fabien Hermenier
 */
public class TinyTemplate implements Template {

    @Override
    public BtrpElement build(String name, Map<String, String> options) {
        BtrpElement e = new BtrpElement(BtrpOperand.Type.VM, name, UUID.randomUUID());
        for (Map.Entry<String, String> opt : options.entrySet()) {
            e.addAttribute(opt.getKey(), opt.getValue());
        }
        return e;
    }

    @Override
    public String getIdentifier() {
        return "tinyInstance";
    }
}
