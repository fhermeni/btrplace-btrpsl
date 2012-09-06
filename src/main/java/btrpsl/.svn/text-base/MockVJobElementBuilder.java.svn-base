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

package btrpsl;

import entropy.configuration.*;
import entropy.platform.PlatformFactory;
import entropy.template.VirtualMachineTemplateFactory;
import entropy.vjob.builder.VJobElementBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * A Mock to create every asked nodes.
 *
 * @author Fabien Hermenier
 */
public class MockVJobElementBuilder implements VJobElementBuilder {

    private ManagedElementSet<Node> nodesCache;

    private VirtualMachineTemplateFactory templates;

    private PlatformFactory plts;

    /**
     * Make a new mock.
     */
    public MockVJobElementBuilder(VirtualMachineTemplateFactory f, PlatformFactory p) {
        nodesCache = new SimpleManagedElementSet<Node>();
        this.templates = f;
        this.plts = p;
    }

    @Override
    public Node matchAsNode(String id) {
        Node n = nodesCache.get(id);
        if (n == null) {
            n = new SimpleNode(id, 10, 10, 10);
            nodesCache.add(n);
        }
        return n;
    }

    @Override
    public void useConfiguration(Configuration cfg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VirtualMachine matchVirtualMachine(String id) {
        return matchVirtualMachine(id, null, new HashMap<String, String>());
    }

    @Override
    public VirtualMachine matchVirtualMachine(String id, String tpl, Map<String, String> options) {
        VirtualMachine vm = new SimpleVirtualMachine(id);
        vm.setTemplate(tpl);
        for (Map.Entry<String, String> e : options.entrySet()) {
            vm.addOption(e.getKey(), e.getValue());
        }
        return vm;
    }

    @Override
    public VirtualMachineTemplateFactory getTemplates() {
        return templates;
    }

    @Override
    public PlatformFactory getPlatformFactory() {
        return plts;
    }
}
