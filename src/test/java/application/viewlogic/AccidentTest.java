package application.viewlogic;

import insuranceCompany.application.domain.accident.InjuryAccident;
import org.junit.jupiter.api.Test;
import insuranceCompany.application.global.utility.db.DBUtil;

import java.sql.*;

/**
 * packageName :  main.application.viewlogic
 * fileName : AccidentTest
 * author :  규현
 * date : 2022-05-18
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-18                규현             최초 생성
 */
class AccidentTest {


    @Test
    void jdbcTest() {
        Connection conn = null;
        PreparedStatement ps1 = null;

        PreparedStatement ps2 = null;

        InjuryAccident injuryAccident = new InjuryAccident();
        injuryAccident.setInjurySite("팔");
        injuryAccident.setEmployeeId(1);
        injuryAccident.setCustomerId(1);
        try {
            String qurey1 = "insert into accident (customer_id, employee_id) values (?,?)";
            String q2 = "insert into injury_accident (accident_id,injury_site) values (?,?)";
             conn = DBUtil.getConnection();
             conn.setAutoCommit(false);
             ps1 = conn.prepareStatement(qurey1,PreparedStatement.RETURN_GENERATED_KEYS);
            ps1.setInt(1,injuryAccident.getCustomerId());
            ps1.setInt(2,injuryAccident.getEmployeeId());
            ps1.executeUpdate();
            ResultSet generatedKeys = ps1.getGeneratedKeys();
            int key = 0;
            if (generatedKeys.next()) {
                    key = generatedKeys.getInt(1);
            }

            System.out.println(key);
            ps2 = conn.prepareStatement(q2);
            ps2.setInt(1,key);
            ps2.setString(2, injuryAccident.getInjurySite());
            int i = ps2.executeUpdate();
            System.out.println(i);
            conn.commit();
        }
        catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    @Test
    void jdbcTest2() {
        Connection conn = null;
        PreparedStatement ps1 = null;



        InjuryAccident injuryAccident = new InjuryAccident();
        injuryAccident.setInjurySite("팔");
        injuryAccident.setEmployeeId(1);
        injuryAccident.setCustomerId(1);
        try {
            String qurey1 = "insert into accident (customer_id) values ('"+injuryAccident.getCustomerId()+"'); ";

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            Statement statement = conn.createStatement();

            statement.executeUpdate(qurey1,Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next())
                System.out.println(generatedKeys.getInt(1));


            conn.commit();
        }
        catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }


}