package btrpsl;

/**
 * Created with IntelliJ IDEA.
 * User: fhermeni
 * Date: 06/10/12
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class ErrorMessage {

    int lineNo;

    int colNo;

    String namespace;

    String message;

    public ErrorMessage(int l, int c, String msg) {
        this(null, l, c, msg);
    }

    public ErrorMessage(String ns, int l, int c, String msg) {
        this.namespace = ns;
        this.lineNo = l;
        this.colNo = c;
        this.message = msg;
    }

    public String toString() {
        StringBuilder b = new StringBuilder(message.length() + 15);
        b.append('[');

        if (namespace != null) {
            b.append(namespace);
        }
        b.append(" ").append(lineNo).append(':').append(colNo).append("] ").append(message);
        return b.toString();
    }
}
