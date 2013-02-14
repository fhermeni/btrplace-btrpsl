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

package btrplace.btrpsl.template;

import btrplace.btrpsl.InMemoryNamingService;
import btrplace.btrpsl.InMemoryUUIDPool;
import btrplace.btrpsl.Script;
import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Unit tests for {@link DefaultTemplateFactory}.
 *
 * @author Fabien Hermenier
 */
public class DefaultTemplateFactoryTest {

    public static class MockVMTemplate implements Template {

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
            BtrpElement el = new BtrpElement(getElementType(), name, UUID.randomUUID());
            el.setTemplate(getIdentifier());
            return el;
        }

        @Override
        public String getIdentifier() {
            return tplName;
        }
    }

    public static class MockNodeTemplate implements Template {

        String tplName;

        @Override
        public BtrpOperand.Type getElementType() {
            return BtrpOperand.Type.node;
        }

        public MockNodeTemplate(String n) {
            tplName = n;
        }

        @Override
        public BtrpElement build(Script scr, String name, Map<String, String> options) throws ElementBuilderException {
            BtrpElement el = new BtrpElement(getElementType(), name, UUID.randomUUID());
            el.setTemplate(getIdentifier());
            return el;
        }

        @Override
        public String getIdentifier() {
            return tplName;
        }
    }

    @Test
    public void testInstantiation() {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new InMemoryUUIDPool()));
        Assert.assertFalse(tplf.isStrict());
        Assert.assertTrue(tplf.getAvailables().isEmpty());
    }

    @Test(dependsOnMethods = {"testInstantiation"})
    public void testRegister() {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new InMemoryUUIDPool()));
        MockVMTemplate t1 = new MockVMTemplate("mock1");
        Assert.assertNull(tplf.register(t1));
        Assert.assertTrue(tplf.getAvailables().contains("mock1"));
        MockVMTemplate t2 = new MockVMTemplate("mock2");
        Assert.assertNull(tplf.register(t2));
        Assert.assertTrue(tplf.getAvailables().contains("mock2") && tplf.getAvailables().size() == 2);

    }

    @Test(dependsOnMethods = {"testInstantiation", "testRegister"})
    public void testAccessibleWithStrict() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new InMemoryUUIDPool()));
        tplf.register(new MockVMTemplate("mock1"));
        Script scr = new Script();
        BtrpElement el = tplf.build(scr, "mock1", "foo", new HashMap<String, String>());
        Assert.assertEquals(el.getTemplate(), "mock1");
    }

    @Test(dependsOnMethods = {"testInstantiation", "testRegister"}, expectedExceptions = {ElementBuilderException.class})
    public void testInaccessibleWithStrict() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new InMemoryUUIDPool()), true);
        Script scr = new Script();
        tplf.build(scr, "bar", "foo", new HashMap<String, String>());
    }

    @Test(dependsOnMethods = {"testInstantiation", "testRegister"})
    public void testAccessibleWithoutStrict() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new InMemoryUUIDPool()), false);
        tplf.register(new MockVMTemplate("mock1"));
        Script scr = new Script();
        BtrpElement el = tplf.build(scr, "mock1", "foo", new HashMap<String, String>());
        Assert.assertEquals(el.getTemplate(), "mock1");
    }

    @Test(dependsOnMethods = {"testInstantiation", "testRegister"})
    public void testInaccessibleWithoutStrict() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new InMemoryUUIDPool()), false);
        Map<String, String> m = new HashMap<String, String>();
        m.put("migratable", null);
        m.put("foo", "7.5");
        m.put("bar", "1243");
        Script scr = new Script();
        BtrpElement el = tplf.build(scr, "bar", "foo", m);
        Assert.assertEquals(el.getTemplate(), "bar");
        Assert.assertEquals(el.getElement(), "foo");
        Assert.assertTrue(scr.getAttributes().getBoolean(el.getUUID(), "migratable"));
        Assert.assertEquals(scr.getAttributes().getLong(el.getUUID(), "bar").longValue(), 1243);
        Assert.assertEquals(scr.getAttributes().getDouble(el.getUUID(), "foo"), 7.5);
        Assert.assertEquals(scr.getAttributes().getKeys(el.getUUID()), m.keySet());
    }

    @Test(expectedExceptions = {ElementBuilderException.class})
    public void testTypingIssue() throws ElementBuilderException {
        DefaultTemplateFactory tplf = new DefaultTemplateFactory(new InMemoryNamingService(new InMemoryUUIDPool()), true);
        tplf.register(new MockVMTemplate("mock1"));
        Script scr = new Script();
        tplf.build(scr, "mock1", "@foo", new HashMap<String, String>());
    }

}
