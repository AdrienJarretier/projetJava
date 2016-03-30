package exceptions;

/**
 *
 * @author Jarretier Adrien "jarretier.adrien@gmail.com"
 */
public class NotGitRepositoryException extends Exception {

    /**
     * Creates a new instance of <code>NotGitRepositoryException</code> without
     * detail message.
     */
    public NotGitRepositoryException() {
    }

    /**
     * Constructs an instance of <code>NotGitRepositoryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotGitRepositoryException(String msg) {
        super(msg);
    }
}
