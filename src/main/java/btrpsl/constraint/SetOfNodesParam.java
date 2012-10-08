package btrpsl.constraint;

import btrpsl.element.BtrpNode;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.IgnorableOperand;
import btrpsl.tree.BtrPlaceTree;
import entropy.configuration.ManagedElementSet;
import entropy.configuration.Node;
import entropy.configuration.SimpleManagedElementSet;

/**
 *  A parameter for a constraint that denotes a set of nodes.
 *  @author Fabien Hermenier
 */
public class SetOfNodesParam implements ConstraintParam<ManagedElementSet<Node>>{

    private String name;

    private boolean canBeEmpty = true;

    public SetOfNodesParam(String n) {
        this(n, true);
    }

    public SetOfNodesParam(String n, boolean canBeEmpty) {
        this.name = n;
        this.canBeEmpty = canBeEmpty;
    }
    @Override
    public String prettySignature() {
        return "set<node>";
    }

    @Override
    public String fullSignature() {
        return name + ": set<node>";
    }

    @Override
    public ManagedElementSet<Node> transform(BtrPlaceTree tree, BtrpOperand op) {

        if (op == IgnorableOperand.getInstance()) { return null;}

        ManagedElementSet<Node> nodes = new SimpleManagedElementSet<Node>();
        if (op.degree() == 0) {
            nodes.add(((BtrpNode) op).getElement());
        } else {
            BtrpSet s = (BtrpSet) op;
            if (!canBeEmpty && s.size() == 0) {
                tree.ignoreError("Parameter '" +getName() + "' expects a non-empty set");
                return null;
            }
            for (BtrpOperand e : s.getValues()) {
                nodes.add(((BtrpNode) e).getElement());
            }
        }
        return nodes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompatibleWith(BtrPlaceTree t, BtrpOperand o) {
        return (o == IgnorableOperand.getInstance() || (o.type() == BtrpOperand.Type.node && o.degree() <= 1 ));
    }
}
