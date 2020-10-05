package br.edu.dmos5.agenda_dmos5.repository.sql;

public class ContactV2SQL {

    public static final String CONTACT_V2_TABLE = "contact_v2";

    public static final String NAME_COLUMN = "name";

    public static final String USER_ID_COLUMN = "user_id";

    public static final String CONTACT_ID_COLUMN = "contact_id";

    public static final String FAVORITE_COLUMN = "favorite";

    public static final String CONTACT_V2_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTACT_V2_TABLE + " ("
            + NAME_COLUMN + " TEXT NOT NULL, "
            + USER_ID_COLUMN + " TEXT NOT NULL, "
            + CONTACT_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT)";

    public static final String ADD_FAVORITE_COLUMN = "ALTER TABLE " + CONTACT_V2_TABLE
            + " ADD COLUMN " + FAVORITE_COLUMN + " INTEGER";

    public static final int IS_FAVORITE = 1;

    public static final int NOT_FAVORITE = 0;

}
