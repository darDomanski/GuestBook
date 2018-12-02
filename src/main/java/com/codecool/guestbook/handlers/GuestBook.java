package com.codecool.guestbook.handlers;

import com.codecool.guestbook.DAO.DAO;
import com.codecool.guestbook.DAO.DBDAO;
import com.codecool.guestbook.model.Content;
import com.codecool.guestbook.model.Entry;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.*;

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
        String cookieString = httpExchange.getRequestHeaders().getFirst("Cookie");

        System.out.println("wszystkie cookie! " + cookieString);

        System.out.println(httpExchange.getRequestHeaders().entrySet());
        boolean isNewSession = true;
        boolean isLogged = false;
        HttpCookie sessionCookie;
        HttpCookie loginStatusCookie;

        if (cookieString != null) {  // Cookie already exists
            sessionCookie = HttpCookie.parse(cookieString).get(0);
            System.out.println("session cookie " + sessionCookie.toString());
            isNewSession = false;

            loginStatusCookie = HttpCookie.parse(cookieString).get(1);
            System.out.println(" login " + loginStatusCookie.toString());

            if (loginStatusCookie == null) {
                isLogged = false;
            } else {
                isLogged = Boolean.valueOf(loginStatusCookie.getValue());
            }
        } else { // Create a new cookie
            isNewSession = true;
            isLogged = false;

            sessionCookie = new HttpCookie("sessionId", UUID.randomUUID().toString());

            httpExchange.getResponseHeaders().add("Set-Cookie", sessionCookie.toString());
        }


        if (isNewSession || !isLogged) {
            System.out.println("trzeba sie zalogowac");
            if (method.equals("GET")) {
                System.out.println("logowanie metoda get");

                ClassLoader classLoader = getClass().getClassLoader();
                File loginForm = new File(classLoader.getResource("static/login.html").getFile());
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(loginForm));

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String response = "";
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();

                httpExchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
            }

            if (method.equals("POST")) {

                System.out.println("logowanie metoda post");
                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                System.out.println(formData);
                Map inputs = parseFormData(formData);

                String username = (String) inputs.get("username");
                String password = (String) inputs.get("password");

                isLogged = dao.validateLogin(username, password);
                loginStatusCookie = new HttpCookie("isLogged", String.valueOf(isLogged));
                httpExchange.getResponseHeaders().add("Set-Cookie", loginStatusCookie.toString());

                if (isLogged) {

                    List<Content> entries = dao.getAll();

                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/guestbook.twig");
                    JtwigModel model = JtwigModel.newModel();

                    model.with("entries", entries);

                    String response = template.render(model);

                    httpExchange.sendResponseHeaders(200, response.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    ClassLoader classLoader = getClass().getClassLoader();
                    File loginForm = new File(classLoader.getResource("static/login.html").getFile());
                    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(loginForm));

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String response = "";
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();

                    httpExchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
                    httpExchange.sendResponseHeaders(200, response.length());
                    OutputStream outputStream = httpExchange.getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.close();
                }

            }
        } else if (isLogged) {
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

        }





    }
}
