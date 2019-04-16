package com.example.user.broncobooks;

import java.io.Serializable;

public class User implements Serializable {
    public String email;
    public String displayName;
    public String phoneNumber;

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String displayName, String phoneNumber) {
        this.email = email;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }
}