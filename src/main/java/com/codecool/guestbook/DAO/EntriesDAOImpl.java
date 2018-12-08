package com.codecool.guestbook.DAO;

import com.codecool.guestbook.DAO.DBConnector.DBConnector;
import com.codecool.guestbook.model.Content;
import com.codecool.guestbook.model.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EntriesDAOImpl implements EntriesDAO {

    @Override
    public List<Content> getAll() {
        List<Content> entries = new ArrayList<>();
        String query = "SELECT * FROM entry";


        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection;

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
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }

    @Override
    public void insert(String content, String author) {
        String query = "INSERT INTO entry(content, author, date) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;
        Connection connection;

        try {
            connection = DBConnector.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, Calendar.getInstance().getTime().toString());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println("Can't put record into table!");
            e.printStackTrace();
        }
    }

}
