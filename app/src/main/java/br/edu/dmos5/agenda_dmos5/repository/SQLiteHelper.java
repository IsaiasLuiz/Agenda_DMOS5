package br.edu.dmos5.agenda_dmos5.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import br.edu.dmos5.agenda_dmos5.repository.sql.ContactSQL;
import br.edu.dmos5.agenda_dmos5.repository.sql.UserSQL;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "phonebook.db";

    private Context context;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ContactSQL.CONTACT_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion){
            case 2 : db.execSQL(UserSQL.USER_CREATE_TABLE);
                db.execSQL(ContactSQL.USER_ID_ADD_COLUMN);
        }
    }

}
