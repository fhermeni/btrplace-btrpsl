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
 * Default implementation of a {@link ConstraintsCatalog}.
 *
 * @author Fabien Hermenier
 */
public class DefaultConstraintsCatalog implements ConstraintsCatalog {

    /**
     * The map to get the builder associated to a constraint.
     */
    private Map<String, SatConstraintBuilder> builders;

    /**
     * Build a new catalog.
     */
    public DefaultConstraintsCatalog() {
        this.builders = new HashMap<String, SatConstraintBuilder>();

        add(new AmongBuilder());
        add(new BanBuilder());
        add(new CumulatedResourceCapacityBuilder());
        add(new CumulatedRunningCapacityBuilder());
        add(new FenceBuilder());
        add(new GatherBuilder());
        add(new KilledBuilder());
        add(new LonelyBuilder());
        add(new OfflineBuilder());
        add(new OnlineBuilder());
        add(new OverbookBuilder());
        add(new PreserveBuilder());
        add(new QuarantineBuilder());
        add(new ReadyBuilder());
        add(new RootBuilder());
        add(new RunningBuilder());
        add(new SingleResourceCapacityBuilder());
        add(new SingleRunningCapacityBuilder());
        add(new SleepingBuilder());
        add(new SplitBuilder());
        add(new SplitAmongBuilder());
        add(new SpreadBuilder());
    }

    /**
     * Add a constraint builder to the catalog.
     * There must not be another builder with the same identifier in the catalog
     *
     * @param c the constraint to add
     * @return true if the constraintbuilder has been added.
     */
    public boolean add(SatConstraintBuilder c) {
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
    public SatConstraintBuilder getConstraint(String id) {
        return builders.get(id);
    }
}
