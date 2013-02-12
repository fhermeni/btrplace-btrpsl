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

import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.IgnorableOperand;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;

/**
 * Tree to handle errors returned by the lexer.
 *
 * @author Fabien Hermenier
 */
public class ErrorTree extends BtrPlaceTree {

    private Token end;

    public ErrorTree(TokenStream input, Token start, Token stop, RecognitionException e) {
        super(start, null);
        end = stop;
    }

    @Override
    public int getLine() {
        return end.getLine();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getCharPositionInLine() {
        return end.getCharPositionInLine();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        return IgnorableOperand.getInstance();
    }
}