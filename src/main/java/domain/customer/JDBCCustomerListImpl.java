package domain.customer;

import dao.Dao;
import utility.db.DBUtil;
import utility.db.DbConst;

import java.sql.*;

/**
 * packageName :  domain.customer
 * fileName : JDBCCustomerListImpl
 * author :  규현
 * date : 2022-05-24
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-24                규현             최초 생성
 */
public class JDBCCustomerListImpl extends Dao implements CustomerList{

    @Override
    public void create(Customer customer) {
            String query = "insert into customer (name, job, email, phone, ssn, address) values ('%s', '%s','%s','%s','%s', '%s)";
            String formattedQuery =  String.format(query, customer.getName(), customer.getJob(), customer.getEmail(), customer.getPhone(), customer.getSsn()
            ,customer.getAddress());
            int id = super.create(formattedQuery);
            customer.setId(id);
    }

    @Override
    public Customer read(int id) {
        Customer customer = null;
        String query = "select * from customer where customer_id = "+id;
        try {
        ResultSet rs = super.read(query);
            if (rs.next()) {
                customer = new Customer();
                customer.setId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setJob(rs.getString("job"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setSsn(rs.getString("ssn"));
                customer.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }


    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            String query = "delete from customer where customer_id = ?";
            Class.forName(DbConst.JDBC_DRIVER);
            conn = DBUtil.getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn,pstm,rs);
        }
        return true;
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
}
