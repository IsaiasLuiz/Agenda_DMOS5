package br.edu.dmos5.agenda_dmos5.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import br.edu.dmos5.agenda_dmos5.model.Contact;

public class ContactRepository {

    private SQLiteDatabase sqLiteDatabase;

    private SQLiteHelper sqlLiteHelper;

    public ContactRepository(Context context) {
        sqlLiteHelper = new SQLiteHelper(context);
    }

    public void save(Contact contact) {
        if (contact == null) {
            throw new NullPointerException();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.NAME_COLUMN, contact.getFullName());
        contentValues.put(SQLiteHelper.LANDLINE_PHONE_COLUMN, contact.getLandlinePhone());
        contentValues.put(SQLiteHelper.CELL_PHONE_COLUMN, contact.getCellPhone());

        sqLiteDatabase = sqlLiteHelper.getWritableDatabase();

        if (sqLiteDatabase.insert(SQLiteHelper.CONTACT_TABLE, null, contentValues) == -1) {
            throw new SQLException();
        }
        sqLiteDatabase.close();
    }

    public List<Contact> findAll() {

        List<Contact> contacts = new LinkedList<>();
        Contact contact;
        Cursor cursor;

        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();


        String columns[] = new String[]{
                SQLiteHelper.NAME_COLUMN,
                SQLiteHelper.LANDLINE_PHONE_COLUMN,
                SQLiteHelper.CELL_PHONE_COLUMN
        };

        String sortOrder = SQLiteHelper.NAME_COLUMN + " COLLATE NOCASE ASC";

        cursor = sqLiteDatabase.query(
                SQLiteHelper.CONTACT_TABLE,
                columns,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            contact = new Contact(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            contacts.add(contact);
        }

        cursor.close();
        sqLiteDatabase.close();

        return contacts;
    }
}
