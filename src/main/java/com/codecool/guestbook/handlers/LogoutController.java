package com.codecool.guestbook.handlers;

import com.codecool.guestbook.DAO.SessionDAO;
import com.codecool.guestbook.DAO.SessionDAOImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;

public class LogoutController implements HttpHandler {
    private SessionDAO sessionDAO;
    private RedirectController redirectController;

    public LogoutController() {
        this.sessionDAO = new SessionDAOImpl();
        this.redirectController = new RedirectController();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String cookieString = httpExchange.getRequestHeaders().getFirst("Cookie");
        if (cookieString != null) {
            HttpCookie sessionIdCookie = HttpCookie.parse(cookieString).get(0);
            sessionDAO.removeSession(sessionIdCookie.getValue());
        }

        HttpCookie cookie = new HttpCookie("sessionId", "token=deleted; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT");
        cookie.setMaxAge(-1);
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());

        redirectController.redirect(httpExchange, "login");
    }
}
