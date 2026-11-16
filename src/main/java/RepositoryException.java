/**
 * CEN 4078 Programming Exercise 3
 * File Name: RepositoryException.java
 * Modified from Liting Zhang's COP4027 examples
 * @author Caleb Metz
 * @version 1.0
 */
public final class RepositoryException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
