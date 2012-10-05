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

package btrpsl;

import entropy.vjob.VJob;
import org.antlr.runtime.Token;

import java.util.LinkedList;
import java.util.List;

/**
 * A structure to report all the errors detected when parsing a VJob.
 *
 * @author Fabien Hermenier
 */
public class DefaultErrorReporter implements ErrorReporter {

    /**
     * The error messages.
     */
    private List<String> errors;

    private VJob vjob;

    /**
     * Make a new instance.
     *
     * @param v the vjob that is builded
     */
    public DefaultErrorReporter(VJob v) {
        errors = new LinkedList<String>();
        this.vjob = v;
    }

    /**
     * Append an error related to a token
     *
     * @param t   the token responsible for the error
     * @param msg the error message
     */
    public void append(Token t, String msg) {
        StringBuilder b = new StringBuilder();
        b.append("(");
        b.append(t.getLine());
        b.append(":");
        b.append(t.getCharPositionInLine());
        b.append(") ");
        b.append(vjob.id());
        b.append(": ");
        b.append(msg);
        errors.add(b.toString());
    }

    @Override
    public void append(String msg) {
        errors.add(msg);
    }

    /**
     * Report a list of error messages.
     *
     * @param msgs the error messages.
     */
    public void append(List<String> msgs) {
        errors.addAll(msgs);
    }

    /**
     * Get the number of errors
     *
     * @return an integer
     */
    public int size() {
        return errors.size();
    }

    /**
     * Print all the errors, one per line.
     *
     * @return all the reported errors
     */
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (String l : errors) {
            b.append(l);
            b.append("\n");
        }
        return b.toString();
    }
}
