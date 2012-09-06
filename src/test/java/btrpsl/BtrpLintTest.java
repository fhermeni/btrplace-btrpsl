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

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.security.Permission;

/**
 * Unit test for BtrpLint.
 *
 * @author Fabien Hermenier
 */
@Test(sequential = true)
public class BtrpLintTest {

    protected static class ExitException extends SecurityException {
        public final int status;

        public ExitException(int status) {
            super("No escape ? Yep, on purpose");
            this.status = status;
        }
    }

    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            // allow anything.
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
            // allow anything.
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status);
            throw new ExitException(status);
        }
    }

    @BeforeClass
    protected void setUp() throws Exception {
        System.setSecurityManager(new NoExitSecurityManager());
    }

    @AfterClass
    protected void tearDown() throws Exception {
        System.setSecurityManager(null); // or save and restore original
    }

    @DataProvider(name = "providerGoods")
    public Object[][] provideGoods() {
        return new Object[][]{
                {"./", "config/catalog.properties", "src/main/examples/helloVWorld.btrp", ".xml"},
                {"./src/main/examples/includes", "config/catalog.properties", "src/main/examples/vappHA.btrp", ".xml"},
                {"./src/main/examples/advanced", "config/catalog.properties", "src/main/examples/advanced/admin/sysadmin.btrp", ".pbd"}
        };
    }

    @Test(dataProvider = "providerGoods")
    public void testGoods(String incl, String cat, String script, String ext) {
        try {
            File out = File.createTempFile("btrp", ext);
            BtrpLint.main(new String[]{"valid", "-o", out.getPath(), "-I", incl, "-cat", cat, script});

            Assert.assertTrue(out.delete());
        } catch (ExitException e) {
            Assert.assertEquals(e.status, 0);
        } catch (IOException e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    @Test(dataProvider = "providerGoods")
    public void testGoodChecks(String incl, String cat, String script, String ext) {
        try {
            BtrpLint.main(new String[]{"check", "-I", incl, "-cat", cat, script});
        } catch (ExitException e) {
            Assert.assertEquals(e.status, 0);
        }
    }

    public void testWithGoodTemplate() {
        try {
            BtrpLint.main(new String[]{"valid", "-v", "-v", "-tpl", "src/test/resources/btrpsl/templates.properties", "-cat", "./config/catalog.properties", "src/test/resources/btrpsl/testTinyTemplate.btrp"});
        } catch (ExitException e) {
            Assert.assertEquals(e.status, 0);
        }
    }

    public void testWithBadTemplate() {
        try {
            BtrpLint.main(new String[]{"valid", "-tpl", "src/test/resources/btrpsl/templates.properties", "-cat", "./config/catalog.properties", "src/test/resources/btrpsl/testMacroTemplate.btrp"});
        } catch (ExitException e) {
            Assert.assertEquals(e.status, 1);
        }
    }

    /**
     * Test print dependencies with a fine script
     */
    public void testHelp() {
        try {
            BtrpLint.main(new String[]{"-h"});
        } catch (ExitException e) {
            Assert.assertEquals(e.status, 0);
        }
    }
}
