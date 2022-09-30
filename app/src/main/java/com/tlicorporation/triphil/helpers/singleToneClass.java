package com.tlicorporation.triphil.helpers;

public class singleToneClass {
    String user_role;
    Integer user_id;

    private static final singleToneClass ourInstance = new singleToneClass();

    public static singleToneClass getInstance() {
        return ourInstance;
    }
    private singleToneClass() {
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
