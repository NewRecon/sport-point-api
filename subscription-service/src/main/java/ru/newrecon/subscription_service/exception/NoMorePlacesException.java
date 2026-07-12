package ru.newrecon.subscription_service.exception;

public class NoMorePlacesException extends RuntimeException{

    public NoMorePlacesException(String message) {
        super(message);
    }
}
