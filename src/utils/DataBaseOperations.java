package utils;

import java.sql.*;

public class DataBaseOperations {

    /*public static Connection connectToDb() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String DB_URL = "jdbc:mysql://localhost:3306/bookshop";
            String USER = "root";
            String PASS = "";
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException t) {
            t.printStackTrace();
        }
        return conn;
    }*/

    /*public static void disconnectFromDb(Connection connection, Statement statement) {

        try {
            if (connection != null && statement != null) {
                connection.close();
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public static void deleteUser(int userId) {
        Connection connection = connectToDb();
        String sql = "DELETE FROM users where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    /*public static void updateField(String dbColName, String value, int userId) {
        Connection connection = connectToDb();
        String sql = "UPDATE user SET " + dbColName + " = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, userId);
            preparedStatement.execute();
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    /*    public static User getUserByLogin(String login) {
            try {
                Connection connection = connectToDb();
                String sql = "SELECT * FROM users WHERE login = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, login);
                ResultSet rs = preparedStatement.executeQuery();
                *//*if(rs.getString(7) != null) Person person = new Person();else Company company = new Company();*//*
            //return person arba company
            disconnectFromDb(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*public static int getUserByLogin(String login, Connection connection) {
        int id = 0;
        try {
            String sql = "SELECT id FROM users WHERE login = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                id = rs.getInt(1);
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }*/
}
