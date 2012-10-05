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

import btrpsl.DefaultErrorReporter;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpString;
import org.antlr.runtime.Token;

/**
 * A tree that is only a root to parse a String.
 *
 * @author Fabien Hermenier
 */
public class StringTree extends BtrPlaceTree {


    /**
     * Make a new tree
     *
     * @param payload the token containing the string
     * @param errors  to report error
     */
    public StringTree(Token payload, DefaultErrorReporter errors) {
        super(payload, errors);
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        //Remove the \" \"
        return new BtrpString(getText().substring(1, getText().length() - 1));
    }
}
