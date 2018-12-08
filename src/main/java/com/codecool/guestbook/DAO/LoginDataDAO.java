package com.codecool.guestbook.DAO;

public interface LoginDataDAO {
    boolean validateLogin(String username, String password);

    int getUserIdByUsername(String username);

    String getUserNameById(int id);
}
