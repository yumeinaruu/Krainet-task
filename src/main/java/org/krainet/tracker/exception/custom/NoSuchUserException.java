package org.krainet.tracker.exception.custom;

public class NoSuchUserException extends RuntimeException{
    String message;

    public NoSuchUserException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Problem with user. Error occurred: " + message;
    }
}
