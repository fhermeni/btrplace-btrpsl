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

import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpVirtualMachine;
import entropy.configuration.*;
import entropy.vjob.Capacity;
import entropy.vjob.Gather;
import gnu.trove.THashSet;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for BtrPlaceVJob
 *
 * @author Fabien Hermenier
 */
@Test
public class BtrPlaceVJobTest {

    public void testId() {
        BtrPlaceVJob v = new BtrPlaceVJob();
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
        BtrPlaceVJob v = new BtrPlaceVJob();
        VirtualMachine vm1 = new SimpleVirtualMachine("VM1");
        VirtualMachine vm2 = new SimpleVirtualMachine("VM2");
        VirtualMachine vm3 = new SimpleVirtualMachine("VM3");
        ManagedElementSet<VirtualMachine> vms = new SimpleManagedElementSet<VirtualMachine>();
        vms.add(vm1);
        vms.add(vm3);
        v.addVirtualMachine(vm1);
        v.addVirtualMachines(vms);
        vms = new SimpleManagedElementSet<VirtualMachine>();
        vms.add(vm2);
        v.addConstraint(new Gather(vms));
        //System.out.println(v.getVirtualMachines());
        Assert.assertEquals(v.getVirtualMachines().size(), 3);
        Assert.assertTrue(v.getVirtualMachines().contains(vm1) && v.getVirtualMachines().contains(vm2) && v.getVirtualMachines().contains(vm3));
    }

    public void testNodeAddition() {
        BtrPlaceVJob v = new BtrPlaceVJob();
        Node n1 = new SimpleNode("n1");
        Node n2 = new SimpleNode("n2");
        Node n3 = new SimpleNode("n3");
        v.addNode(n1);
        v.addNode(n3);
        ManagedElementSet<Node> ns = new SimpleManagedElementSet<Node>();
        ns.add(n2);
        v.addConstraint(new Capacity(ns, 5));
        Assert.assertEquals(v.getNodes().size(), 3);
        Assert.assertTrue(v.getNodes().contains(n1) && v.getNodes().contains(n2) && v.getNodes().contains(n3));

    }

    public void testConstraints() {
        BtrPlaceVJob v = new BtrPlaceVJob();
        VirtualMachine vm2 = new SimpleVirtualMachine("VM2");
        ManagedElementSet<VirtualMachine> vms = new SimpleManagedElementSet<VirtualMachine>();
        vms.add(vm2);
        v.addConstraint(new Gather(vms));

        Node n2 = new SimpleNode("n2");
        ManagedElementSet<Node> ns = new SimpleManagedElementSet<Node>();
        ns.add(n2);
        v.addConstraint(new Capacity(ns, 5));

        Assert.assertEquals(v.getConstraints().size(), 2);


    }

    public void testExported() {
        BtrPlaceVJob v = new BtrPlaceVJob();
        BtrpOperand o1 = new BtrpVirtualMachine(new SimpleVirtualMachine("vm1"));
        v.addExportable("$s", o1);

        //No restrictions, so every can access the exported variable
        Assert.assertEquals(v.getExported("$s"), o1);
        Assert.assertTrue(v.canImport("$s", "foo"));
        Assert.assertTrue(v.canImport("$s", "bar.toto"));

        //Explicit no restriction
        BtrpOperand o2 = new BtrpVirtualMachine(new SimpleVirtualMachine("vm2"));
        v.addExportable("$x", o2, null);
        Assert.assertEquals(v.getExported("$x"), o2);
        Assert.assertTrue(v.canImport("$x", "foo"));
        Assert.assertTrue(v.canImport("$x", "bar.toto"));

        //Unknown export
        Assert.assertFalse(v.canImport("$z", "foo"));
        Assert.assertFalse(v.canImport("$z", "bar.toto"));


        THashSet<String> valid = new THashSet<String>();
        valid.add("foo");
        valid.add("foo.*");
        valid.add("bar");

        BtrpOperand o3 = new BtrpVirtualMachine(new SimpleVirtualMachine("vm2"));
        v.addExportable("$y", o3, valid);
        Assert.assertNull(v.getExported("$y"));

        Assert.assertEquals(v.getExported("$y", "foo"), o3);

        Assert.assertNull(v.getExported("$y", "zog"));

        Assert.assertEquals(v.getExported("$y", "foo.bar.fii"), o3);

        Assert.assertEquals(v.getExported("$y", "bar"), o3);

        Assert.assertEquals(v.getExported().size(), 3);


    }
}
