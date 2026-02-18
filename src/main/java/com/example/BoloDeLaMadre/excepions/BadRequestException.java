package com.example.BoloDeLaMadre.excepions;

public class BadRequestException extends RuntimeException {

     public BadRequestException(String message) {
        super(message);
    }
    
    
}