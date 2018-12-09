package com.codecool.guestbook.handlers;

import com.codecool.guestbook.DAO.*;
import com.codecool.guestbook.helpers.FormParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Map;

public class GuestBookController implements HttpHandler {
    private SessionResolver sessionResolver;
    private FormParser formParser;
    private EntriesDAO entriesDAO;
    private SessionDAO sessionDAO;
    private LoginDataDAO loginDataDAO;

    public GuestBookController() {
        this.sessionResolver = new SessionResolver();
        this.formParser = new FormParser();
        this.entriesDAO = new EntriesDAOImpl();
        this.sessionDAO = new SessionDAOImpl();
        this.loginDataDAO = new LoginDataDAOImpl();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        sessionResolver.checkIfSessionIsValid(httpExchange);

        String method = httpExchange.getRequestMethod();

        if (method.equalsIgnoreCase("post")) {
            Map inputs = formParser.parseFormData(formParser.getFormDataFromRequest(httpExchange));
            entriesDAO.insert((String) inputs.get("content"), (String) inputs.get("name"));
        }
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/guestbook.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("user", getUserName(httpExchange));
        model.with("entries", entriesDAO.getAll());

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String getUserName(HttpExchange httpExchange) {
        String username = "";
        String cookieString = httpExchange.getRequestHeaders().getFirst("Cookie");

        if (cookieString != null) {
            HttpCookie cookie = HttpCookie.parse(cookieString).get(0);

            int userId = sessionDAO.getUserIdBySession(cookie.getValue());
            username = loginDataDAO.getUserNameById(userId);
        }
        return username;
    }
}
