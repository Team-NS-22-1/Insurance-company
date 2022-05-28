package dao;

import utility.db.DBUtil;
import utility.db.DbConst;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void update(String query) {

    }

    public void delete(String query) {

    }

}
