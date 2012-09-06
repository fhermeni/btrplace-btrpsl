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

import btrpsl.SemanticErrors;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.IgnorableOperand;
import gnu.trove.THashSet;
import org.antlr.runtime.Token;

/**
 * A parser to make exploded sets.
 *
 * @author Fabien Hermenier
 */
public class ExplodedSetTree extends BtrPlaceTree {

    /**
     * Make a new parser.
     *
     * @param t    the root token
     * @param errs the errors to report
     */
    public ExplodedSetTree(Token t, SemanticErrors errs) {
        super(t, errs);
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {

        BtrpOperand t0 = getChild(0).go(this);
        if (t0 == IgnorableOperand.getInstance()) {
            return t0;
        }
        BtrpSet s = new BtrpSet(t0.degree() + 1, t0.type());

        THashSet<BtrpOperand> viewed = new THashSet<BtrpOperand>();
        for (int i = 0; i < getChildCount(); i++) {
            BtrpOperand tx = getChild(i).go(this);
            //s.getIntValue().add() is not safe at all. So preconditions have to be check
            if (tx == IgnorableOperand.getInstance()) {
                return tx;
            }
            if (tx.degree() != s.degree() - 1) {
                return ignoreError(s + " only accept elements with a degree equals to " + (s.degree() - 1));
            }
            if (tx.type() != s.type()) {
                return ignoreError(s + " only accept elements having a type " + s.type());
            }
            if (viewed.add(tx)) {
                s.getValues().add(tx);
            } else {
                return ignoreError(tx + " ignored");
            }
        }
        return s;
    }
}
