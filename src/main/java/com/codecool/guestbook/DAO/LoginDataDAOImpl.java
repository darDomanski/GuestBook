package com.codecool.guestbook.DAO;

import com.codecool.guestbook.DAO.DBConnector.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDataDAOImpl implements LoginDataDAO {

    @Override
    public boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM LOGIN_DATA WHERE user_name=? AND password=?;";
        boolean isCorrect = false;

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        Connection connection;
        try {
            connection = DBConnector.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                isCorrect = true;
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isCorrect;
    }

    @Override
    public int getUserIdByUsername(String username) {
        int id = -1;
        String query = "SELECT id FROM login_data WHERE user_name=?;";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        Connection connection;
        try {
            connection = DBConnector.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public String getUserNameById(int id) {
        String username = null;
        String query = "SELECT user_name FROM login_data WHERE id=?;";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        Connection connection;
        try {
            connection = DBConnector.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                username = resultSet.getString("user_name");
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }


}
