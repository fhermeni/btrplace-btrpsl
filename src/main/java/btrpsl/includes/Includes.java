package btrpsl.includes;

import btrpsl.BtrPlaceVJob;
import btrpsl.BtrpPlaceVJobBuilderException;

import java.util.List;

/**
 * Denotes a library that is used to get the vjobs required from the 'use' statement.
 *
 * @author Fabien Hermenier
 */
public interface Includes {

    /**
     * Get a list of vjob from an identifier.
     * If the identifier ends with the '.*' wildcard, then any vjob matching this wildcard will be returned.
     * Otherwise, only the first vjob matching the identifier will be returned if it exists.
     *
     * @param name the identifier of the vjob
     * @return A list containing the matched vjob, may be empty.
     * @throws BtrpPlaceVJobBuilderException if an error occurred while parsing the founded vjob
     */
    List<BtrPlaceVJob> getVJob(String name) throws BtrpPlaceVJobBuilderException;
}
