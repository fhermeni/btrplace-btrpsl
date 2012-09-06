package btrpsl;

import entropy.configuration.*;
import entropy.vjob.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for {@link DefaultContradictionChecker}.
 *
 * @author Fabien Hermenier
 */
@Test
public class DefaultContradictionCheckerTest {

    public void testGoodWithNoDeps() {
        BtrPlaceVJob vjob = new BtrPlaceVJob();

        ManagedElementSet[] tx = new ManagedElementSet[3];
        tx[0] = new SimpleManagedElementSet<VirtualMachine>();
        tx[1] = new SimpleManagedElementSet<VirtualMachine>();
        tx[2] = new SimpleManagedElementSet<VirtualMachine>();

        ManagedElementSet<Node> g1 = new SimpleManagedElementSet<Node>();
        ManagedElementSet<Node> g2 = new SimpleManagedElementSet<Node>();
        for (int i = 0; i < 10; i++) {
            tx[0].add(new SimpleVirtualMachine("T0-" + i, 5, 5, 5));
            tx[1].add(new SimpleVirtualMachine("T1-" + i, 5, 5, 5));
            tx[2].add(new SimpleVirtualMachine("T2-" + i, 5, 5, 5));
        }

        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                g1.add(new SimpleNode("N" + i, 10, 10, 10));
            } else {
                g2.add(new SimpleNode("N" + i, 10, 10, 10));
            }
        }
        Set<ManagedElementSet<Node>> grps = new HashSet<ManagedElementSet<Node>>();
        grps.add(g1);
        grps.add(g2);
        vjob.addConstraint(new ContinuousSpread(tx[0]));
        vjob.addConstraint(new ContinuousSpread(tx[1]));
        vjob.addConstraint(new ContinuousSpread(tx[2]));
        vjob.addConstraint(new Lonely(tx[0]));
        vjob.addConstraint(new Among(tx[2], grps));
        //vjob.addConstraint(new Root(tx[2]));
        System.out.println(vjob);
        DefaultContradictionChecker chk = new DefaultContradictionChecker();
        Assert.assertTrue(chk.check(vjob));
    }

    public void testBadNoDepsWithNodes() {
        BtrPlaceVJob vjob = new BtrPlaceVJob();
        ManagedElementSet<VirtualMachine> vms = new SimpleManagedElementSet<VirtualMachine>();

        ManagedElementSet<Node> g1 = new SimpleManagedElementSet<Node>();
        ManagedElementSet<Node> g2 = new SimpleManagedElementSet<Node>();
        for (int i = 0; i < 500; i++) {
            vms.add(new SimpleVirtualMachine("T0-" + i, 5, 5, 5));
        }

        Set<ManagedElementSet<Node>> grps = new HashSet<ManagedElementSet<Node>>();
        grps.add(g1);
        grps.add(g2);
        for (int i = 0; i < 500; i++) {
            if (i % 2 == 0) {
                g1.add(new SimpleNode("N" + i, 20, 20, 20));
            } else {
                g2.add(new SimpleNode("N" + i, 10, 10, 10));
            }
        }
        vjob.addConstraint(new Capacity(g1, 100));
        //System.out.println(vjob);
        DefaultContradictionChecker chk = new DefaultContradictionChecker();
        Assert.assertTrue(chk.check(vjob));
    }

    public void testBadWithNoDeps() {
        BtrPlaceVJob vjob = new BtrPlaceVJob();
        ManagedElementSet<VirtualMachine> vms = new SimpleManagedElementSet<VirtualMachine>();

        for (int i = 0; i < 10; i++) {
            vms.add(new SimpleVirtualMachine("T0-" + i, 5, 5, 5));
        }

        vjob.addConstraint(new ContinuousSpread(vms));
        vjob.addConstraint(new Gather(vms));
        //System.out.println(vjob);
        DefaultContradictionChecker chk = new DefaultContradictionChecker();
        Assert.assertFalse(chk.check(vjob));
    }
}
