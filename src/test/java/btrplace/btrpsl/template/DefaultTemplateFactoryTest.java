/*
 * Copyright (c) 2013 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
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

package btrplace.btrpsl.template;

import btrplace.btrpsl.InMemoryNamingService;
import btrplace.btrpsl.NamingService;
import btrplace.btrpsl.Script;
import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.model.DefaultModel;
import btrplace.model.Model;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for {@link DefaultTemplateFactory}.
 *
 * @author Fabien Hermenier
 */
public class DefaultTemplateFactoryTest {

    public static class MockVMTemplate implements Template {

        NamingService srv;
        String tplName;

        @Override
        public BtrpOperand.Type getElementType() {
            return BtrpOperand.Type.VM;
        }

        public MockVMTemplate(String n) {
            tplName = n;
        }

        @Override
        public BtrpElement build(Script scr, String name, Map<String, String> options) throws ElementBuilderException {
            Model mo = new DefaultModel();
            BtrpElement el = new BtrpElement(getElementType(), name, mo.newVM());
            scr.getAttributes().put(el.getElement(), "template", getIdentifier());
            return el;
        }

        @Override
        public String getIdentifier() {
            return tplName;
        }

        @Override
        public void setNamingService(NamingService srv) {
            this.srv = srv;

        }
    }

    public static class MockNodeTemplate implements Template {

        String tplName;

        private NamingService srv;

        @Override
        public BtrpOperand.Type getElementType() {
            return BtrpOperand.Type.node;
        }

        public MockNodeTemplate(String n) {
            tplName = n;
        }

        @Override
        public BtrpElement build(Script scr, String name, Map<String, String> options) throws ElementBuilderException {
            Model mo = new DefaultModel();
            BtrpElement el = new BtrpElement(getElementType(), name, mo.newVM());
            scr.getAttributes().put(el.getElement(), "template", getIdentifier());
            return el;
        }

        @Override
        public String getIdentifier() {
            return tplName;
        }

        @Override
        public void setNamingService(NamingService srv) {
            this.srv = srv;
        }
    }

    @Test
    public void testInstantiation() {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new DefaultModel()));
        Assert.assertFalse(tplf.isStrict());
        Assert.assertTrue(tplf.getAvailables().isEmpty());
    }

    @Test(dependsOnMethods = {"testInstantiation"})
    public void testRegister() {
        NamingService srv = new InMemoryNamingService(new DefaultModel());
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(srv);
        MockVMTemplate t1 = new MockVMTemplate("mock1");
        Assert.assertNull(tplf.register(t1));
        Assert.assertEquals(t1.srv, srv);
        Assert.assertTrue(tplf.getAvailables().contains("mock1"));
        MockVMTemplate t2 = new MockVMTemplate("mock2");
        Assert.assertNull(tplf.register(t2));
        Assert.assertEquals(t2.srv, srv);
        Assert.assertTrue(tplf.getAvailables().contains("mock2") && tplf.getAvailables().size() == 2);

    }

    @Test(dependsOnMethods = {"testInstantiation", "testRegister"})
    public void testAccessibleWithStrict() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new DefaultModel()));
        tplf.register(new MockVMTemplate("mock1"));
        Script scr = new Script();
        BtrpElement el = tplf.build(scr, "mock1", "foo", new HashMap<String, String>());
        Assert.assertEquals(scr.getAttributes().get(el.getElement(), "template"), "mock1");
    }

    @Test(dependsOnMethods = {"testInstantiation", "testRegister"}, expectedExceptions = {ElementBuilderException.class})
    public void testInaccessibleWithStrict() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new DefaultModel()), true);
        Script scr = new Script();
        tplf.build(scr, "bar", "foo", new HashMap<String, String>());
    }

    @Test(dependsOnMethods = {"testInstantiation", "testRegister"})
    public void testAccessibleWithoutStrict() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new DefaultModel()), false);
        tplf.register(new MockVMTemplate("mock1"));
        Script scr = new Script();
        BtrpElement el = tplf.build(scr, "mock1", "foo", new HashMap<String, String>());
        Assert.assertEquals(scr.getAttributes().get(el.getElement(), "template"), "mock1");
    }

    @Test(dependsOnMethods = {"testInstantiation", "testRegister"})
    public void testInaccessibleWithoutStrict() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new DefaultModel()), false);
        Map<String, String> m = new HashMap<>();
        m.put("migratable", null);
        m.put("foo", "7.5");
        m.put("bar", "1243");
        m.put("template", "bar");
        Script scr = new Script();
        BtrpElement el = tplf.build(scr, "bar", "foo", m);
        Assert.assertEquals(scr.getAttributes().get(el.getElement(), "template"), "bar");
        Assert.assertEquals(el.getName(), "foo");
        Assert.assertTrue(scr.getAttributes().getBoolean(el.getElement(), "migratable"));
        Assert.assertEquals(scr.getAttributes().getInteger(el.getElement(), "bar").longValue(), 1243);
        Assert.assertEquals(scr.getAttributes().getDouble(el.getElement(), "foo"), 7.5);
        Assert.assertEquals(scr.getAttributes().getKeys(el.getElement()), m.keySet());
    }

    @Test(expectedExceptions = {ElementBuilderException.class})
    public void testTypingIssue() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new DefaultModel()), true);
        tplf.register(new MockVMTemplate("mock1"));
        Script scr = new Script();
        tplf.build(scr, "mock1", "@foo", new HashMap<String, String>());
    }

}
