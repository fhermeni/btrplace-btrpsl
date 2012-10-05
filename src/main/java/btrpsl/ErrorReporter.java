package btrpsl;

/**
 * Interface used to specify a object that collect errors.
 * @author Fabien Hermenier
 */
public interface ErrorReporter {

    /**
     * Report an error.
     * @param msg the error message.
     */
    void append(String msg);
}
