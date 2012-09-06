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

package btrpsl.platform;

import entropy.platform.Platform;
import entropy.platform.PlatformFactory;
import gnu.trove.THashMap;

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
        this.tpls = new THashMap<String, Platform>();
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
