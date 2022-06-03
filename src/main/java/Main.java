import application.Application;
import dao.InsuranceDao;

public class Main {
    public static void main(String[] args) {
        InsuranceDao insuranceDao = new InsuranceDao();
        insuranceDao.connect();
        Application app = new Application();
        app.run();
    }
}