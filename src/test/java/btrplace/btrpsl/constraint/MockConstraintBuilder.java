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

import btrplace.btrpsl.element.BtrpOperand;
import btrplace.btrpsl.tree.BtrPlaceTree;
import btrplace.model.Node;
import btrplace.model.VM;
import btrplace.model.constraint.SatConstraint;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A mock PBPlacementConstraintBuilder that build MockPlacementConstraint.
 *
 * @author Fabien Hermenier
 */
public class MockConstraintBuilder extends DefaultSatConstraintBuilder {

    public MockConstraintBuilder() {
        super(new ConstraintParam[]{new SetOfParam("$v", 2, BtrpOperand.Type.VM, false)});
    }

    @Override
    public String getIdentifier() {
        return "mock";
    }

    @Override
    public SatConstraint buildConstraint(BtrPlaceTree t, List<BtrpOperand> args) {
        checkConformance(t, args);
        return new MockPlacementConstraint((Set<Set<VM>>) params[0].transform(this, t, args.get(0)));
    }
}
