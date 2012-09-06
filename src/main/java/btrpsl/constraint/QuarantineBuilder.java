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
import entropy.configuration.Node;
import entropy.vjob.Quarantine;

import java.util.List;

/**
 * @author Fabien Hermenier
 */
public class QuarantineBuilder implements PlacementConstraintBuilder {

    @Override
    public String getIdentifier() {
        return "quarantine";
    }

    @Override
    public String getSignature() {
        return "quarantine(<nodeset>)";
    }

    @Override
    public Quarantine buildConstraint(List<BtrpOperand> params) throws ConstraintBuilderException {
        PlacementConstraintBuilders.ensureArity(this, params, 1);
        ManagedElementSet<Node> ns = PlacementConstraintBuilders.makeNodes(params.get(0), true);
        if (ns.isEmpty()) {
            throw new ConstraintBuilderException(params.get(0) + " is an empty set");
        }
        return new Quarantine(ns);
    }
}
