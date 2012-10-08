package btrpsl.constraint;

import btrpsl.element.BtrpOperand;
import btrpsl.element.BtrpSet;
import btrpsl.element.BtrpVirtualMachine;
import btrpsl.element.IgnorableOperand;
import btrpsl.tree.BtrPlaceTree;
import entropy.configuration.ManagedElementSet;
import entropy.configuration.SimpleManagedElementSet;
import entropy.configuration.VirtualMachine;

/**
 *  A parameter for a constraint that denotes a set of virtual machines.
 *  @author Fabien Hermenier
 */
public class SetOfVMsParam implements ConstraintParam<ManagedElementSet<VirtualMachine>> {

    private String name;

    private boolean canBeEmpty = true;

    public SetOfVMsParam(String n) {
        this(n, true);
    }

    public SetOfVMsParam(String n, boolean canBeEmpty) {
        this.name = n;
        this.canBeEmpty = canBeEmpty;
    }

    @Override
    public String prettySignature() {
        return "set<VM>";
    }

    @Override
    public String fullSignature() {
        return name + " : set<VM>";
    }

    @Override
    public ManagedElementSet<VirtualMachine> transform(PlacementConstraintBuilder cb, BtrPlaceTree tree, BtrpOperand o) {
        ManagedElementSet<VirtualMachine> vms = new SimpleManagedElementSet<VirtualMachine>();

        if (o != IgnorableOperand.getInstance() && !(o.type() == BtrpOperand.Type.VM && (o.degree() == 0 || o.degree() == 1))) {
            throw new UnsupportedOperationException();
        }

        if (o == IgnorableOperand.getInstance()) { return null;}
        if (o.degree() == 0) {
            vms.add(((BtrpVirtualMachine) o).getElement());
        } else {
            BtrpSet s = (BtrpSet) o;
            if (!canBeEmpty && s.size() == 0) {
                tree.ignoreError("In '" + cb.getFullSignature() + "', '" +getName() + "' expects a non-empty set");
                return null;
            }
            for (BtrpOperand e : ((BtrpSet) o).getValues()) {
                vms.add(((BtrpVirtualMachine) e).getElement());
            }
        }
        return vms;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isCompatibleWith(BtrPlaceTree t, BtrpOperand o) {
        return o == IgnorableOperand.getInstance() || (o.type() == BtrpOperand.Type.VM && (o.degree() == 0 || o.degree() == 1));
    }
}
