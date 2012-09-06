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

import btrpsl.platform.PlatformFactoryStub;
import btrpsl.template.VirtualMachineTemplateFactoryStub;
import entropy.configuration.Configuration;
import entropy.configuration.Node;
import entropy.configuration.SimpleConfiguration;
import entropy.configuration.SimpleNode;
import entropy.vjob.VJob;
import entropy.vjob.builder.DefaultVJobElementBuilder;
import entropy.vjob.builder.VJobBuilderFactory;
import entropy.vjob.builder.VJobBuilderFactoryBuilderFromProperties;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Unit test to check BtrPlaceVJobBuilderBuilder
 *
 * @author Fabien Hermenier
 */
@Test(groups = {"unit"})
public class BtrPlaceVJobBuilderBuilderTest {

    /**
     * Test the instantiation of the builder.
     */
    public void testBuilder() {
        BtrPlaceVJobBuilderBuilder b = new BtrPlaceVJobBuilderBuilder("src/test/resources/btrpsl/btrpVjobs.properties");
        try {
            BtrPlaceVJobBuilder vb = b.build(new MockVJobElementBuilder(new VirtualMachineTemplateFactoryStub(), new PlatformFactoryStub()));
            VJob v = vb.build(new File("src/test/resources/btrpsl/vapp.btrp"));
        } catch (Exception e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    public void testEntropyIntegration() throws Exception {

        VJobBuilderFactoryBuilderFromProperties vbb = new VJobBuilderFactoryBuilderFromProperties("src/test/resources/btrpsl/vjobsBuilder.properties");
        VJobBuilderFactory f = vbb.build();
        f.setVJobElementBuilder(new DefaultVJobElementBuilder(new VirtualMachineTemplateFactoryStub()));
        Configuration cfg = new SimpleConfiguration();
        for (int i = 1; i <= 56; i++) {
            Node n = new SimpleNode("helios-" + i + ".sophia.grid5000.fr");
            cfg.addOnline(n);
        }
        f.useConfiguration(cfg);
        VJob v = f.build("src/test/resources/btrpsl/testEntropy.btrp");
        System.err.println(v);
    }
}
