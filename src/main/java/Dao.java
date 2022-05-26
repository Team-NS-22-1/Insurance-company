import application.Application;

import java.sql.*;

public class Dao {

    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public void connet() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("", "", "");
        } catch (Exception e) {

        }
    }

    public void create(String query) {
        try {

            if (!statement.execute(query));
        } catch (SQLException e) {

        }
    }

    public void update(String query) {

    }

    public void delete(String query) {

    }

    public ResultSet retrieve(String query) {
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {

        }
        return resultSet;
    }
}