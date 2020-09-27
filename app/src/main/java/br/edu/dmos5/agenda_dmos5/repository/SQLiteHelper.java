package br.edu.dmos5.agenda_dmos5.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "phonebook.db";


    public static final String CONTACT_TABLE = "contact";
    public static final String NAME_COLUMN = "name";
    public static final String LANDLINE_PHONE_COLUMN = "landline_phone";
    public static final String CELL_PHONE_COLUMN = "cell_phone";

    private Context context;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + CONTACT_TABLE + " (";
        sql += NAME_COLUMN + " TEXT NOT NULL, ";
        sql += LANDLINE_PHONE_COLUMN + " TEXT NOT NULL, ";
        sql += CELL_PHONE_COLUMN + " TEXT NOT NULL); ";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
