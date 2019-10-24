package com.kingominho.todo;

import android.os.Bundle;

class User {

    final static String USERID_KEY = "USER_ID";
    final static String USER_NAME_KEY = "USER_NAME";
    final static String USER_EMAIL_KEY = "USER_EMAIL";
    final static String USER_PASSWORD_KEY = "USER_PASSWORD";

    private String userName, userEmail, userPassword;
    private int userId;

    private int getHashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userEmail == null) ? 0: userEmail.hashCode());
        result = prime * result + ((userName == null) ? 0: userName.hashCode());
        result = prime * result + ((userPassword == null) ? 0: userPassword.hashCode());
        return result;
    }

    User(String userEmail, String userName, String userPassword)
    {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        //this.userId = this.getHashCode();
        this.userId = 0;
    }

    User(Integer userId, String userEmail, String userName)
    {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userId = Integer.valueOf(userId);
        this.userPassword = null;
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
