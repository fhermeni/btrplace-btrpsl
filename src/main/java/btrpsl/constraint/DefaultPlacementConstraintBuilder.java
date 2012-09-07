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

    @Override
    public String getSignature() {
        StringBuilder b = new StringBuilder();
        b.append(getIdentifier()).append("(");
        ConstraintParameter[] ps = getParameters();
        for (int i = 0; i < ps.length; i++) {
            b.append(ps[i].toString());
            if (i != ps.length - 1) {
                b.append(", ");
            }
        }
        b.append(")");
        return b.toString();
    }

    /**
     * Check the set is not empty.
     *
     * @param buf a message that will prefix the exception
     * @param s   the set
     */
    public static boolean noEmptySets(BtrPlaceTree t, BtrpOperand buf, Collection s) {
        if (s.isEmpty()) {
            t.ignoreError(buf.toString() + " is an empty set ");
            return false;
        }
        return true;
    }

    public boolean checkConformance(BtrPlaceTree t, List<BtrpOperand> ops) {
        //Arity error
        if (ops.size() != getParameters().length) {
            t.ignoreError("'" + getSignature() + "' cannot be applied to " + pretty(ops));
            return false;
        }

        //Type checking
        for (int i = 0; i < ops.size(); i++) {
            BtrpOperand o = ops.get(i);
            ConstraintParameter p = getParameters()[i];
            if (!p.compatibleWith(o)) {
                t.ignoreError("'" + getSignature() + "' cannot be applied to " + pretty(ops));
                return false;
            }
        }
        return true;
    }

    private String pretty(List<BtrpOperand> ops) {
        StringBuilder b = new StringBuilder();
        b.append("(");
        for (int i = 0; i < ops.size(); i++) {
            b.append(DefaultBtrpOperand.prettyType(ops.get(i)));
            if (i != ops.size() - 1) {
                b.append(", ");
            }
        }
        b.append(")");
        return b.toString();
    }

    /**
     * Chech the set is not empty.
     *
     * @param buf a message that will prefix the exception
     * @param s   the set
     */
    public boolean minCardinality(BtrPlaceTree t, BtrpOperand buf, Collection s, int n) {
        if (buf != IgnorableOperand.getInstance() && s != null && s.size() < n) {
            t.ignoreError(new StringBuilder(getSignature()).append(" must be composed by at least '").append(n).append("' element(s)").toString());
            return false;
        }
        return true;
    }
}
