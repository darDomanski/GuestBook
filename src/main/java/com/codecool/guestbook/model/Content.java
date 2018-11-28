package com.codecool.guestbook.model;

public abstract class Content {
    String content;
    String author;
    String date;

    public Content(String content, String author, String date) {
        this.content = content;
        this.author = author;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }
}
