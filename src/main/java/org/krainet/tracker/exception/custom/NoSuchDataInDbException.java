package org.krainet.tracker.exception.custom;

public class NoSuchDataInDbException extends RuntimeException {
    String message;

    public NoSuchDataInDbException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Problem with user. Error occurred: " + message;
    }
}
