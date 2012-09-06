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

import btrpsl.BtrPlaceVJob;
import btrpsl.SemanticErrors;
import btrpsl.element.BtrpOperand;
import btrpsl.element.IgnorableOperand;
import org.antlr.runtime.Token;

/**
 * A statement to specify the namespace of the vjob.
 *
 * @author Fabien Hermenier
 */
public class NameSpaceStatement extends BtrPlaceTree {

    /**
     * The builded vjob.
     */
    private BtrPlaceVJob vjob;

    /**
     * Make a new statement.
     *
     * @param t    the token to consider
     * @param vjob the builded vjob
     * @param errs the reported errors
     */
    public NameSpaceStatement(Token t, BtrPlaceVJob vjob, SemanticErrors errs) {
        super(t, errs);
        this.vjob = vjob;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {
        StringBuilder fqdn = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            fqdn.append(getChild(i));
            if (i != getChildCount() - 1) {
                fqdn.append('.');
            }
        }
        String id = fqdn.toString();
        vjob.setFullyQualifiedName(id);
        return IgnorableOperand.getInstance();
    }
}
