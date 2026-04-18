package main.exceptions;

public class AbonamentInvalidException extends RuntimeException {
    public AbonamentInvalidException(String message) {
        super(message);
    }
}
