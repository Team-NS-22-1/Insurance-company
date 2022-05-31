package insuranceCompany.application.dao;

import insuranceCompany.application.global.utility.db.DbConst;

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
            System.out.println("ERROR:: DB CONNECTION");
            System.out.println("현재 시스템의 장애가 발생하여 일시적으로 정보를 출력할 수 없습니다.\n" +
                    "고객센터(1588-0001)로 연락주시기 바랍니다.");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR:: JDBC DRIVER");
            System.out.println("현재 시스템의 장애가 발생하여 일시적으로 정보를 출력할 수 없습니다.\n" +
                    "고객센터(1588-0001)로 연락주시기 바랍니다.");
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
        connect();
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public boolean update(String query) {
        int resultRows = executeUpdate(query);
        return resultRows > 0;
    }

    public boolean delete(String query) {
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
