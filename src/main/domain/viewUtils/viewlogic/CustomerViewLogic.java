package main.domain.viewUtils.viewlogic;

import main.domain.contract.Contract;
import main.domain.contract.ContractList;
import main.domain.contract.ContractListImpl;
import main.domain.customer.Customer;
import main.domain.customer.CustomerList;
import main.domain.customer.CustomerListImpl;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceList;
import main.domain.insurance.InsuranceListImpl;
import main.domain.payment.*;
import main.domain.viewUtils.ViewLogic;
import main.exception.MyIllegalArgumentException;
import main.exception.MyInadequateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static main.domain.utility.FormatUtility.*;
import static main.domain.utility.MessageUtil.createMenu;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : CustomerViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class CustomerViewLogic implements ViewLogic {

    private ContractList contractList;
    private InsuranceList insuranceList;
    private CustomerList customerList;
    private PaymentList paymentList;
    private Customer customer;
    private Scanner sc;
    public CustomerViewLogic() {
        this.sc = new Scanner(System.in);
        this.contractList = new ContractListImpl();
        this.insuranceList = new InsuranceListImpl();
        this.customerList = new CustomerListImpl();
        this.paymentList = new PaymentListImpl();
    }

    @Override
    public void showMenu() {
        createMenu("고객메뉴", "보험가입", "보험료납입", "사고접수", "보상금청구", "기타등등");
    }

    @Override
    public void work(String command) {
        while (true) {
            try {
                System.out.println("고객 ID 입력 : ");
                int customerId = sc.nextInt();
                login(customerId);
                break;
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        switch (command) {
            case "2" :
                selectButton();
                break;
        }

    }

    public void login(int customerId) {
        this.customer  = customerList.read(customerId);
        List<Payment> payments = paymentList.findAllByCustomerId(customerId);
        this.customer.setPaymentList((ArrayList<Payment>) payments);
    }

    private void selectButton() {


        Contract contract = selectContract();
        if (contract == null) {
            System.out.println("취소하였습니다.");
            return;
        }


        loop : while (true) {
            createMenu("결제 선택","결제하기","결제수단등록하기","결제수단추가하기","취소하기");
            String next = sc.next();
            switch (next) {
                case "1" :
                    payLogic(contract);
                    break;
                case "2" :
                    setPaymentOnContract(contract);
                    break;
                case"3":
                    addnewPayment();
                    break;
                case "4" :
                    break loop;
            }
        }


    }
    private Contract selectContract(){
        Contract contract = null;
        List<Contract> contracts = contractList.findAllByCustomerId(this.customer.getId());
        while (true) {
            try {
                System.out.println("가입된 계약 목록");
                for (Contract con : contracts) {
                    showInfoForPayment(con);
                }
                System.out.println("뒤로가기 : X");
                String key = sc.next();
                if (key.equals("X"))
                    break;

                contract = contractList.read(Integer.parseInt(key));
                break;
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("제대로 된 ID를 입력해주세요");
            }
        }
        return contract;
    }

    private void payLogic(Contract contract) {
        if (contract.getPayment() == null) {
            System.out.println("해당 계약에 대해 결제 수단 정보가 없습니다. 설정해주세요.");
            setPaymentOnContract(contract);
        }else{
            pay(contract);
        }
    }

    // 고객에게 등록된 결제 수단들을 불러온다.
    private void setPaymentOnContract(Contract contract) {
        ArrayList<Payment> paymentList = this.customer.getPaymentList();
        if (paymentList.size() == 0) {
            System.out.println("등록된 결제 수단이 없습니다. 새로 추가해주세요");
            return;
        }
        while (true) {
            try {
                for (Payment payment : paymentList) {
                    System.out.println(payment);
                }
                int paymentId = sc.nextInt();
                Payment payment = this.paymentList.read(paymentId);
                this.customer.registerPayment(contract, payment);
                break;
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
    }
    
    public void addnewPayment() {

        loop :while (true) {
            createMenu("결제수단추가하기","카드추가하기","계좌추가하기","취소하기");
            switch (sc.next()) {
                case "1" :
                    createCard();
                    break;
                case "2":
                    createAccount();
                    break;
                case "3":
                    break loop;
            }

        }



    }



    private void pay(Contract contract) {
       customer.pay(contract);
    }
    private void createCard() {
        Card card = null;
        while (true) {
            try {
                System.out.println("카드 등록하기");
                System.out.println("카드사 선택");
                CardType cardType = selectCardType();
                System.out.println("카드 번호 : (예시 : ****-****-****-****) {4자리 숫자와 - 입력}");
                String cardNo = validateCardNoFormat(sc.next());
                System.out.println("CVC : (예시 : *** {3자리 숫자})");
                String cvc = validateCVCFormat(sc.next());
                System.out.println("만료일 : ");
                System.out.print("월 : ");
                int month = validateMonthFormat(sc.nextInt());
                System.out.print("년 : (예시 : ****) {4개 숫자 입력 && 202* ~ 203* 까지의 값 입력}");
                int year = validateYearFormat(sc.nextInt());
                LocalDate expireDate = createExpireDate(month, year);
                card = new Card();
                card.setCardNo(cardNo)
                        .setCvcNo(cvc)
                        .setCardType(cardType)
                        .setExpiryDate(expireDate)
                        .setCustomerId(this.customer.getId())
                        .setPaytype(PayType.CARD);

                break;

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | MyInadequateFormatException e) {
                System.out.println("정확한 값을 입력해주세요");
            }
        }
        paymentList.create(card);
        customer.addPayment(card);
        System.out.println("결제 수단이 추가되었습니다.");

    }

    private int validateYearFormat(int year) {
        if(!isYear(Integer.toString(year)))
            throw new MyInadequateFormatException();
        return year;
    }

    private int validateMonthFormat(int month) {
        if(!isMonth(month))
            throw new MyInadequateFormatException();
        return month;
    }

    private LocalDate createExpireDate(int month, int year) {
        String mm = month < 10 ? "0"+month : String.valueOf(month);
        String date = "01/"+mm+"/"+year;
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private CardType selectCardType() {
        CardType[] values = CardType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + " " + values[i]);
        }
        System.out.println("뒤로 가기 X");
        System.out.println("카드사 번호 : ");
        String key = sc.next();
       return values[Integer.parseInt(key)];
    }

    private BankType selectBankType() {
        BankType[] values = BankType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + " " + values[i]);
        }
        System.out.println("뒤로 가기 X");
        System.out.println("은행 번호 : ");
        String key = sc.next();
        return values[Integer.parseInt(key)-1];
    }

    private void createAccount() {
        Account account;
        while (true) {
            try{
                System.out.println("계좌 추가하기");
                System.out.println("은행사 선택하기");
                BankType bankType = selectBankType();
                System.out.println("계좌 번호 입력하기 : (예시 -> " + bankType.getFormat() + ")");
                String accountNo = checkAccountFormat(bankType, sc.next());

                account = new Account();
                account.setBankType(bankType)
                        .setAccountNo(accountNo)
                        .setCustomerId(this.customer.getId())
                        .setPaytype(PayType.ACCOUNT);
                break;
            }catch (ArrayIndexOutOfBoundsException | NumberFormatException| MyInadequateFormatException e) {
                System.out.println("정확한 값을 입력해주세요");
            }
        }
        paymentList.create(account);
        customer.addPayment(account);
        System.out.println("결제 수단이 추가되었습니다.");
    }

    private String validateCardNoFormat(String cardNo) {
        if(!isCardNo(cardNo))
            throw new MyInadequateFormatException();
        return cardNo;
    }

    private String validateCVCFormat(String cvc) {
        if(!isCardNo(cvc))
            throw new MyInadequateFormatException();
        return cvc;
    }

    private String checkAccountFormat(BankType bankType, String accountNo) {
        boolean result = false;
        switch (bankType) {
            case KB :
                result = isKB(accountNo);
                break;
            case NH:
                result = isNH(accountNo);
                break;
            case KAKAOBANK:
                result = isKakaoBank(accountNo);
                break;
            case SINHAN:
                result = isSinhan(accountNo);
                break;
            case WOORI:
                result = isWoori(accountNo);
                break;
            case IBK:
                result = isIBK(accountNo);
                break;
            case HANA:
                result = isHana(accountNo);
                break;
            case CITY:
                result = isCity(accountNo);
                break;
            case SAEMAUL:
                result = isSaemaul(accountNo);
                break;
        }
        if (!result)
            throw new MyInadequateFormatException("정확한 형식의 값을 입력해주세요");

        return accountNo;

    }

    public void showInfoForPayment(Contract contract) {
        Insurance insurance = insuranceList.read(contract.getInsuranceId());
        StringBuilder sb = new StringBuilder();
        sb.append("[ID]").append(" : ").append(contract.getId())
                .append(" 이름 : ").append(insurance.getName()).append(" 보험료 : ").append(contract.getPremium())
                .append("\n");
        System.out.println(sb.toString());
    }

    public void payLogicforTest(Contract contract) {
        payLogic(contract);
    }


}
