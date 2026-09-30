package com.example.BoloDeLaMadre.services.iaService;

public class AssistantUnavailableException extends RuntimeException {
    public AssistantUnavailableException(String message) {
        super(message);
    }

    public AssistantUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
