package com.codecool.guestbook;

import com.codecool.guestbook.handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;


public class App {


    public static void main(String[] args) throws Exception {
//        System.out.println(Calendar.getInstance().getTime().toString());
//        App app = new App();
//        app.runServer();
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

    private void runServer() throws Exception {
        // create a server on port 8000
        HttpServer server = null;

        boolean isWorking = true;

        while (isWorking) {
            System.out.println("Type '1' to turn on the server, '2' to turn the server off, or '0' to exit: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    server = HttpServer.create(new InetSocketAddress(8000), 0);
                    System.out.println("Server created successfully!");

                    server.createContext("/", new SessionController());
                    server.createContext("/guestbook", new GuestBookController());
                    server.createContext("/login", new LoginController());

                    server.setExecutor(null);
                    System.out.println("Server's routes set!");

                    server.start();
                    System.out.println("Server starts listening on port 8000...");
                    break;
                case 2:
                    if (server != null) {
                        server.stop(0);
                    }
                    System.out.println("Server stopped.");
                    break;
                case 0:
                    if (server != null) {
                        server.stop(0);
                    }
                    System.out.println("Server stopped, database connection closed, exiting...");
                    isWorking = false;
                    break;
                default:
                    System.out.println("No such a option, type '1', '2' or '0'!");
                    break;
            }

        }

    }


    private int getIntInput() {
        int input = 0;
        boolean isInputANumber = false;

        do {
            Scanner scanner = new Scanner(System.in);
            try {
                input = scanner.nextInt();
                isInputANumber = true;
            } catch (InputMismatchException e) {
                System.out.println("It's not a number!");
            }
        } while (!isInputANumber);

        return input;
    }
}

