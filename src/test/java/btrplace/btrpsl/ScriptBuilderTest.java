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
import btrplace.btrpsl.element.BtrpNumber;
import btrplace.btrpsl.element.BtrpSet;
import btrplace.btrpsl.element.BtrpString;
import btrplace.btrpsl.includes.PathBasedIncludes;
import btrplace.model.SatConstraint;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Unit tests for {@link ScriptBuilder}.
 *
 * @author Fabien Hermenier
 */
@Test(groups = {"unit"}, sequential = true)
public class ScriptBuilderTest {

    private static final String RC_ROOT = "src/test/resources/btrplace/btrpsl/";
          /*



    @Test(expectedExceptions = {ScriptBuilderException.class})
    public void testWithBadFilename() throws ScriptBuilderException {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        ScriptBuilder b = new ScriptBuilder(e, c);
        try {
            Script v = b.build(new File(RC_ROOT + "badName.btrp"));
        } catch (ScriptBuilderException ex) {
            System.out.println(ex);
            Assert.assertEquals(ex.getErrorReporter().getErrors().size(), 1);
            System.out.flush();
            throw ex;
        }

    }

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
        c.add(new CumulatedRunningCapacityBuilder());
        c.add(new SpreadBuilder());
        c.add(new AmongBuilder());
        c.add(new LonelyBuilder());
        c.add(new RootBuilder());
        ScriptBuilder b = new ScriptBuilder(e, c);
        PathBasedIncludes includes = new PathBasedIncludes(b, new File(RC_ROOT));
        b.setIncludes(includes);
        try {
            VJob v = b.build(new File(RC_ROOT + "vapp.btrp"));
            System.err.println(v);
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
        c.add(new CumulatedRunningCapacityBuilder());
        c.add(new SpreadBuilder());
        c.add(new AmongBuilder());
        c.add(new LonelyBuilder());
        c.add(new RootBuilder());
        ScriptBuilder b = new ScriptBuilder(e, c);
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
          */

    public void testNumberComputation() {
        ScriptBuilder b = new ScriptBuilder();
        try {
            Script v = b.build(new File(RC_ROOT + "number.btrp"));
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

    public void testSetManipulation() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();

        Script v = b.build(new File(RC_ROOT + "setManip.btrp"));
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
    }

    @Test(expectedExceptions = {ScriptBuilderException.class})
    public void testSetManipulationWithErrors() throws ScriptBuilderException {
        try {
            ScriptBuilder b = new ScriptBuilder();
            Script v = b.build(
                    "namespace test.template;\n" +
                            "VM[1..20] : tinyVMs<migratable,volatile>;\n" +
                            "$x = VM[1..10] + VM15;\n" +
                            "$y = VM[1..10] + @N[1..20,57];\n" +
                            "$z = VM[1..10] + 7;\n" +
                            "$a = VM[1..10] - {VM[1..10]};\n" +
                            "$b = VM[1..10] / @N1;\n" +
                            "$c = VM[1..10] / @N[1,3];\n" +
                            "$d = VM[1..10] * VM[21,22];\n" +
                            "$e = VM[22,23] / 2;\n"
            );
        } catch (ScriptBuilderException x) {
            System.out.println(x);
            Assert.assertEquals(x.getErrorReporter().getErrors().size(), 8);
            throw x;
        }
    }

    public void testIfStatement() {
        ScriptBuilder b = new ScriptBuilder();
        try {
            Script v = b.build(new File(RC_ROOT + "ifStatement.btrp"));
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


    /**
     * Test templates on VMs and nodes.
     */
    public void testTemplate1() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        Script v = b.build("namespace test.template;\nVM[1..5] : tinyVMs;\nfrontend : mediumVMs; @N[1..12] : defaultNodes;\n");
        Assert.assertEquals(v.getVMs().size(), 6);
        for (BtrpElement el : v.getVMs()) {
            if (el.getElement().endsWith("frontend")) {
                Assert.assertEquals(el.getTemplate(), "mediumVMs");
            } else {
                Assert.assertEquals(el.getTemplate(), "tinyVMs");
            }
        }

        Assert.assertEquals(v.getNodes().size(), 12);
        for (BtrpElement el : v.getNodes()) {
            Assert.assertEquals(el.getTemplate(), "defaultNodes");
        }
    }

    @Test/*(dependsOnMethods = {"testTemplate1"})*/
    public void testTemplateWithOptions() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        Script v = b.build("namespace test.template;\nVM[1..3] : tinyVMs<migratable,start=\"7.5\",stop=12>;");
        Assert.assertEquals(v.getVMs().size(), 3);
        for (BtrpElement el : v.getVMs()) {
            Assert.assertEquals(v.getAttributes().getKeys(el.getUUID()).size(), 3);
            Assert.assertEquals(v.getAttributes().getBoolean(el.getUUID(), "migratable").booleanValue(), true);
            Assert.assertEquals(v.getAttributes().getDouble(el.getUUID(), "start").doubleValue(), 7.5);
            Assert.assertEquals(v.getAttributes().getLong(el.getUUID(), "stop").longValue(), 12);
        }
    }


    public void testTemplate2() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();

        Script v = b.build("namespace test.template;\nVM[1..20] : tinyVMs<migratable,start=\"+7\",stop=12>;\nVMfrontend : mediumVMs;\n");

    }


