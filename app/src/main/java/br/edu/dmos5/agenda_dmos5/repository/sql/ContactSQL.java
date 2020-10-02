package br.edu.dmos5.agenda_dmos5.repository.sql;

import static br.edu.dmos5.agenda_dmos5.repository.sql.UserSQL.USER_TABLE;

public abstract class ContactSQL {

    public static final String CONTACT_TABLE = "contact";

    public static final String NAME_COLUMN = "name";

    public static final String LANDLINE_PHONE_COLUMN = "landline_phone";

    public static final String CELL_PHONE_COLUMN = "cell_phone";

    public static final String USER_ID_COLUMN = "user_id";

    public static final String CONTACT_CREATE_TABLE = "CREATE TABLE " + CONTACT_TABLE + " ("
        + NAME_COLUMN + " TEXT NOT NULL, "
        + LANDLINE_PHONE_COLUMN + " TEXT NOT NULL, "
        + CELL_PHONE_COLUMN + " TEXT NOT NULL); ";

    public static final String USER_ID_ADD_COLUMN = "ALTER TABLE " + CONTACT_TABLE
        +" ADD " + USER_ID_COLUMN + " TEXT REFERENCES "
        + USER_TABLE + "(id);";

}
