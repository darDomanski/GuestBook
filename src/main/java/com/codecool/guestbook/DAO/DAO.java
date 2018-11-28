package com.codecool.guestbook.DAO;

import com.codecool.guestbook.model.Content;

import java.util.List;

public interface DAO {
    List<Content> getAll();

    void insert(Content content);
}
