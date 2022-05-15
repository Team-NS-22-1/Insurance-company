package main.application.viewlogic;

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
import main.application.ViewLogic;
import main.exception.MyCloseSequence;
import main.exception.MyIllegalArgumentException;
import main.exception.MyInadequateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static main.utility.MessageUtil.createMenu;
import static main.utility.PaymentFormatUtil.*;

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
    }

    public CustomerViewLogic(CustomerListImpl customerList, ContractListImpl contractList, InsuranceListImpl insuranceList, PaymentListImpl paymentList) {
        this.sc = new Scanner(System.in);
        this.contractList = contractList;
        this.insuranceList = insuranceList;
        this.customerList = customerList;
        this.paymentList = paymentList;
    }
    @Override
    public void showMenu() {
        createMenu("<<고객메뉴>>", "보험가입", "보험료납입", "사고접수", "보상금청구");
    }

    @Override
    public void work(String command) {
        switch (command) {
            case "2" :
                payPremiumButton();
                break;
            default:
                throw new MyIllegalArgumentException();
        }

    }
    // customer ID를 입력하여 customerViewLogic에서 진행되는 작업에서 사용되는 고객 정보를 불러온다.
    public void login(int customerId) {
        this.customer  = customerList.read(customerId);
        List<Payment> payments = paymentList.findAllByCustomerId(customerId);
        this.customer.setPaymentList((ArrayList<Payment>) payments);
    }

    // 보험료 납입 버튼을 클릭했을 경우, 그 이후 작업들에 대해서 보여준다
    // 이후 진행될 작업으로 보험료를 납입할 계약을 선택하고, 해당 계약으로 즉시 결제를 할지, 계약에 기존에 등록된 결제수단을 등록할지,
    // 고객에게 새로운 결제 수단을 추가할지 정할 수 있다.
    private void payPremiumButton() {
        if (!login()) return;


        while (true) {
            Contract contract = selectContract();
            if (contract == null) {
                System.out.println("취소하였습니다.");
                return;
            }
            loop : while (true) {
                createMenu("결제 선택","결제하기","결제수단등록하기","결제수단추가하기");
                System.out.println("0. 취소하기");
                System.out.println("exit. 종료하기");
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
                    case "0" :
                        break loop;
                    case "exit" :
                        throw new MyCloseSequence();
                    default:
                        System.out.println("입력 값을 다시 확인해주세요");
                }
            }
        }
    }

    private boolean login() {
        while (true) {
            try {
                System.out.println("고객 ID 입력 : ");
                System.out.println("0. 취소하기");
                String id = sc.next();
                if (id.equals("0")) {
                    return false;
                }
                login(Integer.parseInt(id));
                break;
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException| NumberFormatException e) {
                System.out.println("올바른 값을 입력해주세요");
            }
        }
        return true;
    }

    // 고객이 보험료 납입 버튼을 클릭한 이후 사용할 계약을 선택하는 기능이다.
    // 계약의 ID를 입력하는 것으로 이후 작업이 진행될 계약 객체를 선택한다.
    private Contract selectContract(){
        Contract contract = null;
        List<Contract> contracts = contractList.findAllByCustomerId(this.customer.getId());
        while (true) {
            try {
                System.out.println("가입된 계약 목록");
                for (Contract con : contracts) {
                    showContractInfoForPay(con);
                }
                System.out.println("0. 취소하기");
                String key = sc.next();
                if (key.equals("0"))
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

    // 보험료 납부를 위한 계약 정보를 출력하는 기능
    public void showContractInfoForPay(Contract contract) {
        Insurance insurance = insuranceList.read(contract.getInsuranceId());
        StringBuilder sb = new StringBuilder();
        sb.append("[ID]").append(" : ").append(contract.getId())
                .append(" 이름 : ").append(insurance.getName()).append(" 보험료 : ").append(contract.getPremium())
                .append("\n");
        System.out.println(sb.toString());
    }


    // 계약을 선택한 이후 즉시 결제를 시도하는 기능.
    // 해당 계약에 결제 수단이 등록되지 않았다면 결제 수단 등록을 진행한다.
    private void payLogic(Contract contract) {
        if (contract.getPayment() == null) {
            System.out.println("해당 계약에 대해 결제 수단 정보가 없습니다. 설정해주세요.");
            setPaymentOnContract(contract);
        }else{
            pay(contract);
        }
    }

    // 계약에 대해서 보험료를 납부하는 기능
    private void pay(Contract contract) {
        customer.pay(contract);
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
                System.out.println("0 : 취소하기");
                System.out.println("exit : 시스템 종료");
                String key = sc.next();
                key = key.toUpperCase();
                if (key.equals("0"))
                    return;
                if(key.equals("exit"))
                    throw new MyCloseSequence();
                Payment payment = this.paymentList.read(Integer.parseInt(key));
                this.customer.registerPayment(contract, payment);
                break;
            } catch (MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("정확한 형식의 값을 입력해주세요.");
            }
        }
    }
    // 고객에게 새로운 결제수단을 추가하는 기능. 카드와 계좌의 정보를 추가할 수 있다.
    public void addnewPayment() {
        loop :while (true) {
            createMenu("결제수단추가하기","카드추가하기","계좌추가하기");
            System.out.println("0. 취소하기");
            System.out.println("exit : 종료하기");
            switch (sc.next()) {
                case "1" :
                    createCard();
                    break;
                case "2":
                    createAccount();
                    break;
                case "0":
                    break loop;
                case "exit" :
                    throw new MyCloseSequence();
            }
        }
    }
    

    // 결제수단 중 카드를 새로 추가하는 기능
    private void createCard() {
        Card card = null;
        while (true) {
            try {
                System.out.println("카드 등록하기");
                System.out.println("카드사 선택");
                CardType cardType = selectCardType();
                if(cardType==null)
                    return;
                System.out.println("카드 번호 : (예시 : ****-****-****-****) {4자리 숫자와 - 입력}");
                String cardNo = validateCardNoFormat(sc.next());
                System.out.println("CVC : (예시 : *** {3자리 숫자})");
                String cvc = validateCVCFormat(sc.next());
                System.out.println("만료일");
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

                while (true) {
                    System.out.println("카드 정보를 등록하시겠습니까? (Y/N)");
                    String result = sc.next();
                    result = result.toUpperCase();
                    if (result.equals("N")) {
                        System.out.println("결제 수단 등록을 취소하셨습니다.");
                        return;
                    } else if (result.equals("Y"))
                        break;
                    else
                        System.out.println("Y 혹은 N을 입력해주세요");
                }
                break;

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | MyInadequateFormatException e) {
                System.out.println("정확한 값을 입력해주세요");
            }
        }
        paymentList.create(card);
        customer.addPayment(card);
        System.out.println("결제 수단이 추가되었습니다.");

    }

    // 카드 결제 수단 추가 중 카드사를 선택하는 기능
    private CardType selectCardType() {
        CardType[] values = CardType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + " " + values[i]);
        }
        System.out.println("0. 취소하기");
        System.out.println("카드사 번호 : ");
        String key = sc.next();
        if(key.equals("0"))
            return null;
        return values[Integer.parseInt(key)-1];
    }
    // 카드 결제 수단을 추가하는 과정에서 만료기간 중 연도를 형식에 맞게 입력했는지 검증하는 기능
    private int validateYearFormat(int year) {
        if(!isYear(Integer.toString(year)))
            throw new MyInadequateFormatException();
        return year;
    }
    // 카드 결제 수단을 추가하는 과정에서 만료기간 중 달를 형식에 맞게 입력했는지 검증하는 기능
    private int validateMonthFormat(int month) {
        if(!isMonth(month))
            throw new MyInadequateFormatException();
        return month;
    }
    
    // 카드 결제 수단을 추가하는 과정에서 입력한 달과 연을 통해서 저장하기 위한 LocalDate 객체를 생성하는 기능
    private LocalDate createExpireDate(int month, int year) {
        String mm = month < 10 ? "0"+month : String.valueOf(month);
        String date = "01/"+mm+"/"+year;
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    // 카드 결제 수단 추가 과정에서 카드 번호 형식에 대해서 검증하는 기능
    private String validateCardNoFormat(String cardNo) {
        if(!isCardNo(cardNo))
            throw new MyInadequateFormatException();
        return cardNo;
    }
    // 카드 결제 수단 추가 과정에서 CVC 번호 형식에 대해서 검증하는 기능
    private String validateCVCFormat(String cvc) {
        if(!isCVC(cvc))
            throw new MyInadequateFormatException();
        return cvc;
    }

    // 계좌 결제 수단을 추가하는 기능
    private void createAccount() {
        Account account;
        while (true) {
            try{
                System.out.println("계좌 추가하기");
                System.out.println("은행사 선택하기");
                BankType bankType = selectBankType();
                if(bankType==null)
                    return;

                System.out.println("계좌 번호 입력하기 : (예시 -> " + bankType.getFormat() + ")");
                String accountNo = checkAccountFormat(bankType, sc.next());

                account = new Account();
                account.setBankType(bankType)
                        .setAccountNo(accountNo)
                        .setCustomerId(this.customer.getId())
                        .setPaytype(PayType.ACCOUNT);

                while (true) {
                    System.out.println("계좌 정보를 등록하시겠습니까? (Y/N)");
                    String result = sc.next();
                    result = result.toUpperCase();
                    if (result.equals("N")) {
                        System.out.println("결제 수단 등록을 취소하셨습니다.");
                        return;
                    } else if (result.equals("Y"))
                        break;
                    else
                        System.out.println("Y 혹은 N을 입력해주세요");
                }

                break;
            }catch (ArrayIndexOutOfBoundsException | NumberFormatException| MyInadequateFormatException e) {
                System.out.println("정확한 값을 입력해주세요");
            }
        }
        paymentList.create(account);
        customer.addPayment(account);
        System.out.println("결제 수단이 추가되었습니다.");
    }

    // 계좌 결제 수단 추가 과정에서 은행을 선택하는 기능
    private BankType selectBankType() {
        BankType[] values = BankType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + " " + values[i]);
        }
        System.out.println("0. 취소하기");
        System.out.println("은행 번호 : ");
        String key = sc.next();
        
        if(key.equals("0"))
            return null;


        return values[Integer.parseInt(key)-1];
    }

    // 계좌 결제 수단 추가 과정에서 은행사에 따라서 계좌 번호를 검증하는 기능
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



    public void payLogicforTest(Contract contract) {
        payLogic(contract);
    }


}
