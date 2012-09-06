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
import gnu.trove.THashMap;

import java.util.Map;
import java.util.Set;

/**
 * A fake platform.
 *
 * @author Fabien Hermenier
 */
public class PlatformStub implements Platform {

    private String id;

    private THashMap<String, String> opts;

    /**
     * Make a new platform having given identifier
     *
     * @param id the identifier of the platform
     */
    public PlatformStub(String id) {
        this.id = id;
        this.opts = new THashMap<String, String>();
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public void addOption(String opt) {
        opts.put(opt, null);
    }

    @Override
    public boolean checkOption(String opt) {
        return opts.containsKey(opt);
    }

    @Override
    public Set<String> getOptions() {
        return opts.keySet();
    }

    @Override
    public void addOption(String key, String value) {
        opts.put(key, value);
    }

    @Override
    public String getOption(String k) {
        return opts.get(k);
    }

    @Override
    public Platform clone() {
        PlatformStub p = new PlatformStub(id);
        for (Map.Entry<String, String> e : opts.entrySet()) {
            p.addOption(e.getKey(), e.getValue());
        }
        return p;

    }
}
