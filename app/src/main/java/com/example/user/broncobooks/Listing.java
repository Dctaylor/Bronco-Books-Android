package com.example.user.broncobooks;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Listing implements Serializable {
    public Textbook textbook;
    public User seller;
    public double price;
    public String paymentMethod;
    public int epochTimePosted;	// this is the epoch time (in seconds!) that the listing was posted

    public User buyer;
    public boolean onSale;
    public boolean purchaseConfirmed;

    public String id;

    public Listing() {
        // default constructor needed for calls to DataSnapshot.getValue(Listing.class)
    }

    public Listing(Textbook textbook, User seller, double price, String paymentMethod, int epochTimePosted, String id) {
        this.textbook = textbook;
        this.seller = seller;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.epochTimePosted = epochTimePosted;
        this.onSale = true;
        this.id = id;
        purchaseConfirmed = false;
    }

    @Exclude
    public void setBuyer(User buyerToSet) {
        this.buyer = new User(buyerToSet.email, buyerToSet.displayName, buyerToSet.phoneNumber);
    }
}