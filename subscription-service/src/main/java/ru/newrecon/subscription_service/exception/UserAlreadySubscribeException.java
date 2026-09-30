package ru.newrecon.subscription_service.exception;

public class UserAlreadySubscribeException extends RuntimeException {

    public UserAlreadySubscribeException(String message) {
        super(message);
    }
}
