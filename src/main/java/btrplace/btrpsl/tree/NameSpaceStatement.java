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

import btrplace.btrpsl.BtrpScript;
import btrplace.btrpsl.ErrorReporter;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.IgnorableOperand;
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
    private BtrpScript vjob;

    /**
     * Make a new statement.
     *
     * @param t    the token to consider
     * @param vjob the builded vjob
     * @param errs the reported errors
     */
    public NameSpaceStatement(Token t, BtrpScript vjob, ErrorReporter errs) {
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
        errors.updateNamespace();
        return IgnorableOperand.getInstance();
    }
}
