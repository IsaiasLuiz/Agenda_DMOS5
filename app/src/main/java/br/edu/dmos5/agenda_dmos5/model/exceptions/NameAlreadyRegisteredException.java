package br.edu.dmos5.agenda_dmos5.model.exceptions;

public class NameAlreadyRegisteredException extends RuntimeException {

    public NameAlreadyRegisteredException() {
    }

    public NameAlreadyRegisteredException(String message) {
        super(message);
    }

    public NameAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

}
