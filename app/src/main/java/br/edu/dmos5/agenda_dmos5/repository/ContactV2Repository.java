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

    private static final String USER_ID_AND_NAME_WHERE = "upper(" + ContactV2SQL.USER_ID_COLUMN + ") = upper(?) and upper(" + ContactV2SQL.NAME_COLUMN + ") = upper(?);";

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

        String sortOrder = ContactV2SQL.FAVORITE_COLUMN + " DESC, " + ContactV2SQL.NAME_COLUMN + " COLLATE NOCASE ASC";
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

        Cursor cursor = sqLiteDatabase.query(ContactV2SQL.CONTACT_V2_TABLE, columns, USER_ID_AND_NAME_WHERE, new String[]{userId, name}, null, null, null);

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

        Cursor cursor = sqLiteDatabase.query(ContactV2SQL.CONTACT_V2_TABLE, columns, USER_ID_AND_NAME_WHERE, new String[]{userId, contact.getFullName()}, null, null, null);

        cursor.moveToNext();

        Integer contactId = cursor.getInt(0);

        cursor.close();
        sqLiteDatabase.close();

        ContactItemRepository contactItemRepository = new ContactItemRepository(context);

        for (ContactItem item : contact.getContactItems()) {
            contactItemRepository.save(Integer.parseInt(contactId.toString()), item);
        }
    }

    public void favoriteContact(String userId, String name) {
        if (userId == null || name == null) {
            throw new NullPointerException();
        }
        ContentValues values = new ContentValues();
        values.put(ContactV2SQL.FAVORITE_COLUMN, isFavorite(userId, name) ? ContactV2SQL.NOT_FAVORITE : ContactV2SQL.IS_FAVORITE);

        sqLiteDatabase = sqlLiteHelper.getWritableDatabase();

        sqLiteDatabase.update(ContactV2SQL.CONTACT_V2_TABLE, values, USER_ID_AND_NAME_WHERE, new String[]{userId, name});

        sqLiteDatabase.close();
    }

    public boolean isFavorite(String userId, String name) {
        if (userId == null || name == null) {
            throw new NullPointerException();
        }

        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(ContactV2SQL.CONTACT_V2_TABLE, new String[]{ContactV2SQL.FAVORITE_COLUMN}, USER_ID_AND_NAME_WHERE, new String[]{userId, name}, null, null, null);

        cursor.moveToNext();

        int value = cursor.getInt(0);
        cursor.close();
        sqLiteDatabase.close();

        return value == ContactV2SQL.IS_FAVORITE;
    }

    public void delete(String userId, String name) {
        if (userId == null || name == null) {
            throw new NullPointerException();
        }

        sqLiteDatabase = sqlLiteHelper.getWritableDatabase();

        sqLiteDatabase.delete(ContactV2SQL.CONTACT_V2_TABLE, USER_ID_AND_NAME_WHERE, new String[]{userId, name});

        sqLiteDatabase.close();
    }

    public void update(String userId, String name, String newValue) {
        if (userId == null || name == null) {
            throw new NullPointerException();
        }

        ContentValues values = new ContentValues();
        values.put(ContactV2SQL.NAME_COLUMN, newValue);

        sqLiteDatabase = sqlLiteHelper.getWritableDatabase();

        System.out.println(sqLiteDatabase.update(ContactV2SQL.CONTACT_V2_TABLE, values, USER_ID_AND_NAME_WHERE, new String[]{userId, name}));
        sqLiteDatabase.close();
    }

}
