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

import btrplace.btrpsl.ANTLRBtrplaceSL2Parser;
import btrplace.btrpsl.ErrorReporter;
import btrplace.btrpsl.Script;
import btrplace.btrpsl.SymbolsTable;
import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.BtrpSet;
import btrplace.btrpsl.element.IgnorableOperand;
import btrplace.btrpsl.template.ElementBuilderException;
import btrplace.btrpsl.template.TemplateFactory;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.BaseTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A statement to type some virtual machine to a specific template.
 *
 * @author Fabien Hermenier
 */
public class TemplateAssignment extends BtrPlaceTree {

    /**
     * The current vjob.
     */
    private Script vjob;

    /**
     * The template factory.
     */
    private TemplateFactory tpls;

    private SymbolsTable syms;

    /**
     * Make a new tree.
     *
     * @param t    the token to consider
     * @param v    the builded vjob
     * @param syms the symbol table
     * @param errs the errors
     */
    public TemplateAssignment(Token t, Script v, TemplateFactory tpls, SymbolsTable syms, ErrorReporter errs) {
        super(t, errs);
        this.vjob = v;
        this.tpls = tpls;
        this.syms = syms;
    }

    private Map<String, String> getTemplateOptions() {
        Map<String, String> opts = new HashMap<String, String>();
        BaseTree t = getChild(1);
        for (int i = 0; i < t.getChildCount(); i++) {
            TemplateOptionTree opt = (TemplateOptionTree) t.getChild(i);
            opt.go(this);
            opts.put(opt.getKey(), opt.getValue());
        }
        return opts;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {


        BtrPlaceTree t = getChild(0);


        if (tpls == null) {
            return ignoreError("No templates available");
        }

        String tplName = getChild(1).getText();
        Map<String, String> opts = getTemplateOptions();


        try {
            int nType = t.getType();
            if (nType == ANTLRBtrplaceSL2Parser.IDENTIFIER) {
                String fqn = vjob.id() + "." + t.getText();

                BtrpElement e = tpls.build(vjob, tplName, fqn, opts);
                if (e == null) {
                    return ignoreError("Unable to create VM '" + fqn + "' from template '" + tplName + "'");
                }
                vjob.addVM(e);
                //We add the VM to the $me variable
                ((BtrpSet) syms.getSymbol(SymbolsTable.ME)).getValues().add(e);

                return IgnorableOperand.getInstance();
            } else if (nType == ANTLRBtrplaceSL2Parser.NODE_NAME) {
                String ref = t.getText().substring(1, t.getText().length());

                BtrpElement n = tpls.build(vjob, tplName, t.getText(), opts);
                if (n == null) {
                    return ignoreError("Unable to create node '" + ref + "' from template '" + getChild(1).getText() + "'");
                }

                vjob.addNode(n);
                return IgnorableOperand.getInstance();
            } else if (nType == ANTLRBtrplaceSL2Parser.ENUM_ID) {
                BtrpOperand op = ((EnumElement) t).expand();

                if (op == IgnorableOperand.getInstance()) {
                    return op;
                }

                BtrpSet s = (BtrpSet) op;

                for (BtrpOperand o : s.getValues()) {
                    BtrpElement vm = tpls.build(vjob, tplName, o.toString(), opts);
                    if (vm == null) {
                        return ignoreError("Unable to instantiate the VM '" + o.toString() + "'");
                    }

                    vjob.addVM(vm);
                    //We add the VM to the $me variable
                    ((BtrpSet) syms.getSymbol(SymbolsTable.ME)).getValues().add(vm);
                }
                return IgnorableOperand.getInstance();
            } else if (nType == ANTLRBtrplaceSL2Parser.ENUM_FQDN) {
                BtrpOperand op = ((EnumElement) t).expand();

                if (op == IgnorableOperand.getInstance()) {
                    return op;
                }

                BtrpSet s = (BtrpSet) op;
                for (BtrpOperand o : s.getValues()) {
                    BtrpElement n = tpls.build(vjob, tplName, o.toString(), opts);
                    if (n == null) {
                        return ignoreError("Unknown node '" + o.toString() + "'");
                    }
                    vjob.addNode(n);
                }
                return IgnorableOperand.getInstance();
            } else if (nType == ANTLRBtrplaceSL2Parser.EXPLODED_SET) {
                List<BtrPlaceTree> children = t.getChildren();
                for (BtrPlaceTree child : children) {
                    if (child.getType() == ANTLRBtrplaceSL2Parser.IDENTIFIER) {
                        BtrpElement vm = tpls.build(vjob, tplName, vjob.id() + "." + getChild(1).getText(), opts);
                        vjob.addVM(vm);
                        //We add the VM to the $me variable
                        ((BtrpSet) syms.getSymbol(SymbolsTable.ME)).getValues().add(vm);
                    } else if (child.getType() == ANTLRBtrplaceSL2Parser.NODE_NAME) {
                        String ref = child.getText().substring(1, child.getText().length());
                        BtrpElement n = tpls.build(vjob, tplName, child.getText(), opts);
                        if (n == null) {
                            return ignoreError("Unknown node '" + ref + "'");
                        }
                        vjob.addNode(n);
                    } else {
                        return ignoreError("template assignment is only dedicated to VM or node identifier");
                    }
                }
                return IgnorableOperand.getInstance();
            }
            return ignoreError("Unable to assign the template to '" + t.getText());
        } catch (UnsupportedOperationException e) {
            return ignoreError(e.getMessage());
        } catch (ElementBuilderException e) {
            return ignoreError(e.getMessage());
        }
    }
}
