package br.edu.dmos5.agenda_dmos5.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import br.edu.dmos5.agenda_dmos5.model.ContactItem;
import br.edu.dmos5.agenda_dmos5.model.ContactItemType;
import br.edu.dmos5.agenda_dmos5.repository.sql.ContactItemSQL;
import br.edu.dmos5.agenda_dmos5.repository.sql.ContactV2SQL;

public class ContactItemRepository {

    private SQLiteDatabase sqLiteDatabase;

    private SQLiteHelper sqlLiteHelper;

    public ContactItemRepository(Context context) {
        sqlLiteHelper = new SQLiteHelper(context);
    }

    public void save(Integer contactId, ContactItem item) {

        if(contactId == null || item.getType() == null || item.getValue() == null) {
            throw new NullPointerException();
        }

        SQLiteDatabase sqLiteDatabase = sqlLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ContactItemSQL.CONTACT_ID_COLUMN, contactId);
        values.put(ContactItemSQL.TYPE_COLUMN, item.getType().toString());
        values.put(ContactItemSQL.VALUE_COLUMN, item.getValue());

        if(sqLiteDatabase.insert(ContactItemSQL.CONTACT_ITEM_TABLE, null, values) == -1) {
            throw new SQLException();
        }
        sqLiteDatabase.close();
    }

    public List<ContactItem> findByUserIdAndContactNameAndType(String userId, String contactName, String type) {
        List<ContactItem> contactItems = new LinkedList<>();
        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();
        String sql = "SELECT CI.VALUE, CI.TYPE FROM " + ContactV2SQL.CONTACT_V2_TABLE + " C, " + ContactItemSQL.CONTACT_ITEM_TABLE + " CI "
                + "WHERE C.USER_ID = ? AND C.NAME = ? AND C.CONTACT_ID = CI.CONTACT_ID AND CI.TYPE = ?;";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{userId, contactName, type});

        while (cursor.moveToNext()) {
            contactItems.add(new ContactItem(cursor.getString(0), ContactItemType.valueOf(cursor.getString(1))));
        }
        sqLiteDatabase.close();
        cursor.close();
        return contactItems;
    }

}
