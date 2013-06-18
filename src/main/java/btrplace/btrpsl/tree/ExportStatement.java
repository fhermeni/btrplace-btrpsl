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

import btrplace.btrpsl.ANTLRBtrplaceSL2Parser;
import btrplace.btrpsl.ErrorReporter;
import btrplace.btrpsl.Script;
import btrplace.btrpsl.SymbolsTable;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.BtrpSet;
import btrplace.btrpsl.element.IgnorableOperand;
import org.antlr.runtime.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Statement to specify a list of variables to export.
 * This is only allowed if the script belong to a specified namespace.
 * When a variable is exported, it is exported using its current identifier
 * and its fully qualified name, with is the variable identifier prefixed by the namespace.
 *
 * @author Fabien Hermenier
 */
public class ExportStatement extends BtrPlaceTree {

    private Script script;

    /**
     * Make a new statement.
     *
     * @param t      the export token
     * @param script the script to alter with the variables to export
     * @param errs   the list of errors
     */
    public ExportStatement(Token t, Script script, ErrorReporter errs) {
        super(t, errs);
        this.script = script;
    }

    @Override
    public BtrpOperand go(BtrPlaceTree parent) {

        String pkg = script.id();
        if (pkg == null) {
            return ignoreError("No exportation when the script does not have a fully qualified name");
        }
        Set<String> limits = new HashSet<>();
        List<BtrpOperand> toAdd = new ArrayList<>();
        boolean all = false;
        for (int i = 0; i < getChildCount(); i++) {
            //Just the special case of the $me variable that export all the VMs belonging to the script
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
                script.addExportable(op.label(), op, limits.isEmpty() ? null : limits);
                //script.addExportable(longVersion(pkg, op.label()), op, limits.isEmpty() ? null : limits);
            }
            if (all) {
                script.setGlobalExportScope(limits.isEmpty() ? null : limits);
            }
        } catch (UnsupportedOperationException ex) {
            return ignoreError(ex.getMessage());
        }


        return IgnorableOperand.getInstance();
    }

    private static List<BtrpOperand> flatten(BtrpOperand o) {
        List<BtrpOperand> ret = new ArrayList<>();
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
}
