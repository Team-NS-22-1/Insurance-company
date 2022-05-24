package utility.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static utility.db.DbConst.*;

/**
 * packageName :  utility.db
 * fileName : DBUtil
 * author :  규현
 * date : 2022-05-24
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-24                규현             최초 생성
 */
public class DBUtil {

    public static Connection getConnection() {

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
