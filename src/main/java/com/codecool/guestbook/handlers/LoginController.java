package com.codecool.guestbook.handlers;

import com.codecool.guestbook.DAO.LoginDataDAO;
import com.codecool.guestbook.DAO.LoginDataDAOImpl;
import com.codecool.guestbook.DAO.SessionDAO;
import com.codecool.guestbook.DAO.SessionDAOImpl;
import com.codecool.guestbook.helpers.FormParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Map;
import java.util.UUID;


public class LoginController implements HttpHandler {

    private RedirectController redirectController;
    private LoginDataDAO loginDataDAO;
    private SessionDAO sessionDAO;
    private FormParser formParser;

    public LoginController() {
        this.loginDataDAO = new LoginDataDAOImpl();
        this.sessionDAO = new SessionDAOImpl();
        this.redirectController = new RedirectController();
        this.formParser = new FormParser();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();
        System.out.println(method);

        if (method.equals("GET")) {
            System.out.println("metoda get");
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.twig");
            JtwigModel model = JtwigModel.newModel();
            response = template.render(model);
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        if (method.equals("POST")) {
            System.out.println("metoda post");
            String context = "";
            String formData = formParser.getFormDataFromRequest(httpExchange);
            Map inputs = formParser.parseFormData(formData);

            String username = (String) inputs.get("username");
            String password = (String) inputs.get("password");

            boolean userExists = loginDataDAO.validateLogin(username, password);

            if (userExists) {
                int userId = loginDataDAO.getUserIdByUsername(username);
                HttpCookie cookie = new HttpCookie("sessionId", UUID.randomUUID().toString());
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
                sessionDAO.addSession(userId, cookie.getValue());
                context = "guestbook";
            } else {
                context = "login";
            }
            redirectController.redirect(httpExchange, context);
        }

    }


//    private void removeSessionIfCookieExists(HttpExchange httpExchange) {
//        String cookieString = httpExchange.getRequestHeaders().getFirst("Cookie");
//
//        if (cookieString != null) {
//            HttpCookie cookie = HttpCookie.parse(cookieString).get(0);
//            sessionDAO.removeSession(cookie.getValue());
//        }
//    }
}