package domain.accident;

import lombok.extern.slf4j.Slf4j;
import utility.db.DBUtil;

import java.sql.*;
import java.util.List;

/**
 * packageName :  domain.accident
 * fileName : JDBCAccidentListImpl
 * author :  규현
 * date : 2022-05-24
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-24                규현             최초 생성
 */
public class JDBCAccidentListImpl implements AccidentList{
    @Override
    public List<Accident> readAllByCustomerId(int customerId) {
        return null;
    }

    @Override
    public List<Accident> readAllByEmployeeId(int employeeid) {
        return null;
    }

    @Override
    public void update(Accident accident) {

    }

    @Override
    public void create(Accident accident) {

        Connection conn = null;
        PreparedStatement pstm = null;

        try{
            String query = "insert into accident (accident_type, employee_id, customer_id, loss_reserves, date_of_accident, " +
                    "date_of_report) values (?,?,?,?,?,?)";

            conn = getConnection();
            pstm = conn.prepareStatement(query);




        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Accident read(int id) {
        return null;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try{
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (con != null) {
            try{
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() {
        return DBUtil.getConnection();
    }
}
