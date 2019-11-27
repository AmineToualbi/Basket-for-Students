package com.myapps.toualbiamine.food2class.Model;

import android.arch.persistence.room.Ignore;

public class User {     //No need to store the email address since it will be the primary key retrieved directly from db.

    private String name;
    private String password;
    private String email;
    private boolean isStaff;
    private int flagCount;

    public User() {

    }

    public User(String email, String name, String password, boolean isStaff, int flagCount) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.isStaff = isStaff;
        this.flagCount = flagCount;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFlagCount () { return this.flagCount; }







}
