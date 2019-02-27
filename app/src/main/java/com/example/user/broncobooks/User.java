package com.example.user.broncobooks;

public class User {
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