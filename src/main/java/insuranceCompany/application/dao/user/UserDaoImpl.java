package dao;

import login.User;

import java.sql.SQLException;

public class UserDao extends Dao {
    public UserDao() {
        super.connect();
    }

    public void create(User user) {

    }

    public void read(int id) {

    }

    public int login(String userId, String password) {
        int role_id = -9999;
        try {
            String query = "SELECT * FROM user WHERE user_id = '" + userId + "';";
            super.read(query);
            if(resultSet.next()) {
                if(password.equals(resultSet.getString("password")))
                    role_id = resultSet.getInt("role_id");
                else
                    role_id = -1;
            }
            else
                role_id = -2;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role_id;
    }

    public void update() {

    }

    public void delete() {

    }

}
