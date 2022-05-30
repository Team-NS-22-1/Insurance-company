import application.Application;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

//        InsuranceDao insuranceDao = new InsuranceDao();
//        DevInfo devInfo = insuranceDao.readDevInfo(1);
//        System.out.println("devInfo.getSalesAuthState() + " + devInfo.getDevDate() );
        Application app = new Application();
        app.run();
    }
}