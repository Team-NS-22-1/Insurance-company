package dao;

import insuranceCompany.application.domain.dao.customer.PaymentDaoImpl;
import insuranceCompany.application.domain.payment.*;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  dao
 * fileName : PaymentDaoTest
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
class PaymentDaoTest {

    PaymentDaoImpl dao = new PaymentDaoImpl();

    @Test
    void create_account() {
        Account account = new Account();
        account.setAccountNo("test-test")
                .setBankType(BankType.KB)
        .setPaytype(PayType.ACCOUNT)
                .setCustomerId(19);
        dao.create(account);

        Payment read = dao.read(account.getId());

        System.out.println(read);
    }

    @Test
    void create_card() {
        Card card = new Card();
        card.setCardNo("test1")
                .setCvcNo("test2")
                .setCardType(CardType.BC)
                .setExpiryDate(LocalDate.now())
                .setCustomerId(13)
                .setPaytype(PayType.CARD);

        dao.create(card);
        dao = new PaymentDaoImpl();
        Payment read = dao.read(card.getId());

        System.out.println(read);

    }

    @Test
    void read() {
        Payment read = dao.read(2);

        System.out.println(read);
    }

    @Test
    void read_MyIllegalArgumentException() {
        assertThrows(MyIllegalArgumentException.class, ()-> dao.read(11));
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAllByCustomerId() {
        List<Payment> payment = dao.findAllByCustomerId(19);
        for (Payment payment1 : payment) {
            System.out.println(payment1);
        }
        assertEquals(payment.size(),2);
    }
}