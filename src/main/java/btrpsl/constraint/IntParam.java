package btrpsl.constraint;

import btrpsl.element.BtrpNumber;
import btrpsl.element.BtrpOperand;
import btrpsl.element.IgnorableOperand;
import btrpsl.tree.BtrPlaceTree;

/**
 *  A parameter for a constraint that denotes an integer.
 *  @author Fabien Hermenier
 */
public class IntParam implements ConstraintParam<Integer> {

    private String name;

    public IntParam(String n) {
        this.name = n;
    }

    @Override
    public String prettySignature() {
        return "number";
    }

    @Override
    public String fullSignature() {
        return name + ": number";
    }

    @Override
    public Integer transform(PlacementConstraintBuilder cb, BtrPlaceTree tree, BtrpOperand op) {
        if (op == IgnorableOperand.getInstance()) {
            throw new UnsupportedOperationException();
        }
        return ((BtrpNumber) op).getIntValue();

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompatibleWith(BtrPlaceTree t, BtrpOperand o) {
        return o == IgnorableOperand.getInstance() || (o.type() == BtrpOperand.Type.number && o.degree() == 0);
    }
}
