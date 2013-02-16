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

import java.util.Stack;
import java.util.UUID;

/**
 * A in-memory implementation of {@link UUIDPool}.
 *
 * @author Fabien Hermenier
 */
public class InMemoryUUIDPool implements UUIDPool {

    private final Stack<UUID> available;

    private long nextHi;

    private long nextLow;

    private long size;

    private long free;
    public static final long DEFAULT_SIZE = Long.MAX_VALUE;

    public InMemoryUUIDPool() {
        this(DEFAULT_SIZE);
    }

    /**
     * Make a new pool of element.
     */
    public InMemoryUUIDPool(long s) {
        size = s;
        free = size;
        nextHi = 0;
        nextLow = 0;
        available = new Stack<UUID>();
    }

    @Override
    public UUID request() {
        synchronized (available) {
            if (free <= 0) {
                return null;
            }
            if (available.isEmpty()) {
                nextLow = (nextLow + 1) % Long.MAX_VALUE;
                if (nextLow == 0) {
                    nextHi++;
                    if (nextHi < 0) {
                        return null;
                    }
                }
                free--;
                return new UUID(nextHi, nextLow);
            } else {
                free--;
                return available.pop();
            }
        }
    }

    @Override
    public void release(UUID u) {
        synchronized (available) {
            free++;
            available.push(u);
        }
    }
}
