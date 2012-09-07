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
import btrpsl.tree.BtrPlaceTree;
import entropy.configuration.ManagedElementSet;
import entropy.configuration.Node;
import entropy.vjob.Quarantine;

import java.util.List;

/**
 * @author Fabien Hermenier
 */
public class QuarantineBuilder extends DefaultPlacementConstraintBuilder {

    private static ConstraintParameter[] params = new ConstraintParameter[]{
            new ConstraintParameter(BtrpOperand.Type.node, 1, "$n")
    };

    @Override
    public ConstraintParameter[] getParameters() {
        return params;
    }

    @Override
    public String getIdentifier() {
        return "quarantine";
    }

    @Override
    public Quarantine buildConstraint(BtrPlaceTree t, List<BtrpOperand> params) {
        boolean ret = checkConformance(t, params);
        ManagedElementSet<Node> ns = PlacementConstraintBuilders.makeNodes(t, params.get(0));
        return (ret && ns != null ? new Quarantine(ns) : null);
    }
}
