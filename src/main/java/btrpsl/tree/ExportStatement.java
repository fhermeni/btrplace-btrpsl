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
import btrpsl.DefaultErrorReporter;
import btrpsl.SymbolsTable;
import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.IgnorableOperand;
import gnu.trove.THashSet;
import org.antlr.runtime.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Statement to specify a list of variables to export.
 * This is only allowed if the vjob belong to a specified namespace.
 * When a variable is exported, it is exported using its current identifier
 * and its fully qualified name, with is the variable identifier prefixed by the namespace.
 *
 * @author Fabien Hermenier
 */
public class ExportStatement extends BtrPlaceTree {

    private BtrPlaceVJob vjob;

    /**
     * Make a new statement.
     *
     * @param t    the export token
     * @param vjob the vjob to alter with the variables to export
     * @param errs the list of errors
     */
    public ExportStatement(Token t, BtrPlaceVJob vjob, DefaultErrorReporter errs) {
        super(t, errs);
        this.vjob = vjob;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {

        String pkg = vjob.id();
        if (pkg == null) {
            return ignoreError("No exportation when the vjob does not have a fully qualified name");
        }
        THashSet<String> limits = new THashSet<String>();
        List<BtrpOperand> toAdd = new ArrayList<BtrpOperand>();
        boolean all = false;
        for (int i = 0; i < getChildCount(); i++) {
            //Just the special case of the $me variable that export all the VMs belonging to the vjob
            if (getChild(i).getText().equals(SymbolsTable.ME)) {
                all = true;
            } else if (getChild(i).getType() == ANTLRBtrplaceSL2Parser.ENUM_VAR) {
                BtrpOperand r = getChild(i).go(this);
                if (r == IgnorableOperand.getInstance()) {
                    return r;
                }
                for (BtrpOperand o : ((BtrpSet) r).getValues()) {
                    toAdd.add(o);
                }
            } else if (getChild(i).getType() == ANTLRBtrplaceSL2Parser.TIMES) {
                limits.clear(); //So it will be set to null
            } else if (getChild(i).getType() != ANTLRBtrplaceSL2Parser.IDENTIFIER) {
                BtrpOperand e = getChild(i).go(this);
                if (e == IgnorableOperand.getInstance()) {
                    return e;
                }
                toAdd.addAll(flatten(e));
            } else {
                limits.add(getChild(i).getText());
            }
        }
        try {
            for (BtrpOperand op : toAdd) {
                vjob.addExportable(op.label(), op, limits.isEmpty() ? null : limits);
                vjob.addExportable(longVersion(pkg, op.label()), op, limits.isEmpty() ? null : limits);
            }
            if (all) {
                vjob.setGlobalExportScope(limits.isEmpty() ? null : limits);
            }
        } catch (UnsupportedOperationException ex) {
            return ignoreError(ex.getMessage());
        }


        return IgnorableOperand.getInstance();
    }

    private static List<BtrpOperand> flatten(BtrpOperand o) {
        List<BtrpOperand> ret = new ArrayList<BtrpOperand>();
        if (o.label() != null) {
            ret.add(o);
        } else {
            if (o.degree() == 0) {
                throw new UnsupportedOperationException(o + ": Variable expected");
            } else {
                BtrpSet s = (BtrpSet) o;
                for (BtrpOperand so : s.getValues()) {
                    ret.addAll(flatten(so));
                }
            }
        }
        return ret;
    }

    private static String longVersion(String id, String label) {
        StringBuilder b = new StringBuilder("$");
        b.append(id);
        b.append(".");
        b.append(label.substring(1, label.length()));
        return b.toString();
    }
}
