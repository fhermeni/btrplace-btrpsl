package btrpsl.constraint;

import btrpsl.element.*;
import btrpsl.tree.BtrPlaceTree;
import entropy.configuration.ManagedElementSet;
import entropy.configuration.Node;
import entropy.configuration.SimpleManagedElementSet;

import java.util.HashSet;
import java.util.Set;

/**
 *  A parameter for a constraint that denotes a set of nodes.
 *  @author Fabien Hermenier
 */
public class SetSetOfNodesParam implements ConstraintParam<Set<ManagedElementSet<Node>>>{

    private String name;

    private boolean canBeEmpty = true;

    private boolean inEmpty = true;

    public SetSetOfNodesParam(String n, boolean canBeEmpty, boolean inEmpty) {
        this.name = n;
        this.canBeEmpty = canBeEmpty;
        this.inEmpty = inEmpty;
    }


    public SetSetOfNodesParam(String n) {
        this(n, true, true);
    }
    @Override
    public String prettySignature() {
        return "set<set<node>>";
    }

    @Override
    public String fullSignature() {
        return name + ": set<set<node>>";
    }

    @Override
    public Set<ManagedElementSet<Node>> transform(BtrPlaceTree tree, BtrpOperand op) {
        if (op != IgnorableOperand.getInstance() && !(op.type() == BtrpOperand.Type.node && op.degree() == 2)) {
            throw new UnsupportedOperationException();
        }

        Set<ManagedElementSet<Node>> res = new HashSet<ManagedElementSet<Node>>();
        BtrpSet s = (BtrpSet)op;
        if (!canBeEmpty && s.size() == 0) {
            tree.ignoreError("Parameter '" +getName() + "' expects a non-empty set");
            return null;
        }
        for (BtrpOperand o : s.getValues()) {
            BtrpSet ss = (BtrpSet) o;
            if (!inEmpty && ss.size() == 0) {
                tree.ignoreError("Parameter '" +getName() + "' disallow empty sets");
                return null;
            }
            ManagedElementSet<Node> ns = new SimpleManagedElementSet<Node>();
            for (BtrpOperand e : ((BtrpSet) o).getValues()) {
                ns.add(((BtrpNode) e).getElement());
            }
            res.add(ns);
        }
        return res;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompatibleWith(BtrPlaceTree t, BtrpOperand o) {
        return o == IgnorableOperand.getInstance() || (o.type() == BtrpOperand.Type.node && o.degree() == 2);
    }
}
