package domain.customer;

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
public class JDBCCustomerListImpl implements CustomerList{
    @Override
    public void create(Customer customer) {
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            String query = "insert into customer (name, job, ssn, phone, email,address) values (?,?,?,?,?,?)";
            Class.forName(DbConst.JDBC_DRIVER);
             conn = DBUtil.getConnection();
             pstm = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, customer.getName());
            pstm.setString(2, customer.getJob());
            pstm.setString(3, customer.getSsn());
            pstm.setString(4, customer.getPhone());
            pstm.setString(5, customer.getEmail());
            pstm.setString(6, customer.getAddress());
            pstm.executeUpdate();
            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                    generatedKeys.close();
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn,pstm,null);
        }
    }

    @Override
    public Customer read(int id) {
        Customer customer = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
          String query = "select * from customer where customer_id = ?";
            Class.forName(DbConst.JDBC_DRIVER);
             conn = DBUtil.getConnection();
             pstm = conn.prepareStatement(query);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn,pstm,rs);
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
