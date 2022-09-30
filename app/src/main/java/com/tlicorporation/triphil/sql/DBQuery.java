package com.tlicorporation.triphil.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tlicorporation.triphil.model.ContainerClass;

import java.io.IOException;
import java.util.ArrayList;

public class DBQuery {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DBQuery(Context context) {
        dbHelper = new DBHelper(context);
    }

    public DBQuery createDatabase() throws SQLException {
        try {
            dbHelper.createDataBase();
        } catch (IOException ignored) {
        }
        return this;
    }

    public DBQuery open() throws SQLException {
        try {
            dbHelper.openDataBase();
            dbHelper.close();
            db = dbHelper.getReadableDatabase();
        } catch (SQLException ignored) {
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<String> getUsers() {
        String sql = "select * from user";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            ArrayList<String> arUsers = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    arUsers.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            return arUsers;
        } catch (Exception ignored) {
        } finally {
            cursor.close();
        }
        return null;
    }

    //id INTEGER
    //ship_date TEXT
    //row_no INTEGER
    //max_qty INTEGER
    //container_no INTEGER



    public void setRowNoMaxQty(ContainerClass c){
            try{
                ContentValues contentValues = new ContentValues();
                contentValues.put("ship_date", c.getShipDate());
                contentValues.put("container_no", c.getContainerNo());
                contentValues.put("row_no", c.getRowNo());
                contentValues.put("max_qty", c.getMaxQty());
                db.replace("row_max",null, contentValues);
            }catch (SQLException sqex) {
                this.setMessage(sqex.getMessage());
            }catch (Exception ex){
                    this.setMessage(ex.getMessage());
            }
    }

    public ContainerClass getRowNoMaxQty(ContainerClass c){
        String sql = "select id, ship_date, row_no," +
                " max_qty, container_no" +
                " from row_max" +
                " where ship_date = ?" +
                " and row_no = ?" +
                " and container_no = ?";

        String[] args = {c.getShipDate(),
                        String.valueOf(c.getRowNo()),
                        String.valueOf(c.getContainerNo())};

        Cursor cur = db.rawQuery(sql, args);
        if (cur.moveToFirst()) {
            c.setMaxQty(cur.getInt(3));
            return c;
            //return cur.getInt(3);
        }

        c.setMaxQty(0);
        return c;
    }
}
