package btrpsl;

/**
 * A tool to check as much contradiction as possible in a vjob.
 *
 * @author Fabien Hermenier
 */
public interface ContradictionChecker {

    boolean check(BtrPlaceVJob vjob);
}
