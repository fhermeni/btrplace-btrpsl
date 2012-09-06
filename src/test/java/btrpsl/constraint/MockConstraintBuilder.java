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

package btrpsl.constraint;

import btrpsl.element.BtrpOperand;
import entropy.configuration.ManagedElementSet;
import entropy.configuration.VirtualMachine;
import entropy.vjob.PlacementConstraint;

import java.util.List;
import java.util.Set;

/**
 * A mock PBPlacementConstraintBuilder that build MockPlacementConstraint.
 *
 * @author Fabien Hermenier
 */
public class MockConstraintBuilder implements PlacementConstraintBuilder {

    @Override
    public String getIdentifier() {
        return "mock";
    }

    @Override
    public String getSignature() {
        return getIdentifier() + "(<vmset>)";
    }

    @Override
    public PlacementConstraint buildConstraint(List<BtrpOperand> params) throws ConstraintBuilderException {
        try {
            return new MockPlacementConstraint((Set<ManagedElementSet<VirtualMachine>>) params.get(0));
        } catch (ClassCastException e) {
            throw new ConstraintBuilderException(e.getMessage());
        }
    }
}
