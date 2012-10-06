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

import java.util.LinkedList;
import java.util.List;

/**
 * A structure to report all the errors detected when parsing a VJob.
 * Output is a plain text.
 *
 * @author Fabien Hermenier
 */
public class PlainTextErrorReporter implements ErrorReporter {

    /**
     * The error messages.
     */
    private List<ErrorMessage> errors;

    private VJob vjob;

    /**
     * Make a new instance.
     *
     * @param v the vjob that is builded
     */
    public PlainTextErrorReporter(VJob v) {
        errors = new LinkedList<ErrorMessage>();
        this.vjob = v;
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
        for (ErrorMessage err : errors) {
            b.append(err.toString());
            b.append('\n');
        }
        return b.toString();
    }

    @Override
    public List<ErrorMessage> getErrors() {
        return this.errors;
    }

    @Override
    public void append(int lineNo, int colNo, String msg) {
        errors.add(new ErrorMessage(vjob.id(), lineNo, colNo, msg));
    }

    public void append(String msg) {
        errors.add(new ErrorMessage(-1, -1, msg));
    }
}
