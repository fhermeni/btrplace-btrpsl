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

package btrpsl.tree;

import btrpsl.ANTLRBtrplaceSL2Parser;
import btrpsl.SemanticErrors;
import btrpsl.SymbolsTable;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.IgnorableOperand;
import org.antlr.runtime.Token;

import java.util.List;

/**
 * A parser to declare a variable.
 *
 * @author Fabien Hermenier
 */
public class AssignmentStatement extends BtrPlaceTree {

    /**
     * The table of symbols to use;
     */
    private SymbolsTable symbols;

    /**
     * Make a new parser
     *
     * @param t    the root token
     * @param errs the errors to report
     * @param syms the table of symbols to use
     */
    public AssignmentStatement(Token t, SemanticErrors errs, SymbolsTable syms) {
        super(t, errs);
        symbols = syms;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        //We declare the variable even if the right operand has to be ignored
        //to reduce annoying error message

        //We evaluate right operand
        try {
            BtrpOperand res = getChild(1).go(this);
            if (res == IgnorableOperand.getInstance()) {
                return res;
            }
            if (getChild(0).getType() == ANTLRBtrplaceSL2Parser.VARIABLE) {
                String lbl = getChild(0).getText();
                if (symbols.isImmutable(lbl)) {
                    return ignoreError(lbl + " is an immutable variable. Assignment not permitted");
                }
                BtrpOperand cpy = res.clone();
                cpy.setLabel(lbl);
                symbols.declare(lbl, cpy);

                return IgnorableOperand.getInstance();
            } else if (getChild(0).getType() == ANTLRBtrplaceSL2Parser.EXPLODED_SET) {
                List<BtrpOperand> vals = ((BtrpSet) res).getValues();
                BtrPlaceTree t = getChild(0);
                for (int i = 0; i < t.getChildCount(); i++) {
                    switch (t.getChild(i).getType()) {
                        case ANTLRBtrplaceSL2Parser.VARIABLE:
                            if (i < vals.size()) {
                                BtrpOperand e = vals.get(i);
                                String lbl = t.getChild(i).getText();
                                if (symbols.isImmutable(lbl)) {
                                    return ignoreError(lbl + " is an immutable variable. Assignment not permitted");
                                }
                                BtrpOperand cpy = e.clone();
                                cpy.setLabel(lbl);
                                symbols.declare(lbl, cpy);
                            }
                            break;
                        case ANTLRBtrplaceSL2Parser.BLANK:
                            break;
                        default:
                            return ignoreError("Unsupported type for decomposition: " + t);
                    }
                }
            } else if (getChild(0).getType() == ANTLRBtrplaceSL2Parser.ENUM_VAR) {
                List<BtrpOperand> vals = ((BtrpSet) res).getValues();
                BtrpOperand op = ((EnumVar) getChild(0)).expand();
                if (op == IgnorableOperand.getInstance()) {
                    return op;
                }
                BtrpSet vars = (BtrpSet) op;
                for (int i = 0; i < vars.getValues().size(); i++) {
                    BtrpOperand o = vars.getValues().get(i);
                    if (i < vals.size()) {
                        BtrpOperand val = vals.get(i);
                        String lbl = o.toString();
                        if (symbols.isImmutable(lbl)) {
                            return ignoreError(lbl + " is an immutable variable. Assignment not permitted");
                        }
                        BtrpOperand cpy = val.clone();
                        cpy.setLabel(lbl);
                        symbols.declare(lbl, cpy);
                    } else {
                        break;
                    }
                }
            } else {
                return ignoreError("Unsupported decomposition");
            }
        } catch (UnsupportedOperationException e) {
            return ignoreError(e.getMessage());
        }
        return IgnorableOperand.getInstance();
    }
}
