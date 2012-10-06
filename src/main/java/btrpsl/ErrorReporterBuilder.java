package btrpsl;

import entropy.vjob.VJob;

/**
 * The builder used to instantiate a custom {@link ErrorReporter}.
 *
 * @author Fabien Hermenier
 */
public interface ErrorReporterBuilder {

    /**
     * Make a new reporter.
     *
     * @param v the vjob under construction associated to this builder
     * @return the builded reporter
     */
    ErrorReporter build(VJob v);
}
