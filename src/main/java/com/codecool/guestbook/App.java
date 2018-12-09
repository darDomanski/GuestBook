package com.codecool.guestbook;

import com.codecool.guestbook.handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.Calendar;


public class App {


    public static void main(String[] args) throws Exception {
        System.out.println(Calendar.getInstance().getTime().toString());
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        System.out.println("Server created successfully!");

        server.createContext("/", new SessionController());
        server.createContext("/guestbook", new GuestBookController());
        server.createContext("/login", new LoginController());
        server.createContext("/logout", new LogoutController());
        server.createContext("/static", new Static());

        server.setExecutor(null);
        System.out.println("Server's routes set!");

        server.start();
        System.out.println("Server starts listening on port 8000...");

    }
}

