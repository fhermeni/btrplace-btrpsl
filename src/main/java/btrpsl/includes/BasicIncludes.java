package btrpsl.includes;

import btrpsl.BtrPlaceVJob;
import btrpsl.BtrpPlaceVJobBuilderException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A basic include mechanism where all the VJObs are added manually.
 *
 * @author Fabien Hermenier
 */
public class BasicIncludes implements Includes {
    private Map<String, BtrPlaceVJob> hash;

    public BasicIncludes() {
        this.hash = new HashMap<String, BtrPlaceVJob>();
    }

    @Override
    public List<BtrPlaceVJob> getVJob(String name) throws BtrpPlaceVJobBuilderException {

        List<BtrPlaceVJob> vjobs = new ArrayList<BtrPlaceVJob>();
        if (!name.endsWith(".*")) {
            if (hash.containsKey(name)) {
                vjobs.add(hash.get(name));
            }
        } else {
            String base = name.substring(0, name.length() - 2);
            for (Map.Entry<String, BtrPlaceVJob> e : hash.entrySet()) {
                if (e.getKey().startsWith(base)) {
                    vjobs.add(e.getValue());
                }
            }
        }
        return vjobs;
    }

    /**
     * Add a vjob into the set of included vjobs.
     *
     * @param vjob the vjob to add
     */
    public void add(BtrPlaceVJob vjob) {
        this.hash.put(vjob.id(), vjob);
    }
}
