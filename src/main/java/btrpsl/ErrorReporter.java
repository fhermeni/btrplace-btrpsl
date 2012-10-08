package btrpsl;

import java.util.List;

/**
 * Interface used to specify a object that collect errors.
 *
 * @author Fabien Hermenier
 */
public interface ErrorReporter {

    /**
     * Report an error.
     *
     * @param lineNo the line index
     * @param colNo  the column index
     * @param msg    the error message
     */
    void append(int lineNo, int colNo, String msg);

    /**
     * Get the reported errors.
     *
     * @return a list of error messages
     */
    List<ErrorMessage> getErrors();

    void updateNamespace();
}
