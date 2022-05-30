package uwDao;

import domain.contract.*;
import domain.customer.Customer;
import domain.payment.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDao extends Dao {

    public CustomerDao() {
        super.connect();
    }


    public void create(Contract contract) {


    }

    public void update(Contract contract) {
        String query = "";
        //super.update(query);
    }

    public Customer read(int id) {
        String query = "SELECT *\n" +
                        "FROM customer\n" +
                        "WHERE customer_id = " + id;

        String paymentQuery = "SELECT *\n" +
                "FROM customer c\n" +
                "LEFT JOIN payment p\n" +
                "ON c.customer_id = p.customer_id\n" +
                "LEFT JOIN account ac\n" +
                "ON p.payment_id = ac.payment_id\n" +
                "LEFT JOIN card cd\n" +
                "ON p.payment_id = cd.payment_id\n" +
                "WHERE c.customer_id = " + id;

        Customer customer = new Customer();
        ArrayList<Payment> paymentList = new ArrayList<>();

        try {
            ResultSet rs = super.read(query);

            if (rs.next()) {
                customer.setId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setJob(rs.getString("job"));
                customer.setSsn(rs.getString("ssn"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmail(rs.getString("email"));
                customer.setAddress(rs.getString("address"));
            }
            if (rs != null) rs.close();

            ResultSet paymentRs = super.read(paymentQuery);

            while (paymentRs.next()) {

                if (PayType.valueOf(paymentRs.getString("pay_type")).equals(PayType.CARD)) {
                    Card card = new Card();
                    card.setCardNo(paymentRs.getString("card_no"));
                    card.setPaytype(PayType.valueOf(paymentRs.getString("pay_type")));
                    card.setId(paymentRs.getInt("payment_id"));
                    card.setCustomerId(paymentRs.getInt("customer_id"));
                    card.setCvcNo(paymentRs.getString("cvc_no"));
                    card.setExpiryDate(paymentRs.getDate("expiry_date").toLocalDate());
                    card.setCardType(CardType.valueOf(paymentRs.getString("card_type")));
                    paymentList.add(card);
                } else if (PayType.valueOf(paymentRs.getString("pay_type")).equals(PayType.ACCOUNT)) {
                    Account account = new Account();
                    account.setId(paymentRs.getInt("payment_id"));
                    account.setAccountNo(paymentRs.getString("account_no"));
                    account.setCustomerId(paymentRs.getInt("customer_id"));
                    account.setBankType(BankType.valueOf(paymentRs.getString("bank_type")));
                    account.setPaytype(PayType.valueOf(paymentRs.getString("pay_type")));
                    paymentList.add(account);
                }
            }

            customer.setPaymentList(paymentList);
            super.close(paymentRs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }




}