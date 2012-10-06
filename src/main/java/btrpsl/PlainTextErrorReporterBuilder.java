package btrpsl;

import entropy.vjob.VJob;

/**
 * The builder associated to {@link PlainTextErrorReporter}.
 *
 * @author Fabien Hermenier
 */
public class PlainTextErrorReporterBuilder implements ErrorReporterBuilder {

    @Override
    public ErrorReporter build(VJob v) {
        return new PlainTextErrorReporter(v);
    }
}
