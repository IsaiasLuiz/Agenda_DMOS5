package br.edu.dmos5.agenda_dmos5.model.exceptions;

public class EmptyFieldsException extends RuntimeException {

    public EmptyFieldsException() {
    }

    public EmptyFieldsException(String message) {
        super(message);
    }

    public EmptyFieldsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyFieldsException(Throwable cause) {
        super(cause);
    }

}
