package uwDao;

import application.Application;
import utility.db.DBUtil;

import java.sql.*;

public class Dao {

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public void connect() {
        connection = DBUtil.getConnection();
    }

    public int create(String query) {
        connect();
        int id = 0;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                id = generatedKeys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public ResultSet read(String query) {
        connect();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void update(String query, String[] columnNames) {
        connect();
        try {
            statement = connection.createStatement();
            int i  = statement.executeUpdate(query, columnNames);
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(null);
        }
    }

    public void delete(String query) {

    }

    public void close(ResultSet rs) {
        if (rs != null) try { rs.close(); } catch(Exception e) {e.printStackTrace();}
        if (statement != null) try { statement.close(); } catch(Exception e) {e.printStackTrace();}
        if (connection != null) try { connection.close(); } catch(Exception e) {e.printStackTrace();}
    }
}