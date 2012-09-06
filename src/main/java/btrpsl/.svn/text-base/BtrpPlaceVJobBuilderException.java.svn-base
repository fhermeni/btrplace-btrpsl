/*
 * Copyright (c) Fabien Hermenier
 *
 *         This file is part of Entropy.
 *
 *         Entropy is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU Lesser General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         Entropy is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *         GNU Lesser General Public License for more details.
 *         You should have received a copy of the GNU Lesser General Public License
 *         along with Entropy.  If not, see <http://www.gnu.org/licenses/>.
 */
package btrpsl;

import entropy.vjob.builder.VJobBuilderException;

import java.util.ArrayList;
import java.util.List;

/**
 * A Exception related to an error while building a BtrpVJob.
 *
 * @author Fabien Hermenier
 */
public class BtrpPlaceVJobBuilderException extends VJobBuilderException {

    /**
     *
     */
    private static final long serialVersionUID = 5502255795746863232L;

    private List<String> msgs = null;

    /**
     * Make an exception with a specific error message.
     *
     * @param msg the error message.
     */
    public BtrpPlaceVJobBuilderException(String msg) {
        super(msg);
        msgs = new ArrayList<String>();
        msgs.add(msg);
    }

    /**
     * Make an exception with several error messages.
     *
     * @param msgs the error messages to report
     */
    public BtrpPlaceVJobBuilderException(List<String> msgs) {
        super(msgs.get(0));
        this.msgs = msgs;
    }

    /**
     * Make an exception that preserve the stack trace.
     *
     * @param msg the error message
     * @param t   the original exception
     */
    public BtrpPlaceVJobBuilderException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Make an exception that preserve the stack trace.
     *
     * @param t the original exception
     */
    public BtrpPlaceVJobBuilderException(Throwable t) {
        super(t);
    }

    public List<String> getAllErrors() {
        return this.msgs;
    }
}
