package com.example.user.broncobooks;

import java.util.ArrayList;

public class Textbook {
    public String title;
    public String subtitle;
    public ArrayList<String> authors;
    public String publisher;
    public String publishedDate;
    public String language;
    public String edition;
    public int pages;
    public String binding;

    public Textbook() {
        // default constructor required for calls to DataSnapshot.getValue(Textbook.class)
    }

    public Textbook(String title, String subtitle, ArrayList<String> authors, String publisher, String publishedDate, String language, String edition, int pages, String binding) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.language = language;
        this.edition = edition;
        this.pages = pages;
        this.binding = binding;
    }
}