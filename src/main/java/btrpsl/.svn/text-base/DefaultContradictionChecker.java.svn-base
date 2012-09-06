package btrpsl;

import choco.kernel.common.logging.ChocoLogging;
import choco.kernel.common.logging.Verbosity;
import entropy.configuration.*;
import entropy.plan.PlanException;
import entropy.plan.SolutionStatistics;
import entropy.plan.TimedReconfigurationPlan;
import entropy.plan.choco.ChocoCustomRP;
import entropy.plan.choco.constraint.pack.SatisfyDemandingSlicesHeightsFastBP;
import entropy.plan.durationEvaluator.DurationEvaluator;
import entropy.plan.durationEvaluator.MockDurationEvaluator;
import entropy.vjob.VJob;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple checker.
 * Customize a RP with every constraint in the vjob and its parents and try
 * to find a solution.
 * <p/>
 * The checker build a configuration containing every node and virtual machines
 * in the vjobs and every constraints. It then try to compute a placement with
 * all the VMs running, and all the nodes online.
 * <p/>
 * It is not ensured to have a configuration that is fully compatible with every possible
 * constraints. So further refinements should be great.
 *
 * @author Fabien Hermenier
 */
public class DefaultContradictionChecker implements ContradictionChecker {

    private int timeout;

    private DurationEvaluator durEval;

    private ChocoCustomRP rp;

    private static ManagedElementSet<VirtualMachine> emptyVMs = new SimpleManagedElementSet<VirtualMachine>();

    private static ManagedElementSet<Node> emptyNodes = new SimpleManagedElementSet<Node>();

    public DefaultContradictionChecker() {
        ChocoLogging.setVerbosity(Verbosity.SILENT);
        this.durEval = new MockDurationEvaluator(1, 2, 3, 4, 5, 6, 7, 8, 9);
        rp = new ChocoCustomRP(durEval);
        rp.setPackingConstraintClass(new SatisfyDemandingSlicesHeightsFastBP());
        rp.setRepairMode(true);
    }

    public void setTimeout(int t) {
        this.timeout = t;
    }

    public int getTimeout() {
        return this.timeout;
    }

    @Override
    public boolean check(BtrPlaceVJob vjob) {
        List<VJob> vjobs = new ArrayList<VJob>();
        fulfill(vjobs, vjob);

        Configuration cfg = makeMinimalConfiguration(vjobs);

        try {
            TimedReconfigurationPlan plan = rp.compute(cfg, cfg.getAllVirtualMachines(), emptyVMs, emptyVMs, emptyVMs, cfg.getAllNodes(), emptyNodes, vjobs);
            List<SolutionStatistics> stats = rp.getSolutionsStatistics();
            return (!stats.isEmpty());
        } catch (PlanException ex) {
            return false;
        }
    }

    private Configuration makeMinimalConfiguration(List<VJob> vjobs) {
        Configuration cfg = new SimpleConfiguration();
        for (VJob v : vjobs) {
            for (Node n : v.getNodes()) {
                cfg.addOnline(n);
            }

            for (VirtualMachine vm : v.getVirtualMachines()) {
                cfg.addWaiting(vm);
            }
        }
        //Just in case, we add 2* nbVMs nodes to have free space for VMs involved in relative
        //placement constraints
        /*for (int i = 0; i < cfg.getAllVirtualMachines().size() * 2; i++) {
            Node n = new SimpleNode("N" + System.nanoTime(), 1000, 1000, 10000);
            cfg.addOnline(n);
        } */
        return cfg;
    }

    private void fulfill(List<VJob> vjobs, BtrPlaceVJob v) {
        vjobs.add(v);
        for (BtrPlaceVJob parent : v.getDependencies()) {
            fulfill(vjobs, parent);
        }
    }
}
