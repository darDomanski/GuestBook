package com.codecool.guestbook.model;

import java.time.LocalDate;

public class Entry extends Content {
    public Entry(String content, String author, LocalDate date) {
        super(content, author, date);
    }
}
