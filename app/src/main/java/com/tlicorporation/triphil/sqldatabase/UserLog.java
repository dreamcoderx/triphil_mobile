package com.tlicorporation.triphil.sqldatabase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.activities.ReferenceNumberActivity;
import com.tlicorporation.triphil.custom.MessageBox;
import com.tlicorporation.triphil.helpers.singleToneClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserLog {
    int userID;
    String userName;
    String password;
    String email;
    String UserRole;

    Context context;
    String msg;

    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }

    public UserLog(Context ctx){
        context = ctx;
    }

    public void dispose(){
        this.dispose();
    }

    public boolean isUserValid() {
        try {
            Connection con = ConnectionClass.CONN(context);
            if (con == null) {
                    this.setMsg("Database Error!");
                    return false;
            }

            String qry = "select userid, username, password, email, user_role" +
                         " from tblusers " +
                         " where username = ?" +
                         " and password= ?";
            PreparedStatement stmt = con.prepareStatement(qry);


            stmt.setString(1,this.getUserName());
            stmt.setString(2, this.getPassword());
            ResultSet rs = stmt.executeQuery( );

            if (rs.next() ) {
               this.setUserID(rs.getInt("userid"));
               this.setUserName(rs.getString("username"));
               this.setPassword(rs.getString("password"));
               this.setEmail(rs.getString("email"));
               this.setUserRole(rs.getString("user_role"));
               return true;
            }

            this.setMsg("Invalid Username or Password!");
            return false;

        }catch (Exception e){
            this.setMsg(e.getMessage());
            Log.e("error", "");
            return false;
        }
    }

    public boolean createUser(){
        try {
            Connection connection = ConnectionClass.CONN(context);
            String queryStmt = "Insert into tblusers " +
                    " (username, password, user_role) values "
                    + "('"
                    + userName
                    + "','"
                    + password
                    + "','" + UserRole+ "')";
            PreparedStatement preparedStatement = connection.prepareStatement(queryStmt);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            setMsg("Added successfully");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            setMsg(e.getMessage());
            return false;
        } catch (Exception e) {
            setMsg(e.getMessage());
            return false;
        }
    }
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }
}




