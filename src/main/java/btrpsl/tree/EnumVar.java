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

import btrpsl.SemanticErrors;
import btrpsl.SymbolsTable;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.BtrpString;
import btrpsl.element.IgnorableOperand;
import org.antlr.runtime.Token;

/**
 * An enumeration of variables.
 *
 * @author Fabien Hermenier
 */
public class EnumVar extends BtrPlaceTree {

    private SymbolsTable syms;

    /**
     * Make a new tree.
     *
     * @param payload the root token
     * @param sTable  to symbol table
     * @param errors  the errors to report
     */
    public EnumVar(Token payload, SymbolsTable sTable, SemanticErrors errors) {
        super(payload, errors);
        this.syms = sTable;
    }

    /**
     * Expand the enumeration.
     * Variables are not evaluated nor checked
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
                res.getValues().add(new BtrpString(id));
            }
        }
        return res;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        String head = getChild(0).getText().substring(0, getChild(0).getText().length() - 1);
        String tail = getChild(getChildCount() - 1).getText().substring(1);
        BtrpSet res = null;

        for (int i = 1; i < getChildCount() - 1; i++) {
            BtrpOperand op = getChild(i).go(this);
            if (op == IgnorableOperand.getInstance()) {
                return op;
            }
            BtrpSet s = (BtrpSet) op;
            for (BtrpOperand o : s.getValues()) {
                //Compose
                String id = new StringBuilder(head).append(o.toString()).append(tail).toString();
                //lookup
                BtrpOperand var = syms.getSymbol(id);
                if (var == null) {
                    return ignoreError(parent.getToken(), "Unknown variable '" + id + "'");
                }
                if (res == null) {
                    res = new BtrpSet(2, var.type());
                }
                res.getValues().add(var);
            }
        }
        return res;
    }
}
