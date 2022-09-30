package com.tlicorporation.triphil.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tlicorporation.triphil.model.ContainerClass;

public class DatabaseManager {
    private DatabaseHelper dh;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        this.context = c;
    }

    public DatabaseManager open() throws SQLException {
        dh = new DatabaseHelper(context);
        database = dh.getWritableDatabase();
        return this;
    }

    public void close() {
        dh.close();
    }

    public void insert(String userid, String username, String useremail, String userpassword) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dh.COL_USER_ID, userid);
        contentValues.put(dh.COL_USER_NAME, username);
        contentValues.put(dh.COL_USER_EMAIL, useremail);
        contentValues.put(dh.COL_USER_PASSWORD, userpassword);
        database.insert(dh.TABLE_USER, null, contentValues);
    }

    public void update(String userid, String username, String useremail, String userpassword) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dh.COL_USER_ID, userid);
        contentValues.put(dh.COL_USER_NAME, username);
        contentValues.put(dh.COL_USER_EMAIL, useremail);
        contentValues.put(dh.COL_USER_PASSWORD, userpassword);
        database.update(dh.TABLE_USER, contentValues, dh.COL_USER_ID + " = " + userid, null);
    }

    public void delete(int id) {
        database.delete(dh.TABLE_USER,dh.COL_USER_ID + " ='" + id + "'",null);
    }

    public Cursor fetch() {
        String[] columns = new String[]{dh.COL_USER_ID, dh.COL_USER_NAME, dh.COL_USER_EMAIL};
        Cursor cursor = database.query(dh.TABLE_USER, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }



    public void UpsertContRowMax(ContainerClass c) {

    try{

        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dh.COL_SHIPDATE, c.getShipDate());
        contentValues.put(dh.COL_CONTAINER_NO, c.getContainerNo());
        contentValues.put(dh.COL_ROW_NO, c.getRowNo());
        contentValues.put(dh.COL_MAX_QTY, c.getMaxQty());
        db.replace(dh.TABLE_CONTAINER_ROW_MAX,null, contentValues);

    }catch (SQLException sqex){
        Log.e("sql", sqex.getMessage());
    }
    }

}
