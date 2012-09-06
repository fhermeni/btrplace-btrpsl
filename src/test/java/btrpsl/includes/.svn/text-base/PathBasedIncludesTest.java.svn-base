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

package btrpsl.includes;

import btrpsl.BtrPlaceVJob;
import btrpsl.BtrPlaceVJobBuilder;
import btrpsl.constraint.CapacityBuilder;
import btrpsl.constraint.DefaultConstraintsCatalog;
import entropy.configuration.Configuration;
import entropy.configuration.SimpleConfiguration;
import entropy.configuration.SimpleNode;
import entropy.vjob.builder.DefaultVJobElementBuilder;
import entropy.vjob.builder.VJobElementBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

/**
 * @author Fabien Hermenier
 */
@Test
public class PathBasedIncludesTest {

    private static BtrPlaceVJobBuilder makeVJobBuilder() {
        VJobElementBuilder e = new DefaultVJobElementBuilder(null);
        Configuration cfg = new SimpleConfiguration();
        for (int i = 0; i < 100; i++) {
            cfg.addOnline(new SimpleNode("sol-" + i + ".sophia.grid5000.fr", 1, 1, 1));
            cfg.addOnline(new SimpleNode("helios-" + i + ".sophia.grid5000.fr", 1, 1, 1));
            cfg.addOnline(new SimpleNode("paravent-" + i + ".rennes.grid5000.fr", 1, 1, 1));
            cfg.addOnline(new SimpleNode("parapide-" + i + ".rennes.grid5000.fr", 1, 1, 1));

        }
        cfg.addOnline(new SimpleNode("frontend.sophia.grid5000.fr", 1, 1, 1));
        cfg.addOnline(new SimpleNode("frontend.rennes.grid5000.fr", 1, 1, 1));
        e.useConfiguration(cfg);
        DefaultConstraintsCatalog c = new DefaultConstraintsCatalog();
        c.add(new CapacityBuilder());
        return new BtrPlaceVJobBuilder(e, c);
    }

    public void testBasic() {
        BtrPlaceVJobBuilder b = makeVJobBuilder();
        PathBasedIncludes incs = new PathBasedIncludes(b, new File("src/test/resources/btrpsl/includes/i1"));
        b.setIncludes(incs);
        try {
            List<BtrPlaceVJob> res = incs.getVJob("sophia.sol");
            Assert.assertNotNull(res);
            Assert.assertEquals(res.size(), 1);

            res = incs.getVJob("sophia.helios");
            Assert.assertNotNull(res);
            Assert.assertEquals(res.size(), 1);


            res = incs.getVJob("sophia");
            Assert.assertNotNull(res);
            Assert.assertEquals(res.size(), 1);

            Assert.assertTrue(incs.addPath(new File("src/test/resources/btrpsl/includes/i2")));
            Assert.assertNotNull(incs.getVJob("rennes"));
            Assert.assertNotNull(incs.getVJob("rennes.parapide"));
            Assert.assertNotNull(incs.getVJob("rennes.paravent"));
        } catch (Exception e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    public void testCreationFromPaths() {
        try {
            BtrPlaceVJobBuilder b = makeVJobBuilder();
            PathBasedIncludes incs = PathBasedIncludes.fromPaths(b, "src/test/resources/btrpsl/includes/i1:src/test/resources/btrpsl/includes/i2");
            b.setIncludes(incs);
            List<File> path = incs.getPaths();
            Assert.assertEquals(path.size(), 2);
            Assert.assertEquals(path.get(0).getPath(), "src/test/resources/btrpsl/includes/i1");
            Assert.assertEquals(path.get(1).getPath(), "src/test/resources/btrpsl/includes/i2");
            Assert.assertNotNull(incs.getVJob("rennes"));
            Assert.assertNotNull(incs.getVJob("rennes.parapide"));
            Assert.assertNotNull(incs.getVJob("rennes.paravent"));

            Assert.assertNotNull(incs.getVJob("sophia"));
            Assert.assertNotNull(incs.getVJob("sophia.helios"));
            Assert.assertNotNull(incs.getVJob("sophia.sol"));

            Assert.assertNotNull(incs.toString());
        } catch (Exception e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    public void testWildcard() {
        try {
            BtrPlaceVJobBuilder b = makeVJobBuilder();
            PathBasedIncludes incs = PathBasedIncludes.fromPaths(b, "src/test/resources/btrpsl/");
            b.setIncludes(incs);
            List<BtrPlaceVJob> res = incs.getVJob("includes.*");
            Assert.assertEquals(res.size(), 2);
            //System.out.println(res);
        } catch (Exception e) {
            Assert.fail(e.getMessage(), e);
        }
    }
}
