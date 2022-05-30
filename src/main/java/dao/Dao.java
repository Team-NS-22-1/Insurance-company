package dao;

import utility.db.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {
    protected Connection connection = null;
    protected Statement statement = null;
    protected ResultSet resultSet = null;

    public void connect() {
        connection = DBUtil.getConnection();
    }

    public int create(String query) {
        connect();
        int id = 0;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next())
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

    public boolean update(String query) {
        connect();
        int resultRows = executeUpdate(query);
        return resultRows > 0;
    }

    public boolean delete(String query) {
        connect();
        int resultRows = executeUpdate(query);
        return resultRows > 0;
    }

    private int executeUpdate(String query) {
        int result = 0;
        try {
            statement = connection.createStatement();
            result = statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try{
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (con != null) {
            try{
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
