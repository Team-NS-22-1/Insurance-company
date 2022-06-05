package insuranceCompany.application.domain.dao.user;

import insuranceCompany.application.domain.dao.CrudInterface;
import insuranceCompany.application.login.User;

public interface UserDao extends CrudInterface<User> {
    int login(String id, String password);
}
