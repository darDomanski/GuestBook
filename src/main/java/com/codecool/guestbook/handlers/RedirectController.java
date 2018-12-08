package com.codecool.guestbook.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class RedirectController {
    public void redirect(HttpExchange httpExchange, String contextName) throws IOException {

        Headers req = httpExchange.getRequestHeaders();
        Headers map = httpExchange.getResponseHeaders();
        String host = req.getFirst("Host");
        String location = "http://" + host + "/" + contextName;

        map.set("Content-Type", "text/html");
        map.set("Location", location);
        httpExchange.sendResponseHeaders(302, -1);
        httpExchange.close();
    }
}
