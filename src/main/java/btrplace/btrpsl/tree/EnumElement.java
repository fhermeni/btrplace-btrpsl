/*
 * Copyright (c) 2012 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrplace.btrpsl.tree;

import btrplace.btrpsl.ErrorReporter;
import btrplace.btrpsl.NamingService;
import btrplace.btrpsl.Script;
import btrplace.btrpsl.element.*;
import org.antlr.runtime.Token;

/**
 * An enumeration of either nodes or virtual machines.
 *
 * @author Fabien Hermenier
 */
public class EnumElement extends BtrPlaceTree {

    private BtrpOperand.Type type;

    private Script vjob;

    private NamingService namingService;

    /**
     * Make a new tree.
     *
     * @param payload the root token
     * @param v       the vjob being build
     * @param type    the type of the elements in the enumeration
     * @param errors  the errors to report
     */
    public EnumElement(Token payload, NamingService srv, Script v, BtrpOperand.Type type, ErrorReporter errors) {
        super(payload, errors);
        this.type = type;
        this.vjob = v;
        this.namingService = srv;
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
                    res.getValues().add(new BtrpString(id/*id.substring(1)*/));
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
                    BtrpElement el = namingService.resolve(id);
                    if (el == null) {
                        return ignoreError("Unknown node '" + id.substring(1) + "'");
                    }
                    res.getValues().add(el);
                } else if (type == BtrpOperand.Type.VM) {
                    String fqn = new StringBuilder(vjob.id()).append('.').append(id).toString();
                    BtrpElement el = namingService.resolve(fqn);
                    if (el == null) {
                        return ignoreError("Unknown VM '" + id + "'");
                    }
                    res.getValues().add(el);
                } else {
                    return ignoreError("Unsupported type '" + type + "' in enumeration");
                }
            }
        }
        return res;
    }
}
