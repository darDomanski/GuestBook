package com.codecool.guestbook.handlers;

import com.codecool.guestbook.DAO.SessionDAO;
import com.codecool.guestbook.DAO.SessionDAOImpl;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpCookie;

public class SessionResolver {
    private RedirectController redirectController;
    private SessionDAO sessionDAO;

    public SessionResolver() {
        this.redirectController = new RedirectController();
        this.sessionDAO = new SessionDAOImpl();
    }

    public void checkIfSessionIsValid(HttpExchange httpExchange) throws IOException {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");

        if (cookieStr == null) {
            redirectController.redirect(httpExchange, "login");
        } else {
            HttpCookie cookie = HttpCookie.parse(cookieStr).get(0);
            int userId = sessionDAO.getUserIdBySession(cookie.getValue());

            if (userId == -1) {
                redirectController.redirect(httpExchange, "login");
            }
        }
    }
}
