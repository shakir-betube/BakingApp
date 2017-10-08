package com.appsys.utils;

import java.io.IOException;

public class InternetException extends IOException {
    public InternetException() {
        super();
    }

    public InternetException(String message) {
        super(message);
    }

    public InternetException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternetException(Throwable cause) {
        super(cause);
    }
}
