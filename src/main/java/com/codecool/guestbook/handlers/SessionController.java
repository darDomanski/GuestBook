package com.codecool.guestbook.handlers;

import com.codecool.guestbook.DAO.SessionDAO;
import com.codecool.guestbook.DAO.SessionDAOImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;

public class SessionController implements HttpHandler {

    private SessionDAO sessionDAO;
    private RedirectController redirectController;

    public SessionController() {
        this.sessionDAO = new SessionDAOImpl();
        this.redirectController = new RedirectController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String context = "";
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");

        if (cookieStr == null) {
            context = "login";
        } else {
            HttpCookie cookie = HttpCookie.parse(cookieStr).get(0);
            int userId = sessionDAO.getUserIdBySession(cookie.getValue());
            if (userId == -1) {
                context = "login";
            } else {
                context = "guestbook";
            }
        }
        redirectController.redirect(httpExchange, context);
    }
}
