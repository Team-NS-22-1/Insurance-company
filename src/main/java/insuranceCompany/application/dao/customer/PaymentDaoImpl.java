package insuranceCompany.application.dao.customer;

import insuranceCompany.application.dao.Dao;
import insuranceCompany.application.domain.customer.payment.*;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static insuranceCompany.application.global.constant.CustomerViewLogicConstants.NO_PAYMENT_ON_CUSTOMER;

/**
 * packageName :  dao
 * fileName : PaymentDao
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
public class PaymentDaoImpl extends Dao implements PaymentDao {

    public PaymentDaoImpl() {
        super.connect();
    }

    @Override
    public void create(Payment payment) {
        String query = "insert into payment (pay_type, customer_id) values ('%s', %d)";
        String formattedQuery = String.format(query,payment.getPaytype().name(),payment.getCustomerId());
        int id = super.create(formattedQuery);
        payment.setId(id);

        // detail
        String detail_query = "";
        String detailFormat = "";
        switch (payment.getPaytype()) {
            case CARD -> {
                detail_query = "insert into card (payment_id, card_no, card_type, cvc_no, expiry_date)" +
                        "values (%d, '%s', '%s', '%s', '%s')";
                detailFormat = String.format(detail_query,payment.getId(),((Card)payment).getCardNo(),((Card)payment).getCardType(),
                        ((Card)payment).getCvcNo(),((Card)payment).getExpiryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            case ACCOUNT -> {
                detail_query = "insert into account (payment_id, account_no, bank_type)" +
                        "values(%d, '%s', '%s')";
                    detailFormat = String.format(detail_query,payment.getId(), ((Account)payment).getAccountNo(),((Account)payment).getBankType().name());
            }
        }

        super.create(detailFormat);
        close();
    }

    @Override
    public Payment read(int id) {
        Payment payment = null;
        try {
            String query = "select * from payment where payment_id = %d";
            String formattedQuery = String.format(query,id);
            ResultSet rs = super.read(formattedQuery);
            if (rs.next()) {
                payment = getPayment(rs,id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }

        if (payment == null) {
            throw new MyIllegalArgumentException(id + "는 없는 ID 입니다. 다시 확인해주세요.");
        }

        return payment;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<Payment> findAllByCustomerId(int customerId) {
        List<Payment> paymentList = new ArrayList<>();

            try {
                String query = "select * from payment where customer_id = %d";
                String formatted = String.format(query,customerId);
                ResultSet rs = super.read(formatted);
                while (rs.next()) {
                    Payment payment = getPayment(rs, rs.getInt("payment_id"));
                    paymentList.add(payment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                close();
            }

        if(paymentList.isEmpty())
            throw new MyIllegalArgumentException(NO_PAYMENT_ON_CUSTOMER);
        return  paymentList;
    }

    private Payment getPayment(ResultSet rs, int id) {
        Payment payment = null;
        try {
            PayType payType = PayType.valueOf(rs.getString("pay_type").toUpperCase());
            switch (payType) {
                case CARD -> {
                    String detailQuery = "select * from card where payment_id = %d";
                    String formattedDetailQuery = String.format(detailQuery,id);
                    ResultSet detailRs = read(formattedDetailQuery);
                    payment = new Card();
                    if (detailRs.next()) {

                        ((Card)payment).setCardNo(detailRs.getString("card_no"))
                                .setCvcNo(detailRs.getString("cvc_no"))
                                .setExpiryDate(detailRs.getDate("expiry_date").toLocalDate())
                                .setCardType(CardType.valueOf(detailRs.getString("card_type").toUpperCase()));
                    }
                }
                case ACCOUNT -> {
                    String detailQuery = "select * from account where payment_id = %d";
                    String formattedDetailQuery = String.format(detailQuery,id);
                    ResultSet detailRs = read(formattedDetailQuery);
                    payment = new Account();
                    if (detailRs.next()) {
                        ((Account)payment).setAccountNo(detailRs.getString("account_no"))
                                .setBankType(BankType.valueOf(detailRs.getString("bank_type").toUpperCase()));
                    }
                }
            }
            payment.setCustomerId(rs.getInt("customer_id"))
                    .setPaytype(payType)
                    .setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return payment;
    }
}
