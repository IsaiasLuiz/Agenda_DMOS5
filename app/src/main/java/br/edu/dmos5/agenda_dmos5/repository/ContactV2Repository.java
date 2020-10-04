package br.edu.dmos5.agenda_dmos5.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import br.edu.dmos5.agenda_dmos5.model.ContactItem;
import br.edu.dmos5.agenda_dmos5.model.ContactV2;
import br.edu.dmos5.agenda_dmos5.model.exceptions.NameAlreadyRegisteredException;
import br.edu.dmos5.agenda_dmos5.repository.sql.ContactV2SQL;

public class ContactV2Repository {

    private SQLiteDatabase sqLiteDatabase;

    private SQLiteHelper sqlLiteHelper;

    private Context context;

    public ContactV2Repository(Context context) {
        this.context = context;
        sqlLiteHelper = new SQLiteHelper(context);
    }

    public List<String> findContactsNameByUserId(String userId) {
        if (userId == null) {
            throw new NullPointerException();
        }

        List<String> contacts = new LinkedList<>();
        Cursor cursor;

        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();


        String columns[] = new String[]{
                ContactV2SQL.NAME_COLUMN
        };

        String sortOrder = ContactV2SQL.NAME_COLUMN + " COLLATE NOCASE ASC";
        String where = "upper(" + ContactV2SQL.USER_ID_COLUMN + ") = upper(?)";
        String args[] = new String[]{userId};

        cursor = sqLiteDatabase.query(
                ContactV2SQL.CONTACT_V2_TABLE,
                columns,
                where,
                args,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            contacts.add(cursor.getString(0));
        }

        cursor.close();
        sqLiteDatabase.close();

        return contacts;
    }

    public void save(ContactV2 contact, String userId) {
        if (contact == null || userId == null) {
            throw new NullPointerException();
        }

        validIfContactNameExists(userId, contact.getFullName());

        sqLiteDatabase = sqlLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactV2SQL.NAME_COLUMN, contact.getFullName());
        values.put(ContactV2SQL.USER_ID_COLUMN, userId);

        Long contactId = sqLiteDatabase.insert(ContactV2SQL.CONTACT_V2_TABLE, null, values);
        sqLiteDatabase.close();

        if (contactId == -1) {
            throw new SQLException();
        }

        ContactItemRepository contactItemRepository = new ContactItemRepository(context);

        for (ContactItem item : contact.getContactItems()) {
            contactItemRepository.save(Integer.parseInt(contactId.toString()), item);
        }
    }

    public void validIfContactNameExists(String userId, String name) {
        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();

        String[] columns = new String[]{
                ContactV2SQL.CONTACT_ID_COLUMN
        };

        String where = "upper(" + ContactV2SQL.USER_ID_COLUMN + ") = upper(?) and upper(" + ContactV2SQL.NAME_COLUMN + ") = upper(?);";

        Cursor cursor = sqLiteDatabase.query(ContactV2SQL.CONTACT_V2_TABLE, columns, where, new String[]{userId, name}, null, null, null);

        if (cursor.moveToNext()) {
            sqLiteDatabase.close();
            cursor.close();
            throw new NameAlreadyRegisteredException();
        }
    }

    public void saveAndMerge(ContactV2 contact, String userId) {
        if (contact == null || userId == null) {
            throw new NullPointerException();
        }

        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();

        String[] columns = new String[]{
                ContactV2SQL.CONTACT_ID_COLUMN
        };

        String where = "upper(" + ContactV2SQL.USER_ID_COLUMN + ") = upper(?) and upper(" + ContactV2SQL.NAME_COLUMN + ") = upper(?);";

        Cursor cursor = sqLiteDatabase.query(ContactV2SQL.CONTACT_V2_TABLE, columns, where, new String[]{userId, contact.getFullName()}, null, null, null);

        cursor.moveToNext();

        Integer contactId = cursor.getInt(0);

        cursor.close();
        sqLiteDatabase.close();

        ContactItemRepository contactItemRepository = new ContactItemRepository(context);

        for (ContactItem item : contact.getContactItems()) {
            contactItemRepository.save(Integer.parseInt(contactId.toString()), item);
        }
    }

}
