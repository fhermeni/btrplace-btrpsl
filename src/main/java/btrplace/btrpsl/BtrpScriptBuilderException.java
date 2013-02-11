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

package btrplace.btrpsl;

/**
 * A Exception related to an error while building a BtrpVJob.
 *
 * @author Fabien Hermenier
 */
public class BtrpScriptBuilderException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 5502255795746863232L;

    private ErrorReporter errReporter;

    public BtrpScriptBuilderException(ErrorReporter err) {
        super(err.toString());
        errReporter = err;
    }

    /**
     * Make an exception that preserve the stack trace.
     *
     * @param msg the error message
     * @param t   the original exception
     */
    public BtrpScriptBuilderException(String msg, Throwable t) {
        super(msg, t);
    }

    public ErrorReporter getErrorReporter() {
        return this.errReporter;
    }

    @Override
    public String getMessage() {
        if (errReporter != null) {
            return errReporter.toString();
        }
        return super.getMessage();
    }
}
