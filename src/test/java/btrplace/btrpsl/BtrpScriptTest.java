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
import btrplace.model.constraint.Gather;
import btrplace.model.constraint.SingleRunningCapacity;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Unit tests for BtrpScript
 *
 * @author Fabien Hermenier
 */
@Test
public class BtrpScriptTest {

    public void testId() {
        BtrpScript v = new BtrpScript();
        v.setFullyQualifiedName("foo");
        Assert.assertEquals(v.getlocalName(), "foo");
        Assert.assertEquals(v.getNamespace(), "");
        Assert.assertEquals(v.id(), "foo");
        v.setFullyQualifiedName("foo.bar");
        Assert.assertEquals(v.getlocalName(), "bar");
        Assert.assertEquals(v.getNamespace(), "foo");
        Assert.assertEquals(v.id(), "foo.bar");
    }

    public void testVMsAddition() {
        BtrpScript v = new BtrpScript();
        BtrpElement vm1 = new BtrpElement(BtrpOperand.Type.VM, "VM1", UUID.randomUUID());
        BtrpElement vm2 = new BtrpElement(BtrpOperand.Type.VM, "VM2", UUID.randomUUID());
        BtrpElement vm3 = new BtrpElement(BtrpOperand.Type.VM, "VM3", UUID.randomUUID());
        v.addVM(vm1);
        v.addVMs(Arrays.asList(vm2, vm3));
        Assert.assertEquals(v.getVMs().size(), 3);
        Assert.assertTrue(v.getVMs().contains(vm1) && v.getVMs().contains(vm2) && v.getVMs().contains(vm3));
    }

    public void testNodeAddition() {
        BtrpScript v = new BtrpScript();
        BtrpElement n1 = new BtrpElement(BtrpOperand.Type.VM, "N1", UUID.randomUUID());
        BtrpElement n2 = new BtrpElement(BtrpOperand.Type.VM, "N2", UUID.randomUUID());

        v.addNode(n1);
        v.addNode(n2);
        Assert.assertEquals(v.getNodes().size(), 2);
        Assert.assertTrue(v.getNodes().contains(n1) && v.getNodes().contains(n2));

    }

    public void testConstraints() {
        BtrpScript v = new BtrpScript();
        UUID vm2 = UUID.randomUUID();
        v.addConstraint(new Gather(Collections.singleton(vm2)));
        v.addConstraint(new SingleRunningCapacity(Collections.singleton(UUID.randomUUID()), 5));
        Assert.assertEquals(v.getConstraints().size(), 2);
    }

    public void testExported() {
        BtrpScript v = new BtrpScript();
        BtrpOperand o1 = new BtrpElement(BtrpOperand.Type.VM, "vm1", UUID.randomUUID());
        v.addExportable("$s", o1);

        //No restrictions, so every can access the exported variable
        Assert.assertEquals(v.getExported("$s"), o1);
        Assert.assertTrue(v.canImport("$s", "foo"));
        Assert.assertTrue(v.canImport("$s", "bar.toto"));

        //Explicit no restriction
        BtrpOperand o2 = new BtrpElement(BtrpOperand.Type.VM, "vm2", UUID.randomUUID());
        v.addExportable("$x", o2, null);
        Assert.assertEquals(v.getExported("$x"), o2);
        Assert.assertTrue(v.canImport("$x", "foo"));
        Assert.assertTrue(v.canImport("$x", "bar.toto"));

        //Unknown export
        Assert.assertFalse(v.canImport("$z", "foo"));
        Assert.assertFalse(v.canImport("$z", "bar.toto"));


        Set<String> valid = new HashSet<String>();
        valid.add("foo");
        valid.add("foo.*");
        valid.add("bar");

        BtrpOperand o3 = new BtrpElement(BtrpOperand.Type.VM, "vm2", UUID.randomUUID());
        v.addExportable("$y", o3, valid);
        Assert.assertNull(v.getExported("$y"));

        Assert.assertEquals(v.getExported("$y", "foo"), o3);

        Assert.assertNull(v.getExported("$y", "zog"));

        Assert.assertEquals(v.getExported("$y", "foo.bar.fii"), o3);

        Assert.assertEquals(v.getExported("$y", "bar"), o3);

        Assert.assertEquals(v.getExported().size(), 3);


    }
}
