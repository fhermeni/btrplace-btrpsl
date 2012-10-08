package btrpsl.constraint;

import btrpsl.element.BtrpOperand;
import btrpsl.tree.BtrPlaceTree;

/**
 * A parameter for a constraint.
 * @author Fabien Hermenier
 */
public interface ConstraintParam<E> {

    /**
     * Get the signature of the parameter without the parameter name.
     * @return a non-empty string
     */
    String prettySignature();

    /**
     * Get the signature of the parameter including the parameter name.
     * @return a non-empty string
     */
    String fullSignature();

    /**
     * Transform an operand into the right btrplace parameter.
     *
     * @param tree the tree use to propagate errors
     * @param op the operand to transform
     * @return the transformed parameter.
     */
    E transform(BtrPlaceTree tree, BtrpOperand op);

    /**
     * Check if a given operand is compatible with this parameter.
     * @param t the tree used to propagate errors
     * @param o the operand to test
     * @return {@code true} if the operand is compatible
     */
    boolean isCompatibleWith(BtrPlaceTree t, BtrpOperand o);

    /**
     * Get the parameter name.
     * @return a non-empty string
     */
    String getName();
}
