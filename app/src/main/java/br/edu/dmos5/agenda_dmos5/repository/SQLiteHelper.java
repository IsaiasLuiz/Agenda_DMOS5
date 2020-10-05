package br.edu.dmos5.agenda_dmos5.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import br.edu.dmos5.agenda_dmos5.model.Contact;
import br.edu.dmos5.agenda_dmos5.model.ContactItemType;
import br.edu.dmos5.agenda_dmos5.repository.sql.ContactItemSQL;
import br.edu.dmos5.agenda_dmos5.repository.sql.ContactSQL;
import br.edu.dmos5.agenda_dmos5.repository.sql.ContactV2SQL;
import br.edu.dmos5.agenda_dmos5.repository.sql.UserSQL;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;

    public static final String DATABASE_NAME = "phonebook.db";

    private Context context;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ContactV2SQL.CONTACT_V2_CREATE_TABLE);
        sqLiteDatabase.execSQL(UserSQL.USER_CREATE_TABLE);
        sqLiteDatabase.execSQL(ContactItemSQL.CONTACT_ITEM_CREATE_TABLE);
        sqLiteDatabase.execSQL(ContactV2SQL.ADD_FAVORITE_COLUMN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion) {
            case 2:
                db.execSQL(UserSQL.USER_CREATE_TABLE);
                db.execSQL(ContactSQL.USER_ID_ADD_COLUMN);
                break;
            case 3:
                db.execSQL(ContactV2SQL.CONTACT_V2_CREATE_TABLE);
                db.execSQL(ContactItemSQL.CONTACT_ITEM_CREATE_TABLE);
                migrateContacts(db);
                db.execSQL(ContactSQL.CONTACT_DROP_TABLE);
                break;
            case 4:
                db.execSQL(ContactV2SQL.ADD_FAVORITE_COLUMN);
                break;
        }
    }

    private void migrateContacts(SQLiteDatabase db) {
        Cursor usersCursor = db.query(UserSQL.USER_TABLE, new String[]{UserSQL.USER_EMAIL}, null, null, null, null, null);
        while (usersCursor.moveToNext()) {
            String userEmail = usersCursor.getString(0);
            Map<String, Contact> map = new HashMap<>();
            String[] columns = new String[]{
                    ContactSQL.CONTACT_ID_ROW_ID,
                    ContactSQL.CELL_PHONE_COLUMN,
                    ContactSQL.LANDLINE_PHONE_COLUMN,
                    ContactSQL.NAME_COLUMN
            };
            String where = ContactSQL.USER_ID_COLUMN + " = ?;";
            Cursor contactsCursor = db.query(ContactSQL.CONTACT_TABLE, columns, where, new String[]{userEmail}, null, null, null);
            while (contactsCursor.moveToNext()) {
                Integer contactId = contactsCursor.getInt(0);
                String cellPhone = contactsCursor.getString(1);
                String landlinePhone = contactsCursor.getString(2);
                String name = contactsCursor.getString(3);
                Contact oldContact = map.get(name);
                if (oldContact != null) {
                    if (!cellPhone.equalsIgnoreCase(oldContact.getCellPhone())) {
                        saveContactItem(db, oldContact.getContactId(), ContactItemType.CELLPHONE.toString(), cellPhone);
                    }
                    if (!landlinePhone.equalsIgnoreCase(oldContact.getLandlinePhone())) {
                        saveContactItem(db, oldContact.getContactId(), ContactItemType.LANDLINEPHONE.toString(), landlinePhone);
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put(ContactV2SQL.NAME_COLUMN, name);
                    values.put(ContactV2SQL.USER_ID_COLUMN, userEmail);
                    db.insert(ContactV2SQL.CONTACT_V2_TABLE, null, values);

                    saveContactItem(db, contactId, ContactItemType.CELLPHONE.toString(), cellPhone);
                    saveContactItem(db, contactId, ContactItemType.LANDLINEPHONE.toString(), landlinePhone);
                    map.put(name, new Contact(contactId, landlinePhone, cellPhone));
                }
            }
            contactsCursor.close();
        }
        usersCursor.close();
    }

    public void saveContactItem(SQLiteDatabase db, Integer contactId, String type, String phone) {
        ContentValues values = new ContentValues();
        values.put(ContactItemSQL.CONTACT_ID_COLUMN, contactId);
        values.put(ContactItemSQL.TYPE_COLUMN, type);
        values.put(ContactItemSQL.VALUE_COLUMN, phone);
        db.insert(ContactItemSQL.CONTACT_ITEM_TABLE, null, values);
    }
}
