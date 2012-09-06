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

import entropy.configuration.*;
import entropy.plan.choco.ReconfigurationProblem;
import entropy.vjob.PlacementConstraint;
import entropy.vjob.builder.protobuf.PBVJob;
import entropy.vjob.builder.protobuf.ProtobufVJobSerializer;
import entropy.vjob.builder.xml.XmlVJobSerializer;

import java.util.Set;

/**
 * A mock assignment constraint for test purpose.
 *
 * @author Fabien Hermenier
 */
public class MockPlacementConstraint implements PlacementConstraint {

    private Set<ManagedElementSet<VirtualMachine>> sets;

    public MockPlacementConstraint() {

    }

    public MockPlacementConstraint(Set<ManagedElementSet<VirtualMachine>> vmsets) {
        this.sets = vmsets;
    }

    @Override
    public void inject(ReconfigurationProblem plan) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return "mock(" + sets + ")";
    }

    /**
     * Check that the constraint is satified in a configuration.
     *
     * @param cfg the configuration to check
     * @return true
     */
    @Override
    public boolean isSatisfied(Configuration cfg) {
        return true;
    }

    @Override
    public ManagedElementSet<VirtualMachine> getAllVirtualMachines() {
        ManagedElementSet<VirtualMachine> all = new SimpleManagedElementSet<VirtualMachine>();
        for (ManagedElementSet<VirtualMachine> l : sets) {
            all.addAll(l);
        }
        return all;
    }

    @Override
    public ManagedElementSet<VirtualMachine> getMisPlaced(Configuration cfg) {
        return new SimpleManagedElementSet<VirtualMachine>();
    }

    @Override
    public ManagedElementSet<Node> getNodes() {
        return new SimpleManagedElementSet<Node>();
    }

    @Override
    public String toXML() {
        StringBuilder b = new StringBuilder();
        b.append("<constraint id=\"mock\">");
        b.append("<params>");
        b.append("<param>").append(XmlVJobSerializer.getVMBigSet(sets)).append("</param>");
        b.append("</params>");
        b.append("</constraint>");
        return b.toString();
    }

    @Override
    public PBVJob.vjob.Constraint toProtobuf() {
        PBVJob.vjob.Constraint.Builder b = PBVJob.vjob.Constraint.newBuilder();
        b.setId("lonely");
        b.addParam(PBVJob.vjob.Param.newBuilder().setType(PBVJob.vjob.Param.Type.SET).setSet(ProtobufVJobSerializer.getVMBigSet(sets)).build());
        return b.build();
    }

    @Override
    public Type getType() {
        return Type.absolute;
    }
}
