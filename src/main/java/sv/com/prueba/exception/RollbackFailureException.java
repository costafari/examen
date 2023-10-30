package sv.com.prueba.exception;

public class RollbackFailureException extends Exception {
    public RollbackFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackFailureException(String message) {
        super(message);
    }
}
