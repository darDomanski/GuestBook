package com.codecool.guestbook.handlers;

import com.codecool.guestbook.DAO.DAO;
import com.codecool.guestbook.DAO.DBConnector.DBConnector;
import com.codecool.guestbook.DAO.DBDAO;
import com.codecool.guestbook.model.Content;
import com.codecool.guestbook.model.Entry;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestBook implements HttpHandler {
    private Connection connection;
    private DAO dao;

    public GuestBook(Connection connection) {
        this.connection = connection;
        this.dao = new DBDAO(this.connection);
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            System.out.println(formData);
            Map inputs = parseFormData(formData);

            String content = (String) inputs.get("content");
            String author = (String) inputs.get("name");
            dao.insert(new Entry(content, author, Calendar.getInstance().getTime().toString()));
        }

        List<Content> entries = dao.getAll();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/guestbook.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("entries", entries);

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

        DBConnector.closeConnection();

    }
}
