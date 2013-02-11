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

package btrplace.btrpsl.platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Fabien Hermenier
 */
public class PlatformFactoryStub implements PlatformFactory {

    private Map<String, Platform> tpls;

    /**
     * Make a new stub.
     */
    public PlatformFactoryStub() {
        this.tpls = new HashMap<String, Platform>();
    }

    @Override
    public Set<String> getAvailables() {
        return tpls.keySet();
    }

    @Override
    public Platform getPlatform(String k) {
        if (!tpls.containsKey(k)) {
            tpls.put(k, new PlatformStub(k));
        }
        return tpls.get(k);
    }
}
