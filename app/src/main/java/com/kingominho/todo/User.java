package com.kingominho.todo;

import android.os.Bundle;

class User {

    final String USERID_KEY = "USER_ID";
    final String USER_NAME_KEY = "USER_NAME";
    final String USER_EMAIL_KEY = "USER_EMAIL";
    final String USER_PASSWORD_KEY = "USER_PASSWORD";

    private String userName, userEmail, userPassword;
    private int userId;

    User(String userEmail, String userName, String userPassword)
    {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userId = (userEmail + userName).hashCode();
    }

    public int getUserId() {
        return userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Bundle toBundle()
    {
        Bundle bundle = new Bundle();
        bundle.putInt(USERID_KEY, this.userId);
        bundle.putString(USER_EMAIL_KEY, this.userEmail);
        bundle.putString(USER_NAME_KEY, this.userName);
        bundle.putString(USER_PASSWORD_KEY, this.userPassword);

        return bundle;
    }
}
