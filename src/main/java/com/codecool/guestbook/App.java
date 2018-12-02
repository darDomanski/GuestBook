package com.codecool.guestbook;

import com.codecool.guestbook.DAO.DBConnector.DBConnector;
import com.codecool.guestbook.handlers.Cookie;
import com.codecool.guestbook.handlers.GuestBook;
import com.codecool.guestbook.handlers.Static;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;


public class App {

    private Connection connection = DBConnector.getConnection();

    public static void main(String[] args) throws Exception {
        System.out.println(Calendar.getInstance().getTime().toString());
        App app = new App();
        app.runServer();

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

                    server.createContext("/", new GuestBook(this.connection));
                    server.createContext("/static", new Static());
                    server.createContext("/cookie", new Cookie());
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
                    DBConnector.closeConnection();
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

