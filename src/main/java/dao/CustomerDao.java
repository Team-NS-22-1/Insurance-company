package dao;

import domain.customer.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao extends Dao{

    public CustomerDao() {
        super.connect();
    }

    public void create(Customer customer) {
        String query = "insert into customer (name, job, ssn, phone, email, address) values (%s, %s, %s, %s, %s, %s);";
        String customerQuery = String.format(query, customer.getName(), customer.getJob(), customer.getSsn(), customer.getPhone(), customer.getEmail(), customer.getAddress());
//        int id = super.create(customerQuery);
//        customer.setId(id);
    }

    public Customer read(int id) throws SQLException {
        Customer customer = null;
        String query = "select * from customer where customer_id = " + id;
        ResultSet rs = super.read(query);
        if (rs.next()) {
            customer = new Customer();
            customer.setId(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setJob(rs.getString("job"));
            customer.setSsn(rs.getString("ssn"));
            customer.setPhone(rs.getString("phone"));
            customer.setEmail(rs.getString("email"));
            customer.setAddress(rs.getString("address"));
        }
        return customer;
    }

    public void update(Customer customer) {

    }

    public void delete(Customer customer) {

    }
}
