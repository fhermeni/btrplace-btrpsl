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
import btrpsl.tree.BtrPlaceTree;
import entropy.configuration.ManagedElementSet;
import entropy.configuration.Node;
import entropy.configuration.SimpleManagedElementSet;
import entropy.configuration.VirtualMachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * Convert an operand to a set of virtual machines sets.
     *
     * @param elems the operand to convert
     * @return a list of set of virtual machines
     */
    public static List<ManagedElementSet<VirtualMachine>> makeVirtualMachinesSet(BtrPlaceTree t, BtrpOperand elems) {
        if (elems == IgnorableOperand.getInstance()) {
            return null;
        }
        List<ManagedElementSet<VirtualMachine>> set = new ArrayList<ManagedElementSet<VirtualMachine>>();
        for (BtrpOperand e : ((BtrpSet) elems).getValues()) {
            set.add(makeVMs(t, e));
        }
        return set;
    }

    /**
     * Convert an operand to a set of virtual machines.
     *
     * @param elems the operand to convert
     * @return a set of virtual machines
     */
    public static ManagedElementSet<VirtualMachine> makeVMs(BtrPlaceTree t, BtrpOperand elems) {
        ManagedElementSet<VirtualMachine> vms = new SimpleManagedElementSet<VirtualMachine>();

        if (elems == IgnorableOperand.getInstance()) {
            return null;
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
     */
    public static Set<ManagedElementSet<Node>> makeNodesSet(BtrPlaceTree t, BtrpOperand elems) {
        if (elems == IgnorableOperand.getInstance()) {
            return null;
        }
        Set<ManagedElementSet<Node>> set = new HashSet<ManagedElementSet<Node>>();
        for (BtrpOperand e : ((BtrpSet) elems).getValues()) {
            set.add(makeNodes(t, e));
        }
        return set;
    }

    /**
     * Convert an operand to a set of nodes.
     *
     * @param elems the operand to convert
     * @return a set of nodes
     */
    public static ManagedElementSet<Node> makeNodes(BtrPlaceTree t, BtrpOperand elems) {
        if (elems == IgnorableOperand.getInstance()) {
            return null;
        }
        ManagedElementSet<Node> nodes = new SimpleManagedElementSet<Node>();
        if (elems.degree() == 0) {
            nodes.add(((BtrpNode) elems).getElement());
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
     */
    public static Integer makeInt(BtrpOperand elem) {
        if (elem == IgnorableOperand.getInstance()) {
            return null;
        }
        return ((BtrpNumber) elem).getIntValue();
    }
}
