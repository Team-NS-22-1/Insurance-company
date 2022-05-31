package insuranceCompany.application.dao.user;

import insuranceCompany.application.dao.Dao;
import insuranceCompany.application.global.exception.LoginIdFailedException;
import insuranceCompany.application.global.exception.LoginPwFailedException;
import insuranceCompany.application.login.User;

import java.sql.SQLException;

public class UserDaoImpl extends Dao implements UserDao {
    public UserDaoImpl() {
        super.connect();
    }

    @Override
    public void create(User user) {
    }

    @Override
    public User read(int id) {
        return null;
    }

    @Override
    public int login(String userId, String password) {
        int role_id = -9999;
        try {
            String query = "SELECT * FROM user WHERE user_id = '" + userId + "';";
            super.read(query);
            if(resultSet.next()) {
                if(password.equals(resultSet.getString("password")))
                    role_id = resultSet.getInt("role_id");
                else
                    throw new LoginPwFailedException();
            }
            else
                throw new LoginIdFailedException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role_id;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

}