    public void testLogical() {
        ScriptBuilder b = new ScriptBuilder();
        try {
            Script v = b.build(new File(RC_ROOT + "logical.btrp"));
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

    /*
    public void testComplex() {
        VJobElementBuilder e = defaultEb;
        Configuration cfg = new SimpleConfiguration();
        e.useConfiguration(cfg);
        for (int i = 1; i <= 200; i++) {
            cfg.addOnline(new SimpleNode("node-" + i, 5, 5, 5));
        }
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new SpreadBuilder());
        c.add(new LonelyBuilder());
        c.add(new AmongBuilder());
        c.add(new FenceBuilder());
        c.add(new BanBuilder());
        ScriptBuilder b = new ScriptBuilder(e, c);
        PathBasedIncludes includes = new PathBasedIncludes(b, new File(RC_ROOT + "env"));
        b.setIncludes(includes);
        try {
            Script v = b.build(new File(RC_ROOT + "env/sysadmin.btrp"));
            System.err.println(v);
            BtrpNumber card = (BtrpNumber) v.getExported("$card");
            Assert.assertEquals(card.getIntValue(), 30);

        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }
                 */
    public void testExportWithValidRestrictions() {
        ScriptBuilder b = new ScriptBuilder();
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
        ScriptBuilder b = new ScriptBuilder();
        try {
            Script v = b.build("namespace foo; VM[1..5] : tiny;\nVM[6..10] : small;\n lonely($me); ");
            SatConstraint cs = v.getConstraints().iterator().next();
            Assert.assertEquals(cs.getInvolvedVMs().size(), 10);
        } catch (Exception x) {
            Assert.fail(x.getMessage(), x);
        }
    }

    @Test(expectedExceptions = {ScriptBuilderException.class})
    public void testMeReassignment() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        b.build("namespace foo; VM[1..5] : tiny;\nVM[6..10] : small;\n $me = 7; ");
    }

    public void testStringSupport() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        Script v = b.build("namespace foo; VM[1..10] : tiny;\n$arr = {\"foo\",\"bar\", \"baz\"};$arr2 = $arr + {\"git\"}; $out = \"come \" + \"out \" + 5 + \" \" + VM1; export $arr2,$out to *;");
        BtrpString out = (BtrpString) v.getExported("$out");
        BtrpSet arr2 = (BtrpSet) v.getExported("$arr2");
        Assert.assertEquals(out.toString(), "come out 5 foo.VM1");
        Assert.assertEquals(arr2.size(), 4);
    }

    public void testLargeRange() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        Script v = b.build("namespace foo; @N[1..500] : defaultNode;\n$all = @N[251..500]; export $all to *;");
        BtrpSet out = (BtrpSet) v.getExported("$all");
        Assert.assertEquals(out.size(), 250);
    }

    public void testDependencies() {
        //
        // a
        // |- b
        // \– c
        //    \-d
        //
        ScriptBuilder b = new ScriptBuilder();
        PathBasedIncludes includes = new PathBasedIncludes(b, new File(RC_ROOT + "deps"));
        b.setIncludes(includes);
        try {
            Script v = b.build(new File(RC_ROOT + "deps/a.btrp"));
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


    public void testVariablesInElementRange() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        Script v = b.build(new File(RC_ROOT + "range.btrp"));
        BtrpSet s = (BtrpSet) v.getExported("$foo");

        System.out.println(s);
        Assert.assertEquals(s.size(), 9);
    }

    @Test(expectedExceptions = {ScriptBuilderException.class})
    public void testConstraintWithBadParameters() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        b.build("namespace foo; VM[1..10] : tiny;\nlonely(N15);");
    }

    @Test(expectedExceptions = {ScriptBuilderException.class})
    public void testWithLexerErrors() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        b.build("namespace foo; VM[1..10] : tiny;\nroot(VM10;");
    }

    public void testMissingEndl() throws ScriptBuilderException {
        ScriptBuilder b = new ScriptBuilder();
        ErrorReporter r = null;
        try {
            b.build("namespace foo; VM[1..10] : tiny;\nroot(VM10);root(VM9");
        } catch (ScriptBuilderException ex) {
            System.out.println(ex);
            r = ex.getErrorReporter();
            Assert.assertEquals(r.getErrors().size(), 1);
            Assert.assertEquals(r.getErrors().get(0).lineNo, 2);
            Assert.assertTrue(r.getErrors().get(0).colNo > 10);
        }
        Assert.assertNotNull(r);
    }
}
