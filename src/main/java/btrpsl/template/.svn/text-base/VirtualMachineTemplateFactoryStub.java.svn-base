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

import entropy.template.VirtualMachineTemplate;
import entropy.template.VirtualMachineTemplateFactory;
import gnu.trove.THashMap;

import java.util.Map;
import java.util.Set;

/**
 * A fake template factory that create a stub each time a new template is required.
 *
 * @author Fabien Hermenier
 */
public class VirtualMachineTemplateFactoryStub implements VirtualMachineTemplateFactory {

    private Map<String, VirtualMachineTemplate> tpls;

    /**
     * Make a new stub.
     */
    public VirtualMachineTemplateFactoryStub() {
        this.tpls = new THashMap<String, VirtualMachineTemplate>();
    }

    @Override
    public Set<String> getAvailables() {
        return tpls.keySet();
    }

    @Override
    public VirtualMachineTemplate getTemplate(String k) {
        if (!tpls.containsKey(k)) {
            tpls.put(k, new VirtualMachineTemplateStub(k));
        }
        return tpls.get(k);
    }
}
