package com.codecool.guestbook.DAO;

import com.codecool.guestbook.model.Content;

import java.util.List;

interface DAO {
    List<Content> getAll();
}
