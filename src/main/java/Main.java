import application.Application;
import dao.CustomerDao;
import domain.customer.Customer;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

        Application app = new Application();
        app.run();
    }
}