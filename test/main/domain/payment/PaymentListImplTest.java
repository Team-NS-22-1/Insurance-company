package main.domain.payment;

import domain.payment.PaymentListImpl;
import main.exception.MyIllegalArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  main.domain.payment
 * fileName : PaymentListImplTest
 * author :  규현
 * date : 2022-05-12
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-12                규현             최초 생성
 */
class PaymentListImplTest {

    PaymentListImpl paymentList = new PaymentListImpl();


    @BeforeEach
    void beforeEach() {
        Card card = new Card();
        card.setCardNo("3131-31313");
        card.setCardType(CardType.LOTTE);
        card.setCvcNo("111");
        card.setExpiryDate(LocalDate.now());
        card.setPaytype(PayType.CARD);
        card.setCustomerId(1);

        Account account = new Account();
        account.setAccountNo("55555");
        account.setBankType(BankType.CITY);
        account.setPaytype(PayType.ACCOUNT);
        account.setCustomerId(1);

        paymentList.create(account);
        paymentList.create(card);

        Account account2 = new Account();
        account2.setAccountNo("138-111111-1-1-1-11");
        account2.setBankType(BankType.HANA);
        account2.setPaytype(PayType.ACCOUNT);
        account2.setCustomerId(4);
        paymentList.create(account2);

    }

    @DisplayName("결제수단 저장 - 카드")
    @Test
    void create_Card() {
        Card card = new Card();
        card.setCardNo("1234-4321");
        card.setCardType(CardType.BC);
        card.setCvcNo("601");
        card.setExpiryDate(LocalDate.now());
        card.setPaytype(PayType.CARD);

        paymentList.create(card);

        Payment read = paymentList.read(card.getId());
        assertEquals(card,read);
    }

    @DisplayName("결제수단 저장 - 계좌")
    @Test
    void create_Account() {
        Account account = new Account();
        account.setAccountNo("138-111111-1-1-1-11");
        account.setBankType(BankType.HANA);
        account.setPaytype(PayType.ACCOUNT);

        paymentList.create(account);

        Payment read = paymentList.read(account.getId());
        assertEquals(account,read);


    }

    @DisplayName("결제 수단 삭제 - 정상")
    @Test
    void delete() {

        boolean delete = paymentList.delete(1);
        assertTrue(delete);
    }

    @DisplayName("결제 수단 삭제 - 예외 없는 ID 입력하기")
    @Test
    void delete_exception() {
        assertThrows(MyIllegalArgumentException.class
                , () -> paymentList.delete(999));
    }

    @DisplayName("모든 결제 수단 조회하기")
    @Test
    void findAll() {
        List<Payment> all = paymentList.findAll();

        assertEquals(all.size(),3);
        for (Payment payment : all) {
            System.out.println(payment);
        }
    }

    @DisplayName("고객ID로 결제수단 조회하기")
    @Test
    void findAllByCustomerId() {
        List<Payment> all = paymentList.findAllByCustomerId(1);

        assertEquals(all.size(),2);
        for (Payment payment : all) {
            System.out.println(payment);
        }
    }

    @DisplayName("고객ID로 결제수단 조회하기 - 예외 - 존재하지 않은 ID")
    @Test
    void findAllByCustomerId_exception_not_existed_id() {
        assertThrows(MyIllegalArgumentException.class,
                () -> paymentList.findAllByCustomerId(5));
    }


}