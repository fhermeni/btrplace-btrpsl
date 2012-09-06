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

package btrpsl.element;

import entropy.configuration.Node;
import entropy.configuration.SimpleNode;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link btrpsl.element.BtrpNode}
 *
 * @author Fabien Hermenier
 */
@Test
public class BtrpNodeTest {

    public void basic() {
        Node n = new SimpleNode("N1", 1, 1, 1);
        BtrpNode b = new BtrpNode(n);
        Assert.assertEquals(b.getElement(), n);
        Assert.assertEquals(b.toString(), n.getName());
        Assert.assertEquals(b.type(), BtrpOperand.Type.node);
        Assert.assertEquals(b.degree(), 0);
        Assert.assertEquals(b.clone(), b);
    }
}
