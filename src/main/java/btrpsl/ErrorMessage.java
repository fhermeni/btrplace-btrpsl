/*
 *
 *  Copyright (c) 2012 University of Nice Sophia-Antipolis
 *
 *  This file is part of btrplace.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrpsl;

/**
 * An error message
 *
 * @author Fabien Hermenier
 */
public class ErrorMessage {

    /**
     * The line number.
     */
    int lineNo;

    /**
     * The column number.
     */
    int colNo;

    /**
     * The vjob namespace.
     */
    String namespace;

    /**
     * The error message.
     */
    String message;

    /**
     * Build a new error message.
     *
     * @param l   the pointed line number
     * @param c   the pointed column number
     * @param msg the error message
     */
    public ErrorMessage(int l, int c, String msg) {
        this(null, l, c, msg);
    }

    /**
     * Build a new error message.
     *
     * @param ns  the namespace for the error message.
     * @param l   the pointed line number
     * @param c   the pointed column number
     * @param msg the error message
     */
    public ErrorMessage(String ns, int l, int c, String msg) {
        this.namespace = ns;
        this.lineNo = l;
        this.colNo = c;
        this.message = msg;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(message.length() + 15);
        b.append('[');

        if (namespace != null) {
            b.append(namespace);
        }
        b.append(' ').append(lineNo).append(':').append(colNo).append("] ").append(message);
        return b.toString();
    }
}
