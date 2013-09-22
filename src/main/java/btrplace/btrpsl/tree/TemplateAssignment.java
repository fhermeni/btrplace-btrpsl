/*
 * Copyright (c) 2013 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
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
 * A statement to instantiate VMs or nodes from a specific template.
 *
 * @author Fabien Hermenier
 */
public class TemplateAssignment extends BtrPlaceTree {

    /**
     * The current script.
     */
    private Script script;

    /**
     * The template factory.
     */
    private TemplateFactory tpls;

    private SymbolsTable syms;

    /**
     * Make a new tree.
     *
     * @param t    the token to consider
     * @param s    the script that is built
     * @param syms the symbol table
     * @param errs the errors
     */
    public TemplateAssignment(Token t, Script s, TemplateFactory tpls, SymbolsTable syms, ErrorReporter errs) {
        super(t, errs);
        this.script = s;
        this.tpls = tpls;
        this.syms = syms;
    }

    private Map<String, String> getTemplateOptions() {
        Map<String, String> opts = new HashMap<>();
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

        String tplName = getChild(1).getText();
        if (!tpls.getAvailables().contains(tplName)) {
            return ignoreError("Unknown template '" + tplName + "'");
        }
        Map<String, String> opts = getTemplateOptions();

        int nType = t.getType();
        if (nType == ANTLRBtrplaceSL2Parser.IDENTIFIER) {
            addVM(tplName, script.id() + "." + t.getText(), opts);
        } else if (nType == ANTLRBtrplaceSL2Parser.NODE_NAME) {
            addNode(tplName, t.getText(), opts);
        } else if (nType == ANTLRBtrplaceSL2Parser.ENUM_ID) {
            BtrpOperand op = ((EnumElement) t).expand();

            if (op == IgnorableOperand.getInstance()) {
                return op;
            }

            for (BtrpOperand o : ((BtrpSet) op).getValues()) {
                addVM(tplName, o.toString(), opts);
            }
        } else if (nType == ANTLRBtrplaceSL2Parser.ENUM_FQDN) {
            BtrpOperand op = ((EnumElement) t).expand();

            if (op == IgnorableOperand.getInstance()) {
                return op;
            }

            for (BtrpOperand o : ((BtrpSet) op).getValues()) {
                addNode(tplName, o.toString(), opts);
            }
        } else if (nType == ANTLRBtrplaceSL2Parser.EXPLODED_SET) {
            List<BtrPlaceTree> children = t.getChildren();
            for (BtrPlaceTree child : children) {
                if (child.getType() == ANTLRBtrplaceSL2Parser.IDENTIFIER) {
                    addVM(tplName, script.id() + "." + child.getText(), opts);
                } else if (child.getType() == ANTLRBtrplaceSL2Parser.NODE_NAME) {
                    addNode(tplName, child.getText(), opts);
                } else {
                    return ignoreError("Only VMs or nodes can be declared from templates");
                }
            }
        } else {
            ignoreError("Unable to assign the template to '" + t.getText());
        }
        return IgnorableOperand.getInstance();
    }

    private void addVM(String tplName, String id, Map<String, String> opts) {
        try {
            BtrpElement vm = tpls.build(script, tplName, id, opts);
            script.add(vm);
            //We add the VM to the $me variable
            ((BtrpSet) syms.getSymbol(SymbolsTable.ME)).getValues().add(vm);
        } catch (ElementBuilderException ex) {
            ignoreError(ex.getMessage());
        }
    }

    private void addNode(String tplName, String id, Map<String, String> opts) {
        try {
            BtrpElement n = tpls.build(script, tplName, id, opts);
            script.add(n);
        } catch (ElementBuilderException ex) {
            ignoreError(ex.getMessage());
        }
    }
}
