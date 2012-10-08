/*
 * Copyright (c) Fabien Hermenier
 *
 *         This file is part of Entropy.
 *
 *         Entropy is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU Lesser General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         Entropy is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *         GNU Lesser General Public License for more details.
 *         You should have received a copy of the GNU Lesser General Public License
 *         along with Entropy.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrpsl.tree;

import btrpsl.BtrPlaceVJob;
import btrpsl.ErrorReporter;
import btrpsl.element.*;
import entropy.configuration.Node;
import entropy.configuration.VirtualMachine;
import entropy.vjob.builder.VJobElementBuilder;
import org.antlr.runtime.Token;

/**
 * An enumeration of either nodes or virtual machines.
 *
 * @author Fabien Hermenier
 */
public class EnumElement extends BtrPlaceTree {

    private VJobElementBuilder eBuilder;

    private BtrpOperand.Type type;

    private BtrPlaceVJob vjob;

    /**
     * Make a new tree.
     *
     * @param payload the root token
     * @param v       the vjob being build
     * @param eb      the builder to make virtual machines or nodes
     * @param type    the type of the elements in the enumeration
     * @param errors  the errors to report
     */
    public EnumElement(Token payload, BtrPlaceVJob v, VJobElementBuilder eb, BtrpOperand.Type type, ErrorReporter errors) {
        super(payload, errors);
        this.eBuilder = eb;
        this.type = type;
        this.vjob = v;
    }

    /**
     * Expand the enumeration.
     * Elements are not evaluated
     *
     * @return a set of string or an error
     */
    public BtrpOperand expand() {
        String head = getChild(0).getText().substring(0, getChild(0).getText().length() - 1);
        String tail = getChild(getChildCount() - 1).getText().substring(1);
        BtrpSet res = new BtrpSet(1, BtrpOperand.Type.string);

        for (int i = 1; i < getChildCount() - 1; i++) {
            BtrpOperand op = getChild(i).go(this);
            if (op == IgnorableOperand.getInstance()) {
                return op;
            }
            BtrpSet s = (BtrpSet) op;
            for (BtrpOperand o : s.getValues()) {
                //Compose
                String id = new StringBuilder(head).append(o.toString()).append(tail).toString();
                //Remove heading '@' for the nodes
                if (type == BtrpOperand.Type.node) {
                    res.getValues().add(new BtrpString(id.substring(1)));
                } else {
                    res.getValues().add(new BtrpString(new StringBuilder(vjob.id()).append('.').append(id).toString()));
                }

            }
        }
        return res;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        String head = getChild(0).getText().substring(0, getChild(0).getText().length() - 1);
        String tail = getChild(getChildCount() - 1).getText().substring(1);
        BtrpSet res;

        switch (type) {
            case node:
                res = new BtrpSet(1, BtrpOperand.Type.node);
                break;
            case VM:
                res = new BtrpSet(1, BtrpOperand.Type.VM);
                break;
            default:
                return ignoreError("Unsupported enumeration type: '" + type + "'");
        }

        for (int i = 1; i < getChildCount() - 1; i++) {
            BtrpOperand op = getChild(i).go(this);
            if (op == IgnorableOperand.getInstance()) {
                return op;
            }
            BtrpSet s = (BtrpSet) op;
            for (BtrpOperand o : s.getValues()) {

                if (o == IgnorableOperand.getInstance()) {
                    return o;
                }

                //Compose
                String id = new StringBuilder(head).append(o.toString()).append(tail).toString();

                if (type == BtrpOperand.Type.node) {
                    Node n = eBuilder.matchAsNode(id.substring(1)); //remove the heading '@'
                    if (n == null) {
                        return ignoreError(parent.getToken(), "Unknown node '" + id.substring(1) + "'");
                    }
                    res.getValues().add(new BtrpNode(n));
                } else if (type == BtrpOperand.Type.VM) {
                    String fqn = new StringBuilder(vjob.id()).append('.').append(id).toString();
                    VirtualMachine vm = vjob.getVirtualMachines().get(fqn);
                    /*if (VM == null) {
                        VM = eBuilder.matchVirtualMachine(fqn);
                    } */
                    if (vm == null) {
                        return ignoreError(parent.getToken(), "Unknown virtual machine '" + fqn + "'");
                    }
                    res.getValues().add(new BtrpVirtualMachine(vm));
                } else {
                    return ignoreError("Unsupported type '" + type + "' in enumeration");
                }
            }
        }
        return res;
    }
}
