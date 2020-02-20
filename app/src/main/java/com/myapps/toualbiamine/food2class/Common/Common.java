package com.myapps.toualbiamine.food2class.Common;

import com.myapps.toualbiamine.food2class.Model.User;

// Common data that is shared across the app.
public class Common {
    public static User currentUser;
    public static int orderID = 0;
    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static final String NAME_KEY = "Name";
    public static final String FLAG_COUNT = "FlagCount";

    public static final String BAN_TITLE = "Account Suspended";
    public static final String BAN_MSG = "Sorry, it seems like your account is suspended because of multiple orders flagged by employees.";
}
