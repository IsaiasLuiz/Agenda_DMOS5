package br.edu.dmos5.agenda_dmos5.repository.sql;

public abstract class UserSQL {

    public static final String USER_TABLE = "user";

    public static final String USER_EMAIL = "email";

    public static final String USER_PASSWORD = "password";

    public static final String USER_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ("
            + USER_EMAIL + " TEXT NOT NULL UNIQUE, "
            + USER_PASSWORD + " TEXT NOT NULL); ";
}
