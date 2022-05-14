package main;

import main.domain.contract.Contract;
import main.domain.contract.ContractListImpl;
import main.domain.customer.Customer;
import main.domain.customer.CustomerListImpl;
import main.domain.insurance.HealthInsurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.payment.*;

import java.time.LocalDate;

/**
 * packageName :  main
 * fileName : TestData
 * author :  규현
 * date : 2022-05-14
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-14                규현             최초 생성
 */
public class TestData {
    private CustomerListImpl customerList;
    private PaymentList paymentList;
    private InsuranceListImpl insuranceList;
    private ContractListImpl contractList;

    public TestData() {
        this.customerList = new CustomerListImpl();
        this.paymentList = new PaymentListImpl();
        this.insuranceList = new InsuranceListImpl();
        this.contractList = new ContractListImpl();

        createContract();
        createCustomerData();
        createInsurance();
        createPayment();


    }

    private void createCustomerData() {
        Customer customer = new Customer();
        customer.setName("Tester");
        customerList.create(customer);
    }
    private void createInsurance() {
        HealthInsurance h = new HealthInsurance();
        h.setName("테스트 보험");
        h.setPremium(100000);
        insuranceList.create(h);
    }
    private void createPayment() {
        Card card = new Card();
        card.setCustomerId(1);
        card.setExpiryDate(LocalDate.now());
        card.setCardNo("123123");
        card.setCvcNo("231");
        card.setCardType(CardType.BC);
        card.setPaytype(PayType.CARD);
        paymentList.create(card);
    }
    private void createContract() {
        Contract con = new Contract();
        con.setCustomerId(1);
        con.setPremium(100000);
        con.setInsuranceId(1);
        contractList.create(con);
    }
}
