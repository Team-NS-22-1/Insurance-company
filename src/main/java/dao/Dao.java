package dao;

import utility.db.DBUtil;
import utility.db.DbConst;

import java.sql.*;

public class Dao {

    protected Connection connect = null;
    protected Statement statement = null;
    protected ResultSet resultSet = null;

    public void connect() {
        try {
            Class.forName(DbConst.JDBC_DRIVER);
            connect = DriverManager.getConnection(DbConst.URL, DbConst.USERNAME, DbConst.PASSWORD);
        } catch (SQLException e) {
            // DB 접근 실패 Exception
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int create(String query) {
        int id = 0;
        try {
            statement = connect.createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
                generatedKeys.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public ResultSet read(String query) {
        try {
            statement = connect.createStatement();
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
            statement = connect.createStatement();
            result = statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close() {
        if (resultSet != null) {
            try{
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connect != null) {
            try{
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
