package com.example.user.broncobooks;

import java.io.Serializable;

public class User implements Serializable {
    public String email;
    public String displayName;

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }
}