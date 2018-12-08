package com.codecool.guestbook.DAO;

import com.codecool.guestbook.model.Content;

import java.util.List;

public interface EntriesDAO {
    List<Content> getAll();

    void insert(String content, String author);

}
