package br.edu.dmos5.agenda_dmos5.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import br.edu.dmos5.agenda_dmos5.model.User;
import br.edu.dmos5.agenda_dmos5.model.exceptions.UserAlreadyRegisteredException;
import br.edu.dmos5.agenda_dmos5.repository.sql.UserSQL;

public class UserRepository {

    private SQLiteDatabase sqLiteDatabase;

    private SQLiteHelper sqlLiteHelper;

    public UserRepository(Context context) {
        sqlLiteHelper = new SQLiteHelper(context);
    }

    public void save(User user) {
        if (user == null) {
            throw new NullPointerException();
        }

        validateIfUserIsAlreadyRegistered(user);

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserSQL.USER_EMAIL, user.getEmail());
        contentValues.put(UserSQL.USER_PASSWORD, user.getPassword());

        sqLiteDatabase = sqlLiteHelper.getWritableDatabase();

        if (sqLiteDatabase.insert(UserSQL.USER_TABLE, null, contentValues) == -1) {
            throw new SQLException();
        }
        sqLiteDatabase.close();
    }

    public boolean login(User user) {
        if (user == null) {
            throw new NullPointerException();
        }

        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();

        String columns[] = new String[]{
                UserSQL.USER_EMAIL,
                UserSQL.USER_PASSWORD
        };

        String where = "upper(" + UserSQL.USER_EMAIL + ") = upper(?)"
                + " AND upper(" + UserSQL.USER_PASSWORD + ") = upper(?)";
        String[] args = {user.getEmail(), user.getPassword()};

        Cursor cursor = sqLiteDatabase.query(
                UserSQL.USER_TABLE,
                columns,
                where,
                args,
                null,
                null,
                null
        );
        int count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();

        return !(count < 1);
    }

    private void validateIfUserIsAlreadyRegistered(User user) {
        if (user == null) {
            throw new NullPointerException();
        }

        sqLiteDatabase = sqlLiteHelper.getReadableDatabase();

        String columns[] = new String[]{
                UserSQL.USER_EMAIL
        };

        String where = "upper(" + UserSQL.USER_EMAIL + ") = upper(?)";
        String[] args = {user.getEmail()};

        Cursor cursor = sqLiteDatabase.query(
                UserSQL.USER_TABLE,
                columns,
                where,
                args,
                null,
                null,
                null
        );
        int count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();
        if(count > 0 ) {
            throw new UserAlreadyRegisteredException();
        }
    }

}
