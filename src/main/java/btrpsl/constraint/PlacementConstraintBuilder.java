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
import entropy.vjob.PlacementConstraint;

import java.util.List;

/**
 * An interface to specify a generic PlacementConstraint builder.
 *
 * @author Fabien Hermenier
 */
public interface PlacementConstraintBuilder {

    /**
     * Get the identifier of the constraint.
     *
     * @return a string
     */
    String getIdentifier();

    /**
     * Get the signature of the constraint.
     *
     * @return a string
     */
    String getSignature();

    /**
     * Get the full signature of the constraint, including the parameter name.
     *
     * @return a string
     */
    String getFullSignature();

    /**
     * Get the constraint parameters.
     *
     * @return a non-empty array.
     */
    ConstraintParam[] getParameters();

    /**
     * Build the constraint
     *
     * @param t      the current token.
     * @param params the parameters of the constraint.
     * @return the constraint
     */
    PlacementConstraint buildConstraint(BtrPlaceTree t, List<BtrpOperand> params);
}
