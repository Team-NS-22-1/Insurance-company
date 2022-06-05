package insuranceCompany.application.dao.user;

import insuranceCompany.application.dao.CrudInterface;
import insuranceCompany.application.login.User;

public interface UserDao extends CrudInterface<User> {
    int login(String id, String password);
}
