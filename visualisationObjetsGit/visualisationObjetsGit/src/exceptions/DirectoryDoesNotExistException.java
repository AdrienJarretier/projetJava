package exceptions;

/**
 *
 * @author Jarretier Adrien "jarretier.adrien@gmail.com"
 */
public class DirectoryDoesNotExistException extends Exception {

    /**
     * Creates a new instance of <code>DirectoryDoesNotExistException</code>
     * without detail message.
     */
    public DirectoryDoesNotExistException() {
    }

    /**
     * Constructs an instance of <code>DirectoryDoesNotExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DirectoryDoesNotExistException(String msg) {
        super(msg);
    }
}
