package main.domain.viewUtils.viewlogic;

import main.domain.contract.Contract;
import main.domain.contract.ContractListImpl;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.payment.*;
import main.viewUtils.viewlogic.CustomerViewLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : CustomerViewLogicTest
 * author :  규현
 * date : 2022-05-12
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-12                규현             최초 생성
 */
class CustomerViewLogicTest {

    CustomerViewLogic customerViewLogic = new CustomerViewLogic();
    ContractListImpl contractList = new ContractListImpl();
    PaymentListImpl paymentList = new PaymentListImpl();
    InsuranceListImpl insuranceList = new InsuranceListImpl();
    @BeforeEach
    void beforeEach() {

        createPaymentData();
        createInsuranceData();

        Insurance insurance = new Insurance();
        insurance.setName("test1");
        insurance.setPremium(10000);
        insurance.setDescription("this is test");
        insuranceList.create(insurance);


        Contract contract = new Contract();
        contract.setCustomerId(1);
        contract.setPremium(10000);
        contract.setPayment(paymentList.read(1));
        contract.setInsuranceId(1);

        contractList.create(contract);
    }


    void createInsuranceData() {
        Insurance insurance = new Insurance();
        insurance.setName("test1");
        insurance.setPremium(10000);
        insurance.setDescription("this is test");
        insuranceList.create(insurance);
    }
    void createPaymentData() {
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

    @Test
    void readIns() {
        Insurance read = insuranceList.read(1);
        System.out.println(read.getName());
//        assertNull(read);
    }

    @DisplayName("결제 성공")
//    @Test
    void successPay() {
//        customerViewLogic.showContract(1);

    }

    @Test
    void payLogic() {
        customerViewLogic.payLogicforTest(contractList.read(1));
    }

}