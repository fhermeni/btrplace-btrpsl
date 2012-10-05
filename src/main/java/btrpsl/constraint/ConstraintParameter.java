package btrpsl.constraint;

import btrpsl.element.BtrpOperand;
import btrpsl.element.DefaultBtrpOperand;
import btrpsl.element.IgnorableOperand;

/**
 * A constraint parameter;
 *
 * @author Fabien Hermenier
 */
public class ConstraintParameter {

    private BtrpOperand.Type type;

    private int degree;

    private String varName;

    public ConstraintParameter(BtrpOperand.Type t, int d, String name) {
        this.type = t;
        this.degree = d;
        this.varName = name;
    }

    public boolean compatibleWith(BtrpOperand o) {
        if (o == IgnorableOperand.getInstance() || o.type() != type || (o.degree() != degree && !(degree == 1 && o.degree() == 0))) {
            return false;
        }
        return true;
    }

    /**
     * Get the literal type.
     *
     * @return the type
     */
    public BtrpOperand.Type getType() {
        return type;
    }

    /**
     * Get the degree of the parameter. 0 for a literal.
     *
     * @return a positive integer
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Get the variable name.
     *
     * @return a non-empty String
     */
    public String getVarName() {
        return varName;
    }

    @Override
    public String toString() {
        return new StringBuilder(varName).append(" : ").append(DefaultBtrpOperand.prettyType(degree, type)).toString();
    }
}
