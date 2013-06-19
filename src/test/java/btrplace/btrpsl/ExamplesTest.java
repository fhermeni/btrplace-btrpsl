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

package btrplace.btrpsl;

import btrplace.model.VM;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Unit tests that check the examples are working.
 *
 * @author Fabien Hermenier
 */
public class ExamplesTest {

    @Test
    public void testHelloVWorld() throws Exception {
        ScriptBuilder sBuilder = new ScriptBuilder();
        Script scr = sBuilder.build(new File("src/main/examples/helloVWorld.btrp"));

        //Basic checker
        Assert.assertEquals(scr.getVMs().size(), 4);
        for (VM vm : scr.getVMs()) {
            Assert.assertEquals(scr.getAttributes().get(vm, "template"), "tiny");
        }
        Assert.assertEquals(scr.getConstraints().size(), 2);
    }

    @Test
    public void testMultipleAssignment() throws Exception {
        ScriptBuilder sBuilder = new ScriptBuilder();
        Script scr = sBuilder.build(new File("src/main/examples/multipleAssignment.btrp"));
        //Basic checker
        Assert.assertEquals(scr.getVMs().size(), 6);
        for (VM vm : scr.getVMs()) {
            Assert.assertEquals(scr.getAttributes().get(vm, "template"), "tinyInstance");
        }
        Assert.assertEquals(scr.getConstraints().size(), 0);
        Assert.assertEquals(scr.getExported().size(), 7);

    }
}

