package com.codecool.guestbook.DAO;

import com.codecool.guestbook.DAO.DBConnector.DBConnector;
import com.codecool.guestbook.model.Content;
import com.codecool.guestbook.model.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBDAO implements DAO {
    private Connection connection;

    public DBDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Content> getAll() {
        List<Content> entries = new ArrayList<>();
        String query = "SELECT * FROM entry";


        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
//        Connection con;

        try {
            connection = DBConnector.getConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String content = resultSet.getString("content");
                String author = resultSet.getString("author");
                String date = resultSet.getString("date");

                entries.add(new Entry(content, author, date));
            }

            preparedStatement.close();
            resultSet.close();
//            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }

    @Override
    public void insert(Content content) {
        String query = "INSERT INTO entry(content, author, date) VALUES (?, ?, ?)";

//        Connection con = DBConnector.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, content.getContent());
            preparedStatement.setString(2, content.getAuthor());
            preparedStatement.setString(3, content.getDate());
            preparedStatement.executeUpdate();

            preparedStatement.close();
//            con.close();

        } catch (SQLException e) {
            System.err.println("Can't put record into table!");
            e.printStackTrace();
        }
    }

    @Override
    public boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM LOGIN_DATA WHERE user_name=? AND password=?;";
        boolean isCorrect = false;

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                isCorrect = true;
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isCorrect;
    }

}
