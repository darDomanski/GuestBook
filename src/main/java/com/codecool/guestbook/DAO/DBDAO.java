package com.codecool.guestbook.DAO;

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

        try {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }


}
