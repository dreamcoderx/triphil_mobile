package com.tlicorporation.triphil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {

    private static String _user = "sa";
    private static String _pass = "root";
    private static String _db = "triphil";
    private static String _server = "192.168.100.26";


    @SuppressLint("NewApi")
    public static Connection CONN(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;

        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
        _server = sharedPreferences.getString("server","");
        _db = sharedPreferences.getString("database","");
        _user = sharedPreferences.getString("user","");
        _pass = sharedPreferences.getString("password","");
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _db + ";user=" + _user + ";password="
                    + _pass + ";";
            Log.e("erro",ConnURL);
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", "SQL EXCEPTION: " + se.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", "CLASS NOT FOUND: " + e.getMessage());
            return null;

        } catch (Exception e) {
            Log.e("ERROR", "EXCEPTION: " +  e.getMessage());
            return null;

        }
        return conn;
    }
}

