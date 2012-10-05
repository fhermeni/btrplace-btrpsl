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

import btrpsl.constraint.*;
import btrpsl.element.BtrpNumber;
import btrpsl.element.BtrpSet;
import btrpsl.element.BtrpString;
import btrpsl.includes.PathBasedIncludes;
import btrpsl.platform.PlatformFactoryStub;
import btrpsl.template.VirtualMachineTemplateFactoryStub;
import entropy.configuration.*;
import entropy.vjob.PlacementConstraint;
import entropy.vjob.VJob;
import entropy.vjob.builder.DefaultVJobElementBuilder;
import entropy.vjob.builder.VJobBuilderException;
import entropy.vjob.builder.VJobElementBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

/**
 * @author Fabien Hermenier
 */
@Test(groups = {"unit"}, sequential = true)
public class BtrPlaceVJobBuilderTest {

    private static final String RC_ROOT = "src/test/resources/btrpsl/";

    private static final VJobElementBuilder defaultEb = new DefaultVJobElementBuilder(new VirtualMachineTemplateFactoryStub());

    public void testVMset() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        for (int i = 0; i <= 100; i++) {
            cfg.addOnline(new SimpleNode("helios-" + i + ".sophia.grid5000.fr", 1, 1, 1));
            cfg.addOnline(new SimpleNode("sol-" + i + ".sophia.grid5000.fr", 1, 1, 1));
            cfg.addWaiting(new SimpleVirtualMachine("vapp.VM" + i, 1, 1, 1));
        }
        cfg.addWaiting(new SimpleVirtualMachine("vapp.VMtoto", 1, 1, 1));
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new CapacityBuilder());
        c.add(new ContinuousSpreadBuilder());
        c.add(new AmongBuilder());
        c.add(new LonelyBuilder());
        c.add(new RootBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        PathBasedIncludes includes = new PathBasedIncludes(b, new File(RC_ROOT));
        b.setIncludes(includes);
        try {
            VJob v = b.build(new File(RC_ROOT + "vapp.btrp"));
            Assert.assertEquals(v.id(), "vapp");
            Assert.assertEquals(v.getNodes().size(), 40);
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    public void testSophia() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        for (int i = 0; i <= 100; i++) {
            cfg.addOnline(new SimpleNode("helios-" + i + ".sophia.grid5000.fr", 1, 1, 1));
        }
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new CapacityBuilder());
        c.add(new ContinuousSpreadBuilder());
        c.add(new AmongBuilder());
        c.add(new LonelyBuilder());
        c.add(new RootBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            VJob v = b.build(new File(RC_ROOT + "sophia/helios.btrp"));
            System.err.println("vjobs: " + v);
            Assert.assertEquals(v.getVirtualMachines(), cfg.getAllVirtualMachines());
            Assert.assertEquals(v.getNodes().size(), 56);
            Assert.assertEquals(v.getConstraints().size(), 59);


        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    /**
     * test (valid) computation part of the language.
     */
    public void testNumberComputation() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            BtrPlaceVJob v = b.build(new File(RC_ROOT + "number.btrp"));
            BtrpNumber x = (BtrpNumber) v.getExported("$x");
            BtrpNumber y = (BtrpNumber) v.getExported("$y");
            BtrpNumber z = (BtrpNumber) v.getExported("$z");
            BtrpNumber a = (BtrpNumber) v.getExported("$a");
            BtrpNumber b2 = (BtrpNumber) v.getExported("$b");
            BtrpNumber c2 = (BtrpNumber) v.getExported("$c");
            BtrpNumber toto = (BtrpNumber) v.getExported("$toto");
            BtrpNumber titi = (BtrpNumber) v.getExported("$titi");
            BtrpNumber foo = (BtrpNumber) v.getExported("$foo");
            BtrpNumber bar = (BtrpNumber) v.getExported("$bar");
            BtrpNumber bi = (BtrpNumber) v.getExported("$bi");

            BtrpNumber f1 = (BtrpNumber) v.getExported("$f1");
            BtrpNumber f2 = (BtrpNumber) v.getExported("$f2");
            BtrpNumber f3 = (BtrpNumber) v.getExported("$f3");


            //System.err.println(v);
            Assert.assertTrue(x.isInteger());
            Assert.assertEquals(x.getIntValue(), 2);

            Assert.assertTrue(y.isInteger());
            Assert.assertEquals(y.getIntValue(), 8);

            Assert.assertTrue(z.isInteger());
            Assert.assertEquals(z.getIntValue(), 26);

            Assert.assertTrue(a.isInteger());
            Assert.assertEquals(a.getIntValue(), 2);

            Assert.assertTrue(b2.isInteger());
            Assert.assertEquals(b2.getIntValue(), 0);

            Assert.assertTrue(c2.isInteger());
            Assert.assertEquals(c2.getIntValue(), 1);

            Assert.assertFalse(f1.isInteger());
            Assert.assertEquals(f1.getDoubleValue(), 0.7);

            Assert.assertFalse(f2.isInteger());
            Assert.assertEquals(f2.getDoubleValue(), 3.0);

            Assert.assertFalse(f3.isInteger());
            Assert.assertEquals(f3.getDoubleValue(), 892, 5);

            BtrpNumber baz = (BtrpNumber) v.getExported("$baz");
            Assert.assertEquals(baz, BtrpNumber.TRUE);

            BtrpNumber biz = (BtrpNumber) v.getExported("$biz");
            Assert.assertEquals(biz, BtrpNumber.TRUE);

            Assert.assertEquals(toto, BtrpNumber.FALSE);
            Assert.assertEquals(titi, BtrpNumber.TRUE);
            Assert.assertEquals(foo, BtrpNumber.TRUE);
            Assert.assertEquals(bar, BtrpNumber.TRUE);
            Assert.assertEquals(bi, BtrpNumber.FALSE);


        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    public void testSetManipulation() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        for (int i = 1; i <= 100; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("setManip.VM" + i, 1, 1, 1));
            cfg.addOnline(new SimpleNode("pastel-" + i + ".b217.home", 1, 1, 1));
        }
        cfg.addOnline(new SimpleNode("pastel-frontend.b217.home", 1, 1, 1));

        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            BtrPlaceVJob v = b.build(new File(RC_ROOT + "setManip.btrp"));
            BtrpSet t1 = (BtrpSet) v.getExported("$T1");
            BtrpSet t2 = (BtrpSet) v.getExported("$T2");
            BtrpSet t3 = (BtrpSet) v.getExported("$T3");
            BtrpNumber x = (BtrpNumber) v.getExported("$x");
            BtrpNumber res = (BtrpNumber) v.getExported("$res");
            BtrpNumber res2 = (BtrpNumber) v.getExported("$res2");
            BtrpNumber res3 = (BtrpNumber) v.getExported("$res3");
            BtrpNumber y = (BtrpNumber) v.getExported("$y");

            Assert.assertEquals(t1.size() + t2.size() + t3.size(), 100);

            Assert.assertEquals(x.getIntValue(), 12);
            Assert.assertEquals(y.getIntValue(), 3);

            BtrpSet C = (BtrpSet) v.getExported("$C");
            Assert.assertEquals(C.size(), 90);

            BtrpSet a = (BtrpSet) v.getExported("$a");

            Assert.assertEquals(res, BtrpNumber.TRUE);
            Assert.assertEquals(res2, BtrpNumber.TRUE);
            Assert.assertEquals(res3, BtrpNumber.TRUE);

            Assert.assertEquals(a.degree(), 2);
            Assert.assertEquals(a.size(), 4);
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    public void testIfStatement() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        for (int i = 1; i <= 100; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("ifStatement.VM" + i, 1, 1, 1));
            cfg.addOnline(new SimpleNode("pastel-" + i + ".b217.home", 1, 1, 1));
        }
        cfg.addOnline(new SimpleNode("pastel-frontend.b217.home", 1, 1, 1));

        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            BtrPlaceVJob v = b.build(new File(RC_ROOT + "ifStatement.btrp"));
            BtrpNumber first = (BtrpNumber) v.getExported("$first");
            BtrpNumber second = (BtrpNumber) v.getExported("$second");
            BtrpNumber third = (BtrpNumber) v.getExported("$third");
            Assert.assertNotNull(first);
            Assert.assertNotNull(second);
            Assert.assertEquals(first, BtrpNumber.TRUE);
            Assert.assertEquals(second, BtrpNumber.TRUE);
            Assert.assertEquals(third.getIntValue(), 5);
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    public void testTemplate1() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);

        try {
            BtrPlaceVJob v = b.build("namespace test.template;\nVM[1..20] : tinyVMs<migratable,volatile>;\nVMfrontend : mediumVMs;\n");
            Assert.assertEquals(v.getVirtualMachines().size(), 21);
            for (VirtualMachine vm : v.getVirtualMachines()) {
                if (!vm.getOptions().isEmpty()) {
                    Assert.assertTrue(vm.checkOption("migratable"));
                    Assert.assertTrue(vm.checkOption("volatile"));
                }
            }
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    /**
     * Test templates with key/value pairs
     */
    public void testTemplate2() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);

        try {
            BtrPlaceVJob v = b.build("namespace test.template;\nVM[1..20] : tinyVMs<migratable,start=\"+7\",stop=12>;\nVMfrontend : mediumVMs;\n");
            Assert.assertEquals(v.getVirtualMachines().size(), 21);
            for (VirtualMachine vm : v.getVirtualMachines()) {
                if (!vm.getOptions().isEmpty()) {
                    Assert.assertTrue(vm.checkOption("migratable"));
                    Assert.assertTrue(vm.checkOption("start"));
                    Assert.assertTrue(vm.checkOption("stop"));
                    Assert.assertEquals(vm.getOption("start"), "+7");
                    Assert.assertEquals(vm.getOption("stop"), "12");
                }
            }
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }


    @Test(expectedExceptions = {VJobBuilderException.class})
    public void testMultipleVMTemplate() throws VJobBuilderException {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);

        BtrPlaceVJob v = b.build("namespace test.template;\nVM[1..20] : tinyVMs<migratable,start=7,stop=12>, mediumVMs;\n");
        Assert.assertEquals(v.getVirtualMachines().size(), 21);
        for (VirtualMachine vm : v.getVirtualMachines()) {
            if (!vm.getOptions().isEmpty()) {
                Assert.assertTrue(vm.checkOption("migratable"));
                Assert.assertTrue(vm.checkOption("start"));
                Assert.assertTrue(vm.checkOption("stop"));
                Assert.assertEquals(vm.getOption("start"), "+7");
                Assert.assertEquals(vm.getOption("stop"), "12");
            }
        }
    }


    public void testMultipleNodeTemplate() {
        VJobElementBuilder e = new DefaultVJobElementBuilder(new VirtualMachineTemplateFactoryStub(), new PlatformFactoryStub());
        Configuration cfg = new SimpleConfiguration();
        for (int i = 1; i <= 21; i++) {
            cfg.addOnline(new SimpleNode("N" + i, 5, 5, 5));
        }
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            BtrPlaceVJob v = b.build("namespace test.template;\n@N[1..20] : xen<install=10,boot=5,halt=4>, kvm<install=6,boot=7,halt=6>;@N21 : vmware<foo,bar=5>;\n");
            Assert.assertEquals(v.getNodes().size(), 21);
            for (Node n : v.getNodes()) {
                if (n.getName().equals("N21")) {
                    Map<String, String> vmware = n.getPlatformOptions("vmware");
                    Assert.assertEquals(vmware.get("bar"), "5");
                    Assert.assertTrue(vmware.containsKey("foo"));
                } else {
                    Map<String, String> xen = n.getPlatformOptions("xen");
                    Assert.assertEquals(xen.get("install"), "10");
                    Assert.assertEquals(xen.get("boot"), "5");
                    Assert.assertEquals(xen.get("halt"), "4");

                    Map<String, String> kvm = n.getPlatformOptions("kvm");
                    Assert.assertEquals(kvm.get("install"), "6");
                    Assert.assertEquals(kvm.get("boot"), "7");
                    Assert.assertEquals(kvm.get("halt"), "6");
                }

            }
        } catch (VJobBuilderException ex) {
            Assert.fail(ex.getMessage(), ex);
        }
    }

    /**
     * test (valid) computation part of the language.
     */
    public void testLogical() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            BtrPlaceVJob v = b.build(new File(RC_ROOT + "logical.btrp"));
            BtrpNumber and1 = (BtrpNumber) v.getExported("$and1");
            BtrpNumber and2 = (BtrpNumber) v.getExported("$and2");
            BtrpNumber and3 = (BtrpNumber) v.getExported("$and3");
            BtrpNumber and4 = (BtrpNumber) v.getExported("$and4");

            BtrpNumber or1 = (BtrpNumber) v.getExported("$or1");
            BtrpNumber or2 = (BtrpNumber) v.getExported("$or2");
            BtrpNumber or3 = (BtrpNumber) v.getExported("$or3");
            BtrpNumber or4 = (BtrpNumber) v.getExported("$or4");

            Assert.assertEquals(and1, BtrpNumber.FALSE);
            Assert.assertEquals(and2, BtrpNumber.FALSE);
            Assert.assertEquals(and3, BtrpNumber.FALSE);
            Assert.assertEquals(and4, BtrpNumber.TRUE);

            Assert.assertEquals(or1, BtrpNumber.TRUE);
            Assert.assertEquals(or2, BtrpNumber.TRUE);
            Assert.assertEquals(or3, BtrpNumber.TRUE);
            Assert.assertEquals(or4, BtrpNumber.FALSE);

            BtrpNumber h1 = (BtrpNumber) v.getExported("$h1");
            BtrpNumber h2 = (BtrpNumber) v.getExported("$h2");
            Assert.assertEquals(h1, BtrpNumber.TRUE);
            Assert.assertEquals(h2, BtrpNumber.TRUE);


        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    /**
     * test (valid) computation part of the language.
     */
    public void testComplex() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 200; i++) {
            cfg.addOnline(new SimpleNode("node-" + i, 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new ContinuousSpreadBuilder());
        c.add(new LonelyBuilder());
        c.add(new AmongBuilder());
        c.add(new FenceBuilder());
        c.add(new BanBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        PathBasedIncludes includes = new PathBasedIncludes(b, new File(RC_ROOT + "env"));
        b.setIncludes(includes);
        try {
            BtrPlaceVJob v = b.build(new File(RC_ROOT + "env/sysadmin.btrp"));
            System.err.println(v);
            BtrpNumber card = (BtrpNumber) v.getExported("$card");
            Assert.assertEquals(card.getIntValue(), 30);

        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    public void testExportWithValidRestrictions() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 56; i++) {
            cfg.addOnline(new SimpleNode("helios-" + i + ".sophia.grid5000.fr", 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        PathBasedIncludes includes = new PathBasedIncludes(b, new File(RC_ROOT));
        b.setIncludes(includes);

        try {
            b.build("namespace zog; import testExport; for $n in $racks { }");

            b.build("namespace toto; import testExport; for $n in $nodes { }");

            b.build("namespace testExport.bla; import testExport; for $n in $nodes { } for $r in $racks {}");

            b.build("namespace sysadmin; import testExport; for $n in $nodes { } for $r in $racks {} for $n in $testExport {}");

        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }

        //Now, with bad restrictions
        try {
            b.build("namespace zog; import testExport; for $n in $nodes { }");
            Assert.fail();
        } catch (Exception x) {
        }

        try {
            b.build("namespace sysadmin.foo; import testExport; for $n in $nodes { }");
            Assert.fail();
        } catch (Exception x) {
        }

        try {
            b.build("namespace sysadmin.foo; import testExport; for $n in $testExport { }");
            Assert.fail();
        } catch (Exception x) {
        }
    }

    public void testMeUsage() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new LonelyBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            VJob v = b.build("namespace foo; VM[1..5] : tiny;\nVM[6..10] : small;\n lonely($me); ");
            PlacementConstraint cs = v.getConstraints().iterator().next();
            Assert.assertTrue(cs.getAllVirtualMachines().equals(cfg.getWaitings()));
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    /**
     * Must fail cause $me is immutable
     */
    @Test(expectedExceptions = {VJobBuilderException.class})
    public void testMeReassignment() throws VJobBuilderException {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new LonelyBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        VJob v = b.build("namespace foo; VM[1..5] : tiny;\nVM[6..10] : small;\n $me = 7; ");
        PlacementConstraint cs = v.getConstraints().iterator().next();
        Assert.assertTrue(cs.getAllVirtualMachines().equals(cfg.getWaitings()));
    }

    public void testStringSupport() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new LonelyBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            BtrPlaceVJob v = b.build("namespace foo; VM[1..10] : tiny;\n$arr = {\"foo\",\"bar\", \"baz\"};$arr2 = $arr + {\"git\"}; $out = \"come \" + \"out \" + 5 + \" \" + VM1; export $arr2,$out to *;");
            BtrpString out = (BtrpString) v.getExported("$out");
            BtrpSet arr2 = (BtrpSet) v.getExported("$arr2");
            Assert.assertEquals(out.toString(), "come out 5 foo.VM1");
            Assert.assertEquals(arr2.size(), 4);
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    public void testLargeRange() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        for (int i = 251; i <= 500; i++) {
            cfg.addOnline(new SimpleNode("N" + i, 1, 1, 1));
        }
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            BtrPlaceVJob v = b.build("namespace foo; $all = @N[251..500]; export $all to *;");
            BtrpSet out = (BtrpSet) v.getExported("$all");
            Assert.assertEquals(out.size(), 250);
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }


    }

    /**
     * Test the access to the dependencies.
     */
    public void testDependencies() {
        /*
         * a
         * |- b
         * \– c
         *    \-d
         */
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 200; i++) {
            cfg.addOnline(new SimpleNode("node-" + i, 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        PathBasedIncludes includes = new PathBasedIncludes(b, new File(RC_ROOT + "deps"));
        b.setIncludes(includes);
        try {
            BtrPlaceVJob v = b.build(new File(RC_ROOT + "deps/a.btrp"));
            Assert.assertEquals(v.getDependencies().size(), 2);
            String res = "a\n" +
                    "|- b\n" +
                    "   |- in.titi\n" +
                    "   \\- in.toto\n" +
                    "\\- c\n" +
                    "   |- out.foo\n" +
                    "   \\- out.bar\n";
            Assert.assertEquals(v.prettyDependencies(), res);
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }


    public void testVariablesInElementRange() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        for (int i = 0; i < 20; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("range.VM" + i, 1, 1, 1));
        }
        cfg.addWaiting(new SimpleVirtualMachine("range.VMbaz", 1, 1, 1));
        cfg.addWaiting(new SimpleVirtualMachine("range.VMzip", 1, 1, 1));
        e.useConfiguration(cfg);

        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new RootBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        try {
            BtrPlaceVJob v = b.build(new File(RC_ROOT + "range.btrp"));
            BtrpSet s = (BtrpSet) v.getExported("$foo");

            System.out.println(s);
            Assert.assertNotNull(v.getVirtualMachines().get("range.VM5"));
            Assert.assertNotNull(v.getVirtualMachines().get("range.VMbaz"));
            Assert.assertNotNull(v.getVirtualMachines().get("range.VM7"));
            Assert.assertNotNull(v.getVirtualMachines().get("range.VMzip"));
            Assert.assertNotNull(v.getVirtualMachines().get("range.VM9"));
            Assert.assertNotNull(v.getVirtualMachines().get("range.VM10"));
            Assert.assertNotNull(v.getVirtualMachines().get("range.VM11"));
            Assert.assertNotNull(v.getVirtualMachines().get("range.VM12"));
            Assert.assertEquals(s.size(), 9);
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    /**
     * Creation of a constraint with invalid parameters. Should throw one error.
     * TODO: no way currently to test for the number of returned errors.
     *
     * @throws VJobBuilderException
     */
    @Test(expectedExceptions = {VJobBuilderException.class})
    public void testConstraintWithBadParameters() throws VJobBuilderException {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new LonelyBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        b.build("namespace foo; VM[1..10] : tiny;\nlonely(N15);");
    }

    @Test(expectedExceptions = {VJobBuilderException.class})
    public void testWithLexerErrors() throws VJobBuilderException {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 10; i++) {
            cfg.addWaiting(new SimpleVirtualMachine("foo.VM" + i, 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new RootBuilder());
        BtrPlaceVJobBuilder b = new BtrPlaceVJobBuilder(e, c);
        b.build("namespace foo; VM[1..10] : tiny;\nroot(VM10;");
    }
}
