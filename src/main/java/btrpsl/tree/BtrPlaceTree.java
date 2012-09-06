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
import btrpsl.element.BtrpOperand;
import btrpsl.element.IgnorableOperand;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import java.util.List;


/**
 * An abstract parser for a tree.
 *
 * @author Fabien Hermenier
 */
public class BtrPlaceTree extends CommonTree {

    /**
     * All the errors to report.
     */
    private SemanticErrors errors;

    /**
     * Make a new tree.
     *
     * @param t    the token to handle. The root of this tree
     * @param errs the errors to report
     */
    public BtrPlaceTree(Token t, SemanticErrors errs) {
        super(t);
        errors = errs;
    }

    /**
     * Parse the root of the tree.
     *
     * @param parent the parent of the root
     * @return a content
     */
    public BtrpOperand go(BtrPlaceTree parent) {
        throw new UnsupportedOperationException("Unhandled token: " + token.getText() + " (type=" + token.getType() + " " + (token.getType() >= 0 ? ANTLRBtrplaceSL2Parser.tokenNames[token.getType()] : "") + ")");
    }

    /**
     * Report an error for the current token and generate a content to ignore.
     *
     * @param msg the error message
     * @return an empty content
     */
    protected IgnorableOperand ignoreError(String msg) {
        errors.append(token, msg);
        return IgnorableOperand.getInstance();
    }

    protected IgnorableOperand ignoreErrors(List<String> msgs) {
        errors.append(msgs);
        return IgnorableOperand.getInstance();
    }

    protected IgnorableOperand ignoreError(Token t, String msg) {
        errors.append(t, msg);
        return IgnorableOperand.getInstance();
    }

    @Override
    public BtrPlaceTree getChild(int i) {
        return (BtrPlaceTree) super.getChild(i);
    }
}
