package br.edu.dmos5.agenda_dmos5.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import br.edu.dmos5.agenda_dmos5.model.Contact;
import br.edu.dmos5.agenda_dmos5.model.User;
import br.edu.dmos5.agenda_dmos5.repository.sql.ContactSQL;

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
        contentValues.put(ContactSQL.NAME_COLUMN, contact.getFullName());
        contentValues.put(ContactSQL.LANDLINE_PHONE_COLUMN, contact.getLandlinePhone());
        contentValues.put(ContactSQL.CELL_PHONE_COLUMN, contact.getCellPhone());
        contentValues.put(ContactSQL.USER_ID_COLUMN, contact.getUserId());

        sqLiteDatabase = sqlLiteHelper.getWritableDatabase();

        if (sqLiteDatabase.insert(ContactSQL.CONTACT_TABLE, null, contentValues) == -1) {
            throw new SQLException();
        }
        sqLiteDatabase.close();
    }

    public List<Contact> findAll(String userId) {
        if (userId == null) {
            throw new NullPointerException();
        }

        List<Contact> contacts = new LinkedList<>();
        Contact contact;
        Cursor cursor;

        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();


        String columns[] = new String[]{
                ContactSQL.NAME_COLUMN,
                ContactSQL.LANDLINE_PHONE_COLUMN,
                ContactSQL.CELL_PHONE_COLUMN,
                ContactSQL.USER_ID_COLUMN
        };

        String sortOrder = ContactSQL.NAME_COLUMN + " COLLATE NOCASE ASC";
        String where = "upper(" + ContactSQL.USER_ID_COLUMN + ") = upper(?)";
        String args[] = new String[]{userId};

        cursor = sqLiteDatabase.query(
                ContactSQL.CONTACT_TABLE,
                columns,
                where,
                args,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            contact = new Contact(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            contacts.add(contact);
        }

        cursor.close();
        sqLiteDatabase.close();

        return contacts;
    }

    public void updateByFirstUser(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        ContentValues values = new ContentValues();
        values.put(ContactSQL.USER_ID_COLUMN, user.getEmail());

        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();

        sqLiteDatabase.update(ContactSQL.CONTACT_TABLE, values, null, null);

        sqLiteDatabase.close();
    }
}
