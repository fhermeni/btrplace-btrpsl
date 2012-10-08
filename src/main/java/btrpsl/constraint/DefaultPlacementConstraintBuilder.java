package btrpsl.constraint;

import btrpsl.element.BtrpOperand;
import btrpsl.element.DefaultBtrpOperand;
import btrpsl.element.IgnorableOperand;
import btrpsl.tree.BtrPlaceTree;

import java.util.Collection;
import java.util.List;

/**
 * A toolkit class to ease the implementation of {@link PlacementConstraintBuilder}.
 *
 * @author Fabien Hermenier
 */
public abstract class DefaultPlacementConstraintBuilder implements PlacementConstraintBuilder {


    protected final ConstraintParam[] params;

    public DefaultPlacementConstraintBuilder(ConstraintParam [] ps) {
        params = ps;
    }

    @Override
    public ConstraintParam[] getParameters() {
        return params;
    }

    @Override
    public String getSignature() {
        StringBuilder b = new StringBuilder();
        b.append(getIdentifier()).append('(');
        for (int i = 0; i < params.length; i++) {
            b.append(params[i].prettySignature());
            if (i != params.length - 1) {
                b.append(", ");
            }
        }
        b.append(')');
        return b.toString();
    }

    @Override
    public String getFullSignature() {
        StringBuilder b = new StringBuilder();
        b.append(getIdentifier()).append('(');
        for (int i = 0; i < params.length; i++) {
            b.append(params[i].fullSignature());
            if (i != params.length - 1) {
                b.append(", ");
            }
        }
        b.append(')');
        return b.toString();
    }

    public boolean checkConformance(BtrPlaceTree t, List<BtrpOperand> ops) {
        //Arity error
        if (ops.size() != getParameters().length) {
            t.ignoreError("'" + getSignature() + "' cannot be casted to " + pretty(ops) + "'");
            return false;
        }

        //Type checking
        for (int i = 0; i < ops.size(); i++) {
            BtrpOperand o = ops.get(i);
            ConstraintParam p = params[i];
            if (!p.isCompatibleWith(t, o)) {
                if (o != IgnorableOperand.getInstance()) {
                    t.ignoreError("'" + getSignature() + "' cannot be casted to '" + pretty(ops) + "'");
                }
                return false;
            }
        }
        return true;
    }

    private String pretty(List<BtrpOperand> ops) {
        StringBuilder b = new StringBuilder();
        b.append(getIdentifier()).append('(');
        for (int i = 0; i < ops.size(); i++) {
            b.append(DefaultBtrpOperand.prettyType(ops.get(i)));
            if (i != ops.size() - 1) {
                b.append(", ");
            }
        }
        b.append(")");
        return b.toString();
    }
}
