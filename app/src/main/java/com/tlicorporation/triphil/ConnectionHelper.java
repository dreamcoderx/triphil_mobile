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

public class ConnectionHelper {

    private static String _user = "sa";
    private static String _pass = "root";
    private static String _db = "192.168.100.26";
    private static String _server = "triphil";

    @SuppressLint("NewApi")
    public static Connection CONN(Context context) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _db
                    + ";user=" + _user
                    + ";password=" + _pass + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }
    public void set_user(String _user) {
        ConnectionHelper._user = _user;
    }
    public void set_pass(String _pass) {
        ConnectionHelper._pass = _pass;
    }
    public void set_db(String _db) {
        ConnectionHelper._db = _db;
    }
    public void set_server(String _server) { ConnectionHelper._server = _server;
    }
}
