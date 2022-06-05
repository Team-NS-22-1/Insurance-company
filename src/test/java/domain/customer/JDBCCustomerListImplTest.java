package domain.customer;

import insuranceCompany.application.domain.customer.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import insuranceCompany.application.global.utility.db.DBUtil;

import java.sql.*;

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



        }
        catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println("id -> " + findCustomer.getId());
        Assertions.assertEquals(findCustomer.getId(),1);
    }





}