package br.edu.dmos5.agenda_dmos5.repository.sql;

import static br.edu.dmos5.agenda_dmos5.repository.sql.ContactV2SQL.CONTACT_V2_TABLE;

public abstract class ContactItemSQL {

    public static final String CONTACT_ITEM_TABLE = "contact_item";

    public static final String CONTACT_ID_COLUMN = "contact_id";

    public static final String TYPE_COLUMN = "type";

    public static final String VALUE_COLUMN = "value";


    public static final String CONTACT_ITEM_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTACT_ITEM_TABLE + " ("
            + TYPE_COLUMN + " TEXT NOT NULL, "
            + VALUE_COLUMN + " TEXT NOT NULL, "
            + CONTACT_ID_COLUMN + " INTEGER NOT NULL, FOREIGN KEY(" + CONTACT_ID_COLUMN + ") REFERENCES " + CONTACT_V2_TABLE + "(" + CONTACT_ID_COLUMN + ") ON DELETE CASCADE,"
            + " PRIMARY KEY(" + VALUE_COLUMN + ", " + CONTACT_ID_COLUMN + ", " + TYPE_COLUMN + ")); ";

}
