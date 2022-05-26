package dao;

import utility.db.DbConst;

import java.sql.*;

public class Dao {

    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

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

    public void create(String query) {
        try {
            statement = connect.createStatement();
            if(!statement.execute(query)) System.out.println("insert OK!!!");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet read(String query) {
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void update(String query) {

    }

    public void delete(String query) {

    }

}
