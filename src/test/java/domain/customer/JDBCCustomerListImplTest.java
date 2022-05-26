package domain.customer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utility.db.DBUtil;
import utility.db.DbConst;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  domain.customer
 * fileName : JDBCCustomerListImplTest
 * author :  규현
 * date : 2022-05-24
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-24                규현             최초 생성
 */

class JDBCCustomerListImplTest {


    @Test
    void jdbcTest() {

        Customer customer = new Customer();
        customer.setName("tester2222");
        Customer findCustomer = null;
        try {
            String query = "insert into customer (name) values (?)";
            Class.forName(DbConst.JDBC_DRIVER);
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstm = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, customer.getName());
            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println("id -> " + findCustomer.getId());
        Assertions.assertEquals(findCustomer.getId(),1);
    }





}