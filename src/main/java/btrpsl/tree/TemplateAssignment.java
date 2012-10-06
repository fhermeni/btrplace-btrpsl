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
import btrpsl.BtrPlaceVJob;
import btrpsl.ErrorReporter;
import btrpsl.SymbolsTable;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.BtrpVirtualMachine;
import btrpsl.element.IgnorableOperand;
import entropy.configuration.Node;
import entropy.configuration.VirtualMachine;
import entropy.template.VirtualMachineBuilderException;
import entropy.template.VirtualMachineTemplate;
import entropy.template.VirtualMachineTemplateFactory;
import entropy.vjob.builder.VJobElementBuilder;
import gnu.trove.THashMap;
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
     * The current vjon.
     */
    private BtrPlaceVJob vjob;

    /**
     * The template factory.
     */
    private VirtualMachineTemplateFactory tpls;

    private VJobElementBuilder eBuilder;

    private SymbolsTable syms;

    /**
     * Make a new tree.
     *
     * @param t    the token to consider
     * @param v    the builded vjob
     * @param eb   the element builder
     * @param syms the symbol table
     * @param errs the errors
     */
    public TemplateAssignment(Token t, BtrPlaceVJob v, VJobElementBuilder eb, SymbolsTable syms, ErrorReporter errs) {
        super(t, errs);
        this.vjob = v;
        this.tpls = eb.getTemplates();
        this.eBuilder = eb;
        this.syms = syms;
    }

    private THashMap<String, String> getVMOptions(int idx) {
        THashMap<String, String> opts = new THashMap<String, String>();
        BaseTree t = getChild(idx);
        for (int i = 0; i < t.getChildCount(); i++) {
            TemplateOptionTree opt = (TemplateOptionTree) t.getChild(i);
            opt.go(this);
            java.lang.String k = opt.getKey();
            java.lang.String v = opt.getValue();
            opts.put(k, v);
        }
        return opts;
    }

    private Map<String, String> getPlatformOption(int idx) {
        BtrPlaceTree t = getChild(idx);

        Map<String, String> opts = new HashMap<String, String>();
        for (int i = 0; i < t.getChildCount(); i++) {
            TemplateOptionTree opt = (TemplateOptionTree) t.getChild(i);
            opt.go(this);
            java.lang.String k = opt.getKey();
            java.lang.String v = opt.getValue();
            opts.put(k, v);
        }
        return opts;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {


        BtrPlaceTree t = getChild(0);

        try {
            int nType = t.getType();
            if (nType == ANTLRBtrplaceSL2Parser.IDENTIFIER) {
                String tplName = getChild(1).getText();
                String fqn = vjob.id() + "." + t.getText();
                if (getChildCount() > 2) { //More than one template for VMs
                    return ignoreError("A Virtual machine can not have multiple templates");
                }
                VirtualMachine vm = eBuilder.matchVirtualMachine(fqn, tplName, getVMOptions(1));
                /*VirtualMachineTemplate tpl = tpls.getTemplate(tplName);
                if (tpl == null) {
                    return ignoreError("Unknown template '" + tplName + "'");
                } */
                //VirtualMachine vm = tpl.build(vjob.id() + "." + t.getText(), getVMOptions(1));
                if (vm == null) {
                    return ignoreError("Unable to instantiate virtual machine '" + t.getText() + "'");
                }
                vm.setTemplate(tplName);
                vjob.addVirtualMachine(vm);
                //We add the VM to the $me variable
                ((BtrpSet) syms.getSymbol(SymbolsTable.ME)).getValues().add(new BtrpVirtualMachine(vm));

                return IgnorableOperand.getInstance();
            } else if (nType == ANTLRBtrplaceSL2Parser.NODE_NAME) {
                String ref = t.getText().substring(1, t.getText().length());
                Node n = eBuilder.matchAsNode(ref);
                if (n == null) {
                    return ignoreError("Unknown node '" + ref + "'");
                }
                //Get all the options
                for (int i = 1; i < getChildCount(); i++) {
                    String id = getChild(i).getText();
                    Map<String, String> opts = getPlatformOption(i);
                    n.addPlatform(id, opts);
                }
                vjob.addNode(n);
                return IgnorableOperand.getInstance();
            } else if (nType == ANTLRBtrplaceSL2Parser.ENUM_ID) {
                BtrpOperand op = ((EnumElement) t).expand();

                if (op == IgnorableOperand.getInstance()) {
                    return op;
                }

                BtrpSet s = (BtrpSet) op;

                String tplName = getChild(1).getText();

                /*VirtualMachineTemplate tpl = tpls.getTemplate(tplName);

                if (tpl == null) {
                    return ignoreError("Unknown template '" + tplName + "'");
                } */
                if (getChildCount() > 2) { //More than one template for VMs
                    return ignoreError("A Virtual machine can not have multiple templates");
                }
                for (BtrpOperand o : s.getValues()) {


                    VirtualMachine vm = eBuilder.matchVirtualMachine(o.toString(), tplName, getVMOptions(1));
                    /*VirtualMachineTemplate tpl = tpls.getTemplate(tplName);
                    if (tpl == null) {
                        return ignoreError("Unknown template '" + tplName + "'");
                    } */
                    //VirtualMachine vm = tpl.build(vjob.id() + "." + t.getText(), getVMOptions(1));
                    if (vm == null) {
                        VirtualMachineTemplate tpl = tpls.getTemplate(tplName);
                        if (tpl == null) {
                            return ignoreError("Template '" + tplName + "' unknown");
                        } else {
                            return ignoreError("Unable to instantiate virtual machine '" + o.toString() + "'");
                        }
                    }

//                    VirtualMachine vm = tpl.build(o.toString(), getVMOptions(1));
//                    vm.setTemplate(tplName);
                    vjob.addVirtualMachine(vm);
                    //We add the VM to the $me variable
                    ((BtrpSet) syms.getSymbol(SymbolsTable.ME)).getValues().add(new BtrpVirtualMachine(vm));
                }
                return IgnorableOperand.getInstance();
            } else if (nType == ANTLRBtrplaceSL2Parser.ENUM_FQDN) {
                BtrpOperand op = ((EnumElement) t).expand();

                if (op == IgnorableOperand.getInstance()) {
                    return op;
                }

                BtrpSet s = (BtrpSet) op;

                for (BtrpOperand o : s.getValues()) {
                    Node n = eBuilder.matchAsNode(o.toString());
                    if (n == null) {
                        return ignoreError("Unknown node '" + o.toString() + "'");
                    }
                    //Get all the options
                    for (int i = 1; i < getChildCount(); i++) {
                        String id = getChild(i).getText();
                        Map<String, String> opts = getPlatformOption(i);
                        n.addPlatform(id, opts);
                    }
                    vjob.addNode(n);
                }
                return IgnorableOperand.getInstance();
            } else if (nType == ANTLRBtrplaceSL2Parser.EXPLODED_SET) {
                List<BtrPlaceTree> children = t.getChildren();
                for (BtrPlaceTree child : children) {
                    if (child.getType() == ANTLRBtrplaceSL2Parser.IDENTIFIER) {
                        String tplName = getChild(1).getText();
                        VirtualMachineTemplate tpl = tpls.getTemplate(tplName);
                        if (tpl == null) {
                            return ignoreError("Unknown template '" + tplName + "'");
                        }
                        if (getChildCount() > 2) { //More than one template for VMs
                            return ignoreError("A Virtual machine can not have multiple templates");
                        }
                        VirtualMachine vm = tpl.build(vjob.id() + "." + child.getText(), getVMOptions(1));
                        vm.setTemplate(tplName);
                        vjob.addVirtualMachine(vm);
                        //We add the VM to the $me variable
                        ((BtrpSet) syms.getSymbol(SymbolsTable.ME)).getValues().add(new BtrpVirtualMachine(vm));
                    } else if (child.getType() == ANTLRBtrplaceSL2Parser.NODE_NAME) {
                        String ref = child.getText().substring(1, child.getText().length());
                        Node n = eBuilder.matchAsNode(ref);
                        //Get all the options
                        for (int i = 1; i < getChildCount(); i++) {
                            String id = getChild(i).getText();
                            Map<String, String> opts = getPlatformOption(i);
                            n.addPlatform(id, opts);
                        }
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
        } catch (VirtualMachineBuilderException e) {
            return ignoreError(e.getMessage());
        }
    }
}
