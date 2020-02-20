package com.myapps.toualbiamine.food2class.Model;

public class User {     //No need to store the email address since it will be the primary key retrieved directly from db.

    private String name;
    private String password;
    private String email;
    private String flagCount;
    private boolean isStaff;

    public User() { }

    public User(String email, String name, String password, String flagCount, boolean isStaff) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.flagCount = flagCount;
        this.isStaff = isStaff;
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
    public String getFlagCount() { return flagCount; }
    public void setFlagCount(String flagCount) { this.flagCount = flagCount; }
    public boolean getIsStaff() { return isStaff; }
    public void setIsStaff(boolean isStaff) { this.isStaff = isStaff; }
}
