package com.insogroup.utils.initialisator.exceptions;

import java.io.IOException;

/**
 * Exception will called when object can't be initialised
 */
public class InitializableException extends IOException {
    public InitializableException() {
    }

    public InitializableException(String message) {
        super(message);
    }

    public InitializableException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializableException(Throwable cause) {
        super(cause);
    }
}
