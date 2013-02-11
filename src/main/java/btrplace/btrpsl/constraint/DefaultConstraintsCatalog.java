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

package btrplace.btrpsl.constraint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of ConstraintCalalog.
 *
 * @author Fabien Hermenier
 */
public class DefaultConstraintsCatalog implements ConstraintsCatalog {

    /**
     * The map to get the builder associated to a constraint.
     */
    private Map<String, PlacementConstraintBuilder> builders;

    /**
     * Build a new catalog.
     */
    public DefaultConstraintsCatalog() {
        this.builders = new HashMap<String, PlacementConstraintBuilder>();

        add(new AmongBuilder());
        add(new BanBuilder());
        add(new FenceBuilder());
        add(new BanBuilder());
        add(new SplitBuilder());
        add(new LonelyBuilder());
        add(new OfflineBuilder());
        add(new OnlineBuilder());
        add(new QuarantineBuilder());
        add(new RootBuilder());
        add(new SpreadBuilder());
        add(new GatherBuilder());
        add(new CumulatedRunningCapacityBuilder());
    }

    /**
     * Add a constraint builder to the catalog.
     * There must not be another builder with the same identifier in the catalog
     *
     * @param c the constraint to add
     * @return true if the constraintbuilder has been added.
     */
    public boolean add(PlacementConstraintBuilder c) {
        if (this.builders.containsKey(c.getIdentifier())) {
            return false;
        }
        this.builders.put(c.getIdentifier(), c);
        return true;
    }

    @Override
    public Set<String> getAvailableConstraints() {
        return this.builders.keySet();
    }

    @Override
    public PlacementConstraintBuilder getConstraint(String id) {
        return builders.get(id);
    }
}
