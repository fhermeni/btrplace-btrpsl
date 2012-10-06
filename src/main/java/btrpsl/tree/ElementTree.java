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
import btrpsl.element.BtrpNode;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpVirtualMachine;
import entropy.configuration.Node;
import entropy.configuration.VirtualMachine;
import entropy.vjob.builder.VJobElementBuilder;
import org.antlr.runtime.Token;

/**
 * A Tree parser to identify a virtual machine or a node.
 *
 * @author Fabien Hermenier
 */
public class ElementTree extends BtrPlaceTree {

    private VJobElementBuilder eBuilder;

    private BtrPlaceVJob vjob;

    /**
     * Make a new parser.
     *
     * @param t    the token to analyze
     * @param errs the errors to report
     */
    public ElementTree(Token t, BtrPlaceVJob vjob, ErrorReporter errs, VJobElementBuilder eb) {
        super(t, errs);
        eBuilder = eb;
        this.vjob = vjob;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        String lbl = getText();
        switch (token.getType()) {
            case ANTLRBtrplaceSL2Parser.NODE_NAME:
                String ref = lbl.substring(1, lbl.length());
                Node n = eBuilder.matchAsNode(ref);
                if (n == null) {
                    return ignoreError("Unknown node '" + ref + "'");
                }
                return new BtrpNode(n);
            case ANTLRBtrplaceSL2Parser.IDENTIFIER:
                /**
                 * Switch to Fully Qualified name before getting the VM
                 */
                String fqn = new StringBuilder(vjob.id()).append('.').append(lbl).toString();
                VirtualMachine vm = vjob.getVirtualMachines().get(fqn);
                if (vm == null) {
                    return ignoreError("Unknown virtual machine '" + fqn + "'");
                }
                return new BtrpVirtualMachine(vm);
            default:
                return ignoreError("Unexpected type: " + ANTLRBtrplaceSL2Parser.tokenNames[token.getType()]);
        }
    }
}
