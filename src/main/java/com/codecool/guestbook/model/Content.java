package com.codecool.guestbook.model;

import java.time.LocalDate;

public abstract class Content {
    String content;
    String author;
    LocalDate date;

    public Content(String content, String author, LocalDate date) {
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

    public LocalDate getDate() {
        return date;
    }
}
