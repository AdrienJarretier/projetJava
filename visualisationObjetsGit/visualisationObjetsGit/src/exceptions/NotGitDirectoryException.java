package exceptions;

/**
 *
 * @author Jarretier Adrien "jarretier.adrien@gmail.com"
 */
public class NotGitDirectoryException extends Exception {

    /**
     * Creates a new instance of <code>NoGitDirectoryException</code> without
     * detail message.
     */
    public NotGitDirectoryException() {
    }

    /**
     * Constructs an instance of <code>NoGitDirectoryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotGitDirectoryException(String msg) {
        super(msg);
    }
}
