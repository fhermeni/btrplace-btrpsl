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

package btrpsl.constraint;

import btrpsl.element.*;
import entropy.configuration.ManagedElementSet;
import entropy.configuration.Node;
import entropy.configuration.SimpleManagedElementSet;
import entropy.configuration.VirtualMachine;

import java.util.*;

/**
 * A set of tools to convert BtrpOperand to Entropy sets and checkers.
 *
 * @author Fabien Hermenier
 */
public final class PlacementConstraintBuilders {

    /**
     * No instantiation.
     */
    private PlacementConstraintBuilders() {
    }

    /**
     * Check the number of parameters for a constraint.
     * if the size of {@code elems} is not equals to {@code x}, then an exception is throw and specifies
     * the excepted signature
     *
     * @param b     the constraint to check
     * @param elems the parameters given to the constraint
     * @param x     the expected arity
     * @throws ConstraintBuilderException if the actual arity is not equals to {@code x}
     */
    public static void ensureArity(PlacementConstraintBuilder b, List<BtrpOperand> elems, int x) throws ConstraintBuilderException {
        int s = elems.size();
        if (s != x) {
            throw new ConstraintBuilderException(new StringBuilder(b.getIdentifier()).append(" expects " + x + " argument(s); ").append(elems.size()).append(" were given").toString());
        }
    }

    /**
     * Chech the set is not empty.
     *
     * @param buf a message that will prefix the exception
     * @param s   the set
     * @throws ConstraintBuilderException if the set is empty. {@code buf} is added at the beginning of the error message
     */
    public static void noEmptySets(BtrpOperand buf, Collection s) throws ConstraintBuilderException {
        if (s.isEmpty()) {
            throw new ConstraintBuilderException(new StringBuilder(buf.toString()).append(" is an empty set ").toString());
        }
    }

    /**
     * Convert an operand to a set of virtual machines sets.
     *
     * @param elems the operand to convert
     * @return a list of set of virtual machines
     * @throws ConstraintBuilderException if the conversion is not possible (incompatible type or degree)
     */
    public static List<ManagedElementSet<VirtualMachine>> makeVirtualMachinesSet(BtrpOperand elems) throws ConstraintBuilderException {
        if (elems.type() != BtrpOperand.Type.vm || elems.degree() != 2) {
            throw new ConstraintBuilderException(elems + " must be a set of vmsets");
        }
        List<ManagedElementSet<VirtualMachine>> set = new ArrayList<ManagedElementSet<VirtualMachine>>();
        for (BtrpOperand e : ((BtrpSet) elems).getValues()) {
            set.add(makeVMs(e, false));
        }
        return set;
    }

    /**
     * Convert an operand to a set of virtual machines.
     *
     * @param elems     the operand to convert
     * @param allowAtom {@code true} to transform a single element into a singleton instead of creating an error
     * @return a set of virtual machines
     * @throws ConstraintBuilderException if the conversion is not possible (incompatible type or degree)
     */
    public static ManagedElementSet<VirtualMachine> makeVMs(BtrpOperand elems, boolean allowAtom) throws ConstraintBuilderException {
        ManagedElementSet<VirtualMachine> vms = new SimpleManagedElementSet<VirtualMachine>();
        if (elems.type() != BtrpOperand.Type.vm || (!allowAtom && elems.degree() != 1)) {
            if (elems.degree() == 0) {
                throw new ConstraintBuilderException(elems + " must be a set of virtual machines, not a litteral of type '" + elems.type() + "'");
            }
            throw new ConstraintBuilderException(elems + " must be a set of virtual machines, not a set of type '" + elems.type() + "' with a degree equals to " + elems.degree());
        }
        if (elems.degree() == 0) {
            vms.add(((BtrpVirtualMachine) elems).getElement());
        } else {
            for (BtrpOperand e : ((BtrpSet) elems).getValues()) {
                vms.add(((BtrpVirtualMachine) e).getElement());
            }
        }
        return vms;
    }

    /**
     * Convert an operand to a set of node sets.
     *
     * @param elems the operand to convert
     * @return a list of set of node
     * @throws ConstraintBuilderException if the conversion is not possible (incompatible type or degree)
     */
    public static Set<ManagedElementSet<Node>> makeNodesSet(BtrpOperand elems) throws ConstraintBuilderException {
        if (elems.type() != BtrpOperand.Type.node || elems.degree() != 2) {
            throw new ConstraintBuilderException(elems + " must be a set of nodesets");
        }
        Set<ManagedElementSet<Node>> set = new HashSet<ManagedElementSet<Node>>();
        for (BtrpOperand e : ((BtrpSet) elems).getValues()) {
            set.add(makeNodes(e, false));
        }
        return set;
    }

    /**
     * Convert an operand to a set of nodes.
     *
     * @param elems     the operand to convert
     * @param allowAtom {@code true} to transform a single element into a singleton instead of creating an error
     * @return a set of nodes
     * @throws ConstraintBuilderException if the conversion is not possible (incompatible type or degree)
     */
    public static ManagedElementSet<Node> makeNodes(BtrpOperand elems, boolean allowAtom) throws ConstraintBuilderException {
        if (elems.type() != BtrpOperand.Type.node || (!allowAtom && elems.degree() != 1)) {
            if (elems.degree() == 0) {
                throw new ConstraintBuilderException(elems + " must be a set of nodes, not a litteral of type '" + elems.type() + "'");
            }
            throw new ConstraintBuilderException(elems + " must be a set of nodes, not a set of type '" + elems.type() + "' with a degree equals to " + elems.degree());
        }
        ManagedElementSet<Node> nodes = new SimpleManagedElementSet<Node>();
        if (elems.degree() == 0) {
            nodes.add(((BtrpNode) elems).getElement());
        } else if (elems.degree() > 1) {
            throw new ConstraintBuilderException(elems + " must be a set of nodes, not a set with a degree equals to " + elems.degree());
        } else {
            for (BtrpOperand e : ((BtrpSet) elems).getValues()) {
                nodes.add(((BtrpNode) e).getElement());
            }
        }
        return nodes;
    }

    /**
     * Convert an operand to an int
     *
     * @param elem the operand to convert
     * @return an int
     * @throws ConstraintBuilderException if the conversion is not possible (incompatible type or degree)
     */
    public static int makeInt(BtrpOperand elem) throws ConstraintBuilderException {
        if (elem.type() != BtrpOperand.Type.number || elem.degree() != 0) {
            throw new ConstraintBuilderException(elem + " must be a number");
        }
        return ((BtrpNumber) elem).getIntValue();
    }

    /**
     * Textual representation of a type declaration.
     *
     * @param varName the variable name
     * @param t       the type
     * @param degree  the degree of the set
     * @return a String
     */
    public static String prettyTypeDeclaration(String varName, int degree, BtrpOperand.Type t) {
        return new StringBuilder(varName).append(" : ").append(DefaultBtrpOperand.prettyType(degree, t)).toString();
    }
}
