package com.codecool.guestbook.DAO;

import com.codecool.guestbook.DAO.DBConnector.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionDAOImpl implements SessionDAO {

    @Override
    public void addSession(int userId, String sessionId) {
        String query = "INSERT INTO session VALUES (?, ?);";

        PreparedStatement preparedStatement;
        Connection connection;
        try {
            connection = DBConnector.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, sessionId);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSession(String sessionId) {
        String query = "DELETE FROM session WHERE session_id = ?;";

        PreparedStatement preparedStatement;
        Connection connection;
        try {
            connection = DBConnector.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sessionId);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getUserIdBySession(String sessionId) {
        int userId = -1;
        String query = "SELECT * FROM session WHERE session_id = ?;";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        Connection connection;
        try {
            connection = DBConnector.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sessionId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }
}
