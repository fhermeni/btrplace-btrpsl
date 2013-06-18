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

import btrplace.btrpsl.ErrorReporter;
import btrplace.btrpsl.Script;
import btrplace.btrpsl.ScriptBuilderException;
import btrplace.btrpsl.SymbolsTable;
import btrplace.btrpsl.element.BtrpElement;
import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.element.BtrpSet;
import btrplace.btrpsl.element.IgnorableOperand;
import btrplace.btrpsl.includes.Includes;
import org.antlr.runtime.Token;

import java.util.List;

/**
 * Statement to import some other script wrt. their namespace.
 * If a valid namespace is found. Then, the current symbol table will be
 * completed with the exported variables. In case of conflicts, conflicting
 * variables are removed. This should only occurs with short variables. Fully Qualified
 * variable names should not be affected.
 *
 * @author Fabien Hermenier
 */
public class ImportStatement extends BtrPlaceTree {

    /**
     * The list of available includes.
     */
    private Includes includes;

    /**
     * The symbol table to fulfil.
     */
    private SymbolsTable symTable;

    private Script script;

    /**
     * Make a new statement
     *
     * @param t      the 'IMPORT' token
     * @param incs   the list of available includes
     * @param sTable the symbol table.
     * @param script the currently built script
     * @param errs   the list of errors.
     */
    public ImportStatement(Token t, Includes incs, SymbolsTable sTable, Script script, ErrorReporter errs) {
        super(t, errs);
        this.includes = incs;
        this.symTable = sTable;
        this.script = script;
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
        List<Script> res;
        if (includes == null) {
            return ignoreError("Error while loading '" + id + "': no includes specified");
        }
        try {
            res = includes.getscript(id);
            script.getDependencies().addAll(res);
        } catch (ScriptBuilderException e) {
            int nb = e.getErrorReporter().getErrors().size();
            return ignoreError(Integer.toString(nb) + " error(s) imported through '" + id + "'");
        }
        if (res.isEmpty()) {
            return ignoreError(getChild(0).getToken(), "Unable to locate '" + id + "'");
        }

        BtrpSet global = null;
        if (id.endsWith(".*")) { //Prepare the global variable.
            global = new BtrpSet(1, BtrpOperand.Type.VM);
            global.setLabel(new StringBuilder("$").append(id.substring(0, id.length() - 2)).toString());
        }
        for (Script v : res) {
            for (String ref : v.getExported()) {
                if (v.canImport(ref, script.id())) {
                    //Register the element fully qualified identifier
                    String fqn;
                    if (ref.startsWith("$")) {
                        fqn = "$" + v.id() + "." + ref.substring(1);
                    } else {
                        fqn = v.id() + "." + ref;
                    }
                    if (!symTable.declareImmutable(fqn, v.getExported(ref, script.id()))) {
                        return ignoreError("Unable to export the undefined variable '" + ref + "'");
                    }
                }
            }

            if (v.canImport(script.id())) {
                BtrpSet s = new BtrpSet(1, BtrpOperand.Type.VM);
                for (BtrpElement vm : v.getVMs()) {
                    s.getValues().add(vm);
                    if (global != null) {
                        global.getValues().add(vm);
                    }
                }
                String lbl = new StringBuilder(v.id().length() + 1).append("$").append(v.id()).toString();
                s.setLabel(lbl);
                if (!symTable.declareImmutable(lbl, s)) {
                    return ignoreError("Unable to add variable '" + lbl + "'");
                }
            }
        }
        if (global != null && global.size() > 0 && !symTable.declareImmutable(global.label(), global)) {
            return ignoreError("Unable to add variable '" + global.label() + "'");
        }
        return IgnorableOperand.getInstance();
    }
}
