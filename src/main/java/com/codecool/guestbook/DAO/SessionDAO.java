package com.codecool.guestbook.DAO;

public interface SessionDAO {
    void addSession(int userId, String sessionId);

    void removeSession(String sessionId);

    int getUserIdBySession(String sessionId);
}
