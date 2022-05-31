import dao.Dao;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        Dao dao = new Dao();
        dao.connect();
//        Application app = new Application();
//        app.run();
    }
}