package com.codecool.guestbook.DAO.DBConnector;


import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {

    private static BasicDataSource dataSource = new BasicDataSource();
    private static Connection connection;

    static {
        dataSource.setUrl("jdbc:postgresql://localhost:5432/guestbook");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    private DBConnector(){ }

    public static Connection getConnection() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Can't close connection!");
                e.printStackTrace();
            }
        }
    }
}