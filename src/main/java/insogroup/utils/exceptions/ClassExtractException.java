package insogroup.utils.exceptions;

import java.io.IOException;

/**
 * Throws when extraction class was failed
 */
public class ClassExtractException extends IOException {
    public ClassExtractException(String message) {
        super(message);
    }

    public ClassExtractException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassExtractException(Throwable cause) {
        super(cause);
    }

    public ClassExtractException() {
    }
}
