package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.dao.accident.AccidentDaoImpl;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDao;
import insuranceCompany.application.dao.accident.AccidentDocumentFileDaoImpl;
import insuranceCompany.application.dao.contract.ContractDao;
import insuranceCompany.application.dao.contract.ContractDaoImpl;
import insuranceCompany.application.dao.insurance.InsuranceDao;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.AccidentType;
import insuranceCompany.application.domain.accident.CarAccident;
import insuranceCompany.application.domain.accident.accidentDocumentFile.AccDocType;
import insuranceCompany.application.domain.accident.accidentDocumentFile.AccidentDocumentFile;
import insuranceCompany.application.domain.contract.BuildingType;
import insuranceCompany.application.domain.contract.CarType;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.customer.payment.*;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Guarantee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.InsuranceType;
import insuranceCompany.application.domain.insurance.SalesAuthorizationState;

import insuranceCompany.application.global.exception.*;
import insuranceCompany.application.global.utility.FileDialogUtil;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.login.User;
import insuranceCompany.application.viewlogic.dto.UserDto.UserDto;
import insuranceCompany.application.viewlogic.dto.accidentDto.AccidentReportDto;
import insuranceCompany.application.viewlogic.dto.contractDto.*;
import insuranceCompany.application.viewlogic.dto.customerDto.CustomerDto;
import insuranceCompany.outerSystem.CarAccidentService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static insuranceCompany.application.domain.contract.BuildingType.*;
import static insuranceCompany.application.domain.contract.CarType.*;
import static insuranceCompany.application.global.constant.CommonConstants.*;
import static insuranceCompany.application.global.constant.ContractConstants.*;
import static insuranceCompany.application.global.constant.CustomerViewLogicConstants.*;
import static insuranceCompany.application.global.constant.ExceptionConstants.*;
import static insuranceCompany.application.global.utility.BankUtil.checkAccountFormat;
import static insuranceCompany.application.global.utility.BankUtil.selectBankType;
import static insuranceCompany.application.global.utility.CompAssignUtil.assignCompEmployee;
import static insuranceCompany.application.global.utility.FormatUtil.*;
import static insuranceCompany.application.global.utility.MessageUtil.*;

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

    private ContractDao contractList;
    private InsuranceDao insuranceList;
    private AccidentDao accidentDao;
    private AccidentDocumentFileDao accidentDocumentFileDao;

    private Customer customer;
    private Scanner sc;
    private MyBufferedReader br;
    private Insurance insurance;

    public CustomerViewLogic() {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        this.sc = new Scanner(System.in);
        this.customer = new Customer();
    }

    public CustomerViewLogic(Customer customer) {
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        this.sc = new Scanner(System.in);
        this.customer = customer;
        setPayment();
    }
    // customer ID를 입력하여 customerViewLogic에서 진행되는 작업에서 사용되는 고객 정보를 불러온다.
    private void setPayment() {
        try {
            customer.readPayments();
        } catch (MyIllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public String showMenu() {
        if (customer.getId() == 0)
            return createMenuAndExitQuery(CUSTOMER_MENU, SIGN_IN_INSURANCE);
        else
            return createMenuAndLogoutAndInput(CUSTOMER_MENU, SIGN_IN_INSURANCE, PAY_PREMIUM, REPORT_ACCIDENT, CLAIM_COMPENSATION);
    }

    @Override
    public void work(String command) {
        try {
            if (customer.getId() == 0) {
                switch (command) {
                    case ONE -> selectInsurance();
                    case "" -> throw new InputNullDataException();
                }
            } else {
                switch (command) {
                    case ONE -> selectInsurance();
                    case TWO -> payPremiumButton();
                    case THREE -> reportAccident();
                    case FOUR -> claimCompensation();
                    case "" -> throw new InputNullDataException();
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR:: IO 시스템에 장애가 발생하였습니다!\n프로그램을 종료합니다...");
            System.exit(0);
        } catch (MyIllegalArgumentException | NoResultantException | InputNullDataException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectInsurance() throws IOException {
        ArrayList<Insurance> insurances = customer.readInsurances();
        if(insurances.size() == 0)
            throw new NoResultantException();
        while (true) {
            System.out.println(CONTRACT_INSURANCE_LIST);
            System.out.printf(CONTRACT_INSURANCES_CATEGORY_FORMAT, CONTRACT_INSURANCE_ID, CONTRACT_INSURANCE_NAME, CONTRACT_INSURANCE_TYPE);
            System.out.println(CONTRACT_SHORT_DIVISION);
            for (Insurance insurance : insurances) {
                if (insurance.getDevelopInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION)
                    System.out.printf(CONTRACT_INSURANCES_VALUE_FORMAT, insurance.getId(), insurance.getName(), insurance.printInsuranceType());
//                    System.out.println("보험상품 번호: " + insurance.getId() + " | 보험상품 이름: " + insurance.getName() +
//                            "   \t보험상품 종류: " + insurance.getInsuranceType());
            }

            try {
                int insuranceId = 0;
                System.out.println(CUSTOMER_SELECT_INSURANCE_ID );
                insuranceId = (int) br.verifyRead(CONTRACT_INPUT_INSURANCE_ID, insuranceId);
                if (insuranceId == 0) break;

                insurance = customer.readInsurance(insuranceId);
                if (insurance.getDevelopInfo().getSalesAuthorizationState() == SalesAuthorizationState.PERMISSION) {
                    System.out.println(CONTRACT_INSURANCE_DESCRIPTION + insurance.getDescription() + CONTRACT_INSURANCE_CONTRACT_PERIOD + insurance.getContractPeriod() +
                            CONTRACT_YEAR_PARAMETER + CONTRACT_INSURANCE_PAYMENT_PERIOD + insurance.getPaymentPeriod() +CONTRACT_YEAR_PARAMETER + CONTRACT_INSURANCE_GUARANTEE);
                    System.out.printf(CONTRACT_GUARANTEES_CATEGORY_FORMAT, "",CONTRACT_INSURANCE_GUARANTEE_DESCRIPTION, CONTRACT_INSURANCE_GUARANTEE_AMOUNT);
                    System.out.println(CONTRACT_LONG_DIVISION);
                    for(Guarantee guarantee : insurance.getGuaranteeList()){
                        System.out.printf(CONTRACT_GUARANTEES_VALUE_FORMAT, guarantee.getName(), guarantee.getDescription(), guarantee.getGuaranteeAmount());
                    }
                    decideSigning();
                }
                else {
                    throw new NoResultantException();
                }
            } catch (InputException | MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 코드를 입력해주세요");
            }
        }
    }

    private void decideSigning() throws IOException {
        int choice = br.verifyCategory(CUSTOMER_DICIDE_SIGNING, 2);
        CustomerDto customerDto = null;
        if (choice == 1) {
            if (customer.getId() == 0)
                customerDto = inputCustomerInfo();
            ContractDto contractDto = switch (insurance.getInsuranceType()) {
                case HEALTH -> inputHealthInfo(customerDto);
                case FIRE -> inputFireInfo();
                case CAR -> inputCarInfo(customerDto);
            };
            signContract(customerDto, contractDto);
        }
    }

    private CustomerDto inputCustomerInfo() throws IOException {
        String name = "", ssn = "", phone = "", address = "", email = "", job = "";
        System.out.println(CONTRACT_INPUT_CUSTOMER_INFO);

        name = (String) br.verifySpecificRead(CONTRACT_CUSTOMER_NAME_QUERY, name, "name");
        ssn = (String) br.verifySpecificRead(CONTRACT_SSN_QUERY, ssn, "ssn");
        phone = (String) br.verifySpecificRead(CONTRACT_PHONE_QUERY, phone, "phone");
        address = (String) br.verifyRead(CONTRACT_ADDRESS_QUERY, address);
        email = (String) br.verifySpecificRead(CONTRACT_EMAIL_QUERY, email, "email");
        job = (String) br.verifyRead(CONTRACT_JOB_QUERY, job);

        return new CustomerDto(name, ssn, phone, address, email, job);
    }

    private ContractDto inputHealthInfo(CustomerDto customerDto) {
        int riskCount = 0, height = 0, weight = 0;
        String diseaseDetail = "";
        boolean isDrinking, isSmoking, isDriving, isDangerActivity, isTakingDrug, isHavingDisease;
        System.out.println(CONTRACT_INPUT_HEALTH_INFO);

        height = (int) br.verifyRead(CONTRACT_HEIGHT_QUERY, height);
        weight = (int) br.verifyRead(CONTRACT_WEGHIT_QUERY, weight);
        isDrinking = br.verifyCategory(CONTRACT_IS_DRINKING_QUERY, 2) == 1;
        if(isDrinking) riskCount++;
        isSmoking = br.verifyCategory(CONTRACT_IS_SMOKING_QUERY, 2) == 1;
        if(isSmoking) riskCount++;
        isDriving = br.verifyCategory(CONTRACT_IS_DRIVING_QUERY, 2) == 1;
        if(isDriving) riskCount++;
        isDangerActivity = br.verifyCategory(CONTRACT_IS_DANGER_ACTIVITY_QUERY, 2) == 1;
        if(isDangerActivity) riskCount++;
        isTakingDrug = br.verifyCategory(CONTRACT_IS_TAKING_DRUG_QUERY, 2) == 1;
        if(isTakingDrug) riskCount++;
        isHavingDisease = br.verifyCategory(CONTRACT_IS_HAVING_DISEASE_QUERY, 2) == 1;
        if (isHavingDisease) {
            riskCount++;
            diseaseDetail = (String) br.verifyRead(CONTRACT_DISEASE_DETAIL_QUERY, diseaseDetail);
        }

        int premium = customer.inquireHealthPremium(customerDto.getSsn(), riskCount, insurance);
        return new HealthContractDto(height, weight, isDrinking, isSmoking, isDriving, isDangerActivity,
                                    isTakingDrug, isHavingDisease, diseaseDetail).setPremium(premium);
    }

    private ContractDto inputFireInfo() {
        int buildingArea = 0;
        BuildingType buildingType;
        Long collateralAmount = 0L;
        boolean isSelfOwned, isActualResidence;

        buildingArea = (int) br.verifyRead(CONTRACT_BUILDING_AREA_QUERY, buildingArea);
        buildingType = switch (br.verifyCategory(CONTRACT_BUILDING_TYPE_QUERY, 4)) {
            case 1 -> COMMERCIAL;
            case 2 -> INDUSTRIAL;
            case 3 -> INSTITUTIONAL;
            case 4 -> RESIDENTIAL;
            default -> throw new IllegalStateException();
        };
        collateralAmount = (Long) br.verifyRead(CONTRACT_COLLATERAL_AMOUNT_QUERY, collateralAmount);
        isSelfOwned = br.verifyCategory(CONTRACT_IS_SELF_OWNED_QUERY, 2) == 1;
        isActualResidence = br.verifyCategory(CONTRACT_IS_ACTUAL_RESIDENCE_QUERY, 2) == 1;

        int premium = customer.inquireFirePremium(buildingType, collateralAmount, insurance);
        return new FireContractDto(buildingArea, buildingType, collateralAmount, isSelfOwned, isActualResidence).setPremium(premium);
    }

    private ContractDto inputCarInfo(CustomerDto customerDto) {
        CarType carType;
        String modelName = "", carNo = "";
        int modelYear = 0;
        Long value = 0L;

        carNo = (String) br.verifySpecificRead(CONTRACT_CAR_NO_QUERY, carNo, "carNo");
        carType = switch (br.verifyCategory(CONTRACT_CAR_TYPE_QUERY, 7)) {
            case 1 -> URBAN;
            case 2 -> SUBCOMPACT;
            case 3 -> COMPACT;
            case 4 -> MIDSIZE;
            case 5 -> LARGESIZE;
            case 6 -> FULLSIZE;
            case 7 -> SPORTS;
            default -> throw new IllegalStateException();
        };
        modelName = (String) br.verifyRead(CONTRACT_MADEL_NAME_QUERY, modelName);
        modelYear = (int) br.verifyRead(CONTRACT_MODEL_YEAR_QUERY, modelYear);
        value = (Long) br.verifyRead(CONTRACT_VALUE_QUERY, value);

        int premium = customer.inquireCarPremium(customerDto.getSsn(), value, insurance);
        return new CarContractDto(carNo, carType, modelName, modelYear, value).setPremium(premium);
    }

    private void signContract(CustomerDto customerDto, ContractDto contractDto) {
        System.out.println(premiumInquiry(contractDto.getPremium()));

        int choice = br.verifyCategory(CUSTOMER_SGIN_CONTRACT, 2);
        switch (choice) {
            case 1 -> {
                User user = null;
                if (customer.getId() == 0) {
                    customer = customer.registerCustomer(customerDto);
                    UserDto userDto = signUp();
                    user = customer.registerUser(userDto);
                }
                Contract contract = customer.registerContract(customer, contractDto, insurance);
                System.out.println(customer);
                System.out.println(user);
                System.out.println(contract);
                System.out.println(CUSTOMER_SIGN);
            }
            case 2 -> System.out.println(CUSTOMER_CANCEL);
        }
    }

    private UserDto signUp() {
        String userId = "", password = "";
        userId = (String) br.verifyRead(CONTRACT_USER_ID_QUERY, userId);
        password = (String) br.verifyRead(CONTRACT_USER_PASSWORD_QUERY, password);
        return new UserDto(userId, password, customer.getId());
    }




    // 보험료 납입 버튼을 클릭했을 경우, 그 이후 작업들에 대해서 보여준다
    // 이후 진행될 작업으로 보험료를 납입할 계약을 선택하고, 해당 계약으로 즉시 결제를 할지, 계약에 기존에 등록된 결제수단을 등록할지,
    // 고객에게 새로운 결제 수단을 추가할지 정할 수 있다.
    private void payPremiumButton() {
        while (true) {
            Contract contract = selectContract();
            if (contract == null) {
                System.out.println(CANCEL);
                return;
            }
            loop : while (true) {
                try {
                    createMenu(PAY_MENU, DO_PAY, SET_PAYMENT, ADD_ACCOUNT_MENU_HEAD);
                    System.out.println(ZERO_MESSAGE);
                    System.out.println(EXIT_MESSAGE);
                    System.out.print(INPUT);
                    String next = sc.next();
                    switch (next.toUpperCase()) {
                        case ONE:
                            payLogic(contract);
                            break;
                        case TWO:
                            setPaymentOnContract(contract);
                            break;
                        case THREE:
                            addNewPayment();
                            break;
                        case ZERO:
                            break loop;
                        case EXIT:
                            throw new MyCloseSequence();
                        default:
                            throw new InputInvalidMenuException();
                    }
                } catch (InputInvalidMenuException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // 고객이 보험료 납입 버튼을 클릭한 이후 사용할 계약을 선택하는 기능이다.
    // 계약의 ID를 입력하는 것으로 이후 작업이 진행될 계약 객체를 선택한다.
    private Contract selectContract(){
        Contract contract = null;
        List<Contract> contracts = customer.readContracts();
        while (true) {
            try {
                try {
                    System.out.println(CONTRACT_LIST);
                    for (Contract con : contracts) {
                        showContractInfoForPay(con);
                    }
                    System.out.println(ZERO_MESSAGE);
                    System.out.print(INPUT);
                    String key = sc.next();
                    if (key.equals(ZERO))
                        break;
                    contractList = new ContractDaoImpl();
                    contract = contractList.read(Integer.parseInt(key));
                    if (contract.getCustomerId() != this.customer.getId()) {
                        throw new MyIllegalArgumentException(INPUT_DATA_ON_LIST);
                    }

                    break;
                } catch (MyIllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    throw new InputInvalidDataException(e);
                }
            } catch (InputInvalidDataException e) {
                System.out.println(e.getMessage());
            }
        }
        return contract;
    }

    // 보험료 납부를 위한 계약 정보를 출력하는 기능
    public void showContractInfoForPay(Contract contract) {

        insuranceList = new InsuranceDaoImpl();
        Insurance insurance = insuranceList.read(contract.getInsuranceId());
        StringBuilder sb = new StringBuilder();
        sb.append(CONTRACT_ID_LABEL).append(contract.getId())
                .append(CONTRACT_NAME_LABEL).append(insurance.getName()).append(CONTRACT_PREMIUM_LABEL).append(contract.getPremium())
                .append("\n");
        System.out.println(sb.toString());
    }


    // 계약을 선택한 이후 즉시 결제를 시도하는 기능.
    // 해당 계약에 결제 수단이 등록되지 않았다면 결제 수단 등록을 진행한다.
    private void payLogic(Contract contract) {
        if (contract.getPaymentId() == 0) {
            System.out.println(NO_PAYMENT_ON_CONTRACT);
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
        if (paymentList.isEmpty()) {
            addNewPayment();
            return;
        }
        while (true) {
            try{
                try {
                    System.out.println(PAYMENT_LIST_MENU);
                    for (Payment payment : paymentList) {
                        System.out.println(payment);
                    }
                    System.out.println(ZERO_MESSAGE);
                    System.out.println(EXIT_MESSAGE);
                    System.out.print(INPUT);
                    String key = sc.next();
                    key = key.toUpperCase();
                    if (key.equals(ZERO))
                        return;
                    if(key.equals(EXIT))
                        throw new MyCloseSequence();
                    int paymentId = Integer.parseInt(key);
                    this.customer.registerPayment(contract, paymentId);
                    break;
                } catch (NumberFormatException e) {
                    throw new InputInvalidDataException(INPUT_WRONG_FORMAT, e);
                }
            } catch (MyIllegalArgumentException |InputInvalidDataException e ) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 고객에게 새로운 결제수단을 추가하는 기능. 카드와 계좌의 정보를 추가할 수 있다.
    public void addNewPayment() {
        loop :while (true) {
            try {

                createMenu(ADD_ACCOUNT_MENU_HEAD, REGISTER_CARD, REGISTER_ACCOUNT);
                System.out.println(ZERO_MESSAGE);
                System.out.println(EXIT_MESSAGE);
                System.out.print(INPUT);
                switch (sc.next().toUpperCase()) {
                    case ONE:
                        createCard();
                        break;
                    case TWO:
                        createAccount();
                        break;
                    case ZERO:
                        break loop;
                    case EXIT:
                        throw new MyCloseSequence();
                    default:
                        throw new InputInvalidMenuException();
                }
            } catch (InputInvalidMenuException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    // 결제수단 중 카드를 새로 추가하는 기능
    private void createCard() {
        PaymentDto card = new PaymentDto();
        while (true) {
            try {
                System.out.println(REGISTER_CARD);
                System.out.println(SELECT_CARD_TYPE);
                CardType cardType = selectCardType();
                if(cardType==null)
                    return;

                while (true) {
                    try {
                        System.out.println(CARD_NO_EX);
                        System.out.print(INPUT);
                        String cardNo = validateCardNoFormat(sc.next());
                        System.out.println(CVC_EX);
                        System.out.print(INPUT);
                        String cvc = validateCVCFormat(sc.next());
                        System.out.println(EXPIRY_DATE);
                        System.out.print(MONTH);
                        int month = validateMonthFormat(sc.nextInt());
                        System.out.println(YEAR_EX);
                        System.out.print(INPUT);
                        int year = validateYearFormat(sc.nextInt());
                        LocalDate expireDate = createExpireDate(month, year);

                        card.setCardNo(cardNo)
                                .setCvcNo(cvc)
                                .setCardType(cardType)
                                .setExpiryDate(expireDate)
                                .setCustomerId(this.customer.getId())
                                .setPayType(PayType.CARD);
                        break;
                    } catch ( MyInadequateFormatException e) {
                        System.out.println(INPUT_WRONG_FORMAT);
                    }
                }


                while (true) {
                    System.out.println(REGISTER_CARD_INFO);
                    System.out.print(INPUT);
                    String result = sc.next();
                    result = result.toUpperCase();
                    if (result.equals(NO)) {
                        System.out.println(CANCEL_REGISTER_PAYMENT);
                        return;
                    } else if (result.equals(YES))
                        break;
                    else
                        throw new InputInvalidDataException();
                }
                break;

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | MyInadequateFormatException | InputInvalidDataException e) {
                System.out.println(INPUT_WRONG_FORMAT);
            }
        }
        customer.addPayment(card);
        System.out.println(SUCCESS_REGISTER_PAYMENT);

    }

    // 카드 결제 수단 추가 중 카드사를 선택하는 기능
    private CardType selectCardType() {
        CardType[] values = CardType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + SPACE + values[i]);
        }
        System.out.println(ZERO_MESSAGE);
        System.out.println(SELECT_CARD_TYPE_NO);
        System.out.print(INPUT);
        String key = sc.next();
        if(key.equals(ZERO))
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
        String mm = month < 10 ? ZERO+month : String.valueOf(month);
        String date = DAY_EX+mm+SLASH+year;
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
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
        PaymentDto account = new PaymentDto();
        loop: while (true) {
            try{
                System.out.println(REGISTER_ACCOUNT);
                System.out.println(SELECT_BANK);
                BankType bankType = selectBankType(br);
                if(bankType==null)
                    return;
                while (true) {
                    try {
                        System.out.println(showAccountNoEX(bankType.getFormat()));
                        System.out.println(ZERO_MESSAGE);
                        System.out.print(INPUT);
                        String command = sc.next();
                        if (command.equals(ZERO)) {
                            continue loop;
                        }
                        String accountNo = checkAccountFormat(bankType,command);


                        account.setBankType(bankType)
                                .setAccountNo(accountNo)
                                .setCustomerId(this.customer.getId())
                                .setPayType(PayType.ACCOUNT);
                        break;
                    } catch (MyInadequateFormatException e) {
                        System.out.println(e.getMessage());
                    }
                }

                while (true) {
                    System.out.println(REGISTER_ACCOUNT_INFO);
                    System.out.print(INPUT);
                    String result = sc.next();
                    result = result.toUpperCase();
                    if (result.equals(NO)) {
                        System.out.println(CANCEL_REGISTER_PAYMENT);
                        return;
                    } else if (result.equals(YES))
                        break;
                    else
                        throw new InputInvalidDataException();
                }

                break;
            }catch (ArrayIndexOutOfBoundsException | NumberFormatException| MyInadequateFormatException e) {
                System.out.println(INPUT_WRONG_FORMAT);
            }
        }
        customer.addPayment(account);
        System.out.println(SUCCESS_REGISTER_PAYMENT);
    }





    private Accident selectAccident() {
        Accident retAccident = null;
        int accidentId = 0;
        while (true) {
            accidentDao = new AccidentDaoImpl();
            List<Accident> accidents = accidentDao.readAllByCustomerId(customer.getId());
            for (Accident accident : accidents) {
                accident.printForCustomer();
            }
            try {

                System.out.println(ZERO_MESSAGE);
                System.out.println(EXIT_MESSAGE);
                accidentId = (int) br.verifyRead(INPUT_ACCIDENT_ID, accidentId);

                if (accidentId == 0) {
                    break;
                }
                accidentDao = new AccidentDaoImpl();
                retAccident = accidentDao.read(accidentId);
                if(retAccident.getCustomerId() != this.customer.getId())
                    throw new MyIllegalArgumentException(INPUT_DATA_ON_LIST);
                break;
            } catch (InputException | MyIllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        if (accidentId != 0) {
            try{
                accidentDocumentFileDao = new AccidentDocumentFileDaoImpl();
                List<AccidentDocumentFile> files = accidentDocumentFileDao.readAllByAccidentId(retAccident.getId());
                for (AccidentDocumentFile file : files) {
                    retAccident.getAccDocFileList().put(file.getType(),file);
                }
            }catch (MyIllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
        return retAccident;
    }

    private void reportAccident()  {
        AccidentType accidentType = selectAccidentType();
        if (accidentType != null)
            inputAccidentInfo(accidentType);
    }

    private void inputAccidentInfo(AccidentType selectAccidentType) {

        AccidentReportDto accidentReportDto = inputDetailAccidentInfo(inputCommonAccidentInfo(selectAccidentType));
        Accident accident = customer.reportAccident(accidentReportDto);
        accident.printForCustomer();

        AccidentType accidentType = accident.getAccidentType();
        if (accidentType == AccidentType.CARACCIDENT) {
            if (((CarAccident) accident).isRequestOnSite()) {
                CarAccidentService.connectWorker();
            }
        } else if (accidentType == AccidentType.CARBREAKDOWN) {
            CarAccidentService.connectWorker();
        }

    }

    private AccidentReportDto inputDetailAccidentInfo(AccidentReportDto accidentReportDto) {
        AccidentType accidentType = accidentReportDto.getAccidentType();
        return switch (accidentType) {
            case INJURYACCIDENT ->inputInjuryAccidentInfo(accidentReportDto);
            case CARBREAKDOWN -> inputCarBreakdown(accidentReportDto);
            case CARACCIDENT -> inputCarAccident(accidentReportDto);
            case FIREACCIDENT -> inputFireAccidentInfo(accidentReportDto);
        };
    }

    private AccidentReportDto inputPlaceAddress(AccidentReportDto accidentReportDto) {
        String placeAddress = "";
        placeAddress= (String) br.verifyRead(ADDRESS, placeAddress);
        return accidentReportDto.setPlaceAddress(placeAddress);
    }

    private AccidentReportDto inputCarNo(AccidentReportDto accidentReportDto) {
        String carNo = "";
        while (true) {
            try {

                carNo = (String) br.verifyRead(CAR_NO_EX, carNo);
                if (isCarNo(carNo))
                    break;
                throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return accidentReportDto.setCarNo(carNo);
    }

    private AccidentReportDto inputCarAccident(AccidentReportDto accidentReportDto) {
        inputPlaceAddress(accidentReportDto);
        inputCarNo(accidentReportDto);

        String opposingDriverPhone = "";
        while (true) {
            try{

                opposingDriverPhone= (String) br.verifyRead(OPOSSING_PHONE, opposingDriverPhone);
                if(isPhone(opposingDriverPhone))
                    break;
                throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        accidentReportDto.setOpposingDriverPhone(opposingDriverPhone);


        String isRequestOnSite = "";
        while (true) {
            isRequestOnSite= (String) br.verifyRead(REQUEST_ON_SITE, isRequestOnSite);
            isRequestOnSite = isRequestOnSite.toUpperCase();
            if(isRequestOnSite.equals(YES)||isRequestOnSite.equals(NO))
                break;
        }
        boolean request = false;
        if (isRequestOnSite.equals(YES)) {
            request = true;
        }

        return accidentReportDto.setRequestOnSite(request);
    }

    private AccidentReportDto inputCarBreakdown(AccidentReportDto accidentReportDto) {
        inputPlaceAddress(accidentReportDto);
        inputCarNo(accidentReportDto);

        String symptom = "";
        symptom= (String) br.verifyRead(SYMPTOM, symptom);
        return accidentReportDto.setSymptom(symptom);
    }

    private AccidentReportDto inputInjuryAccidentInfo(AccidentReportDto accidentReportDto) {
        String injurySite = "";

        injurySite = (String)br.verifyRead(INJURY_SITE,injurySite);
        return accidentReportDto.setInjurySite(injurySite);
    }

    private AccidentReportDto inputFireAccidentInfo(AccidentReportDto accidentReportDto) {
        return inputPlaceAddress(accidentReportDto);
    }

    private AccidentReportDto inputCommonAccidentInfo(AccidentType selectAccidentType) {

        int year = 0;
        int month = 0;
        int day = 0;
        int  hour = 0;
        int min = 0;
        System.out.println(REPORT_ACCIDENT_INFO);
        System.out.println(INPUT_ACCIDENT_DATE);

        year = validateYear(year);
        month = vadliateMonth(month);
        day = validateDay(day);

        hour = validateHour(hour);
        min = validateMinute(min);

        String M = dateFormatter(month);
        String d = dateFormatter(day);
        String h = dateFormatter(hour);
        String m = dateFormatter(min);

        String dateFormat = year+SLASH+M+SLASH+d+SPACE+h+COLON+m;

        LocalDateTime accidentDate = LocalDateTime.parse(dateFormat, DateTimeFormatter.ofPattern(DATE_FORMAT_HOUR_MINUTE));

        return new AccidentReportDto().setAccidentType(selectAccidentType)
                .setDateOfAccident(accidentDate)
                .setDateOfReport(LocalDateTime.now());
    }

    private int validateYear(int year) {
        while (true) {
            try {
                year = (int) br.verifyRead(YEAR_EX, year);
                if (isYear(Integer.toString(year)))
                    break;
                throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return year;
    }

    private int vadliateMonth(int month) {
        while (true) {
            try{

                month = (int) br.verifyRead(MONTH, month);
                if(isMonth(month))
                    break;
                throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
            }catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return month;
    }

    private int validateDay(int day) {
        while (true) {
            try {
                day = (int) br.verifyRead(DAY, day);
                if(isDay(day))
                    break;
                throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }

        }
        return day;
    }

    private int validateHour(int hour) {
        while (true) {
            try {

                hour = (int) br.verifyRead(HOUR, hour);
                if (isHour(hour))
                    break;
                throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return hour;
    }

    private int validateMinute(int min) {
        while (true) {
            try{

                min = (int) br.verifyRead(MINUTE, min);
                if(isMinute(min))
                    break;
                throw new MyInadequateFormatException(INPUT_WRONG_FORMAT);
            } catch (MyInadequateFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return min;
    }

    private String dateFormatter(int time) {
        String date = Integer.toString(time);
        if (time < 10) {
            date = ZERO+time;
        }
        return date;
    }

    private AccidentType selectAccidentType() {
        contractList = new ContractDaoImpl();
        List<ContractwithTypeDto> contractTypes = contractList.findAllContractWithTypeByCustomerId(this.customer.getId());
        AccidentType accidentType = null;
            while (true) {
                try {
                    int insType = 0;
                    String query = createMenuAndClose(ACCIDENT_MENU, CAR_ACCIDENT, CAR_BREAKDOWN, INJURY_ACCIDENT, FIRE_ACCIDENT);
                    insType = br.verifyMenu(query, 4);

                    switch (insType) {
                        case 1 -> accidentType = AccidentType.CARACCIDENT;
                        case 2 -> accidentType = AccidentType.CARBREAKDOWN;
                        case 3 -> accidentType = AccidentType.INJURYACCIDENT;
                        case 4 -> accidentType = AccidentType.FIREACCIDENT;
                        case 0 -> accidentType = null;
                    }

                    if (accidentType == null)
                        break;

                    for (ContractwithTypeDto contractType : contractTypes) {
                        if(isValidateReportAccident(accidentType,contractType.getInsuranceType()))
                            return accidentType;
                    }
                    throw new MyInvalidAccessException(NO_INSURANCE_ABOUT_ACCIDENT);
                } catch (MyInvalidAccessException e) {
                    System.out.println(e.getMessage());
                }
            }
        return accidentType;
    }

    private boolean isValidateReportAccident(AccidentType accidentType, InsuranceType insuranceType) {
        return switch (accidentType) {
            case CARACCIDENT, CARBREAKDOWN -> insuranceType == InsuranceType.CAR;
            case FIREACCIDENT -> insuranceType == InsuranceType.FIRE;
            case INJURYACCIDENT -> insuranceType == InsuranceType.HEALTH;
        };
    }


    private void claimCompensation() {
        try {
            Accident accident = selectAccident();
            if (accident == null)
                return;
            showRequiredDocFile(accident);
        } catch (MyIllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showRequiredDocFile(Accident accident) {
        AccidentType accidentType = accident.getAccidentType();
        switch (accidentType) {
            case CARACCIDENT -> showCarAccidentDoc(accident);
            case FIREACCIDENT -> showFireAccidentDoc(accident);
            case INJURYACCIDENT ->showInjuryAccidentDoc(accident);
            case CARBREAKDOWN -> throw new MyIllegalArgumentException(CAR_BREAKDOWN_EXCEPTION);
        }
    }
    private void showCommonAccidentDoc(Accident accident) {

        submitDocFile(accident,AccDocType.CLAIMCOMP);
    }

    private void submitMedicalConfirmation(Accident accident) {
        submitDocFile(accident, AccDocType.MEDICALCERTIFICATION); // 진단서 제출
        submitDocFile(accident, AccDocType.CONFIRMADMISSIONDISCHARGE); // 입퇴원 확인서 제출
    }

    private void submitFile(Accident accident, AccDocType accDocType) {
        while (true) {
            try {
                String uploadMedicalCertification = "";
                FileDialogUtil.isExist(accident,accDocType);
                uploadMedicalCertification = (String) br.verifyRead(getSubmitDocQuery(accDocType.getDesc()),uploadMedicalCertification);
                if (uploadMedicalCertification.equals(YES)) {
                    AccidentDocumentFile accidentDocumentFile = customer.claimCompensation(accident, new AccidentDocumentFile().setAccidentId(accident.getId())
                            .setType(accDocType));
                    if (accidentDocumentFile == null) {
                        System.out.println(getSubmitDocCancel(accDocType.getDesc()));
                        break;
                    }
                    break;
                } else if (uploadMedicalCertification.equals(NO)) {
                    break;
                }
            } catch (MyFileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void submitDocFile(Accident accident, AccDocType accDocType) {
        System.out.println(getSubmitDoc(accDocType.getDesc()));

        while (true) {
            try {
                String medicalCertification = "";
                medicalCertification = (String) br.verifyRead(getDownloadDocExQuery(accDocType.getDesc()), medicalCertification);
                if (medicalCertification.equals(YES)) {
                    String dir = getExDirectory(accDocType.getDesc());
                    FileDialogUtil.download(dir);
                    break;
                } else if (medicalCertification.equals(NO)) {
                    break;
                }
            } catch (MyFileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        submitFile(accident,accDocType);
    }

    private void showCarAccidentDoc(Accident accident) {
        showCommonAccidentDoc(accident);
        submitMedicalConfirmation(accident);
        submitDocFile(accident,AccDocType.CARACCIDENTFACTCONFIRMATION); // 교통사고 사실 확인원
        submitDocFile(accident,AccDocType.PAYMENTRESOLUTION); // 자동차 보험금 지급 결의서

        boolean submitted = isAllDocSubmitted(accident, AccDocType.CLAIMCOMP, AccDocType.MEDICALCERTIFICATION, AccDocType.CONFIRMADMISSIONDISCHARGE
                , AccDocType.CARACCIDENTFACTCONFIRMATION, AccDocType.PAYMENTRESOLUTION);

        isFinishedClaimComp(accident, submitted);
    }

    private void isFinishedClaimComp(Accident accident, boolean submitted) {

        if (submitted) {
            connectCompEmployee(accident);
        } else {
            System.out.println(FAIL_TO_FINISH_CLAIM_COMPENSATION);
        }
    }

    private void connectCompEmployee(Accident accident) {

        Employee compEmployee = assignCompEmployee();
        System.out.println(compEmployee.print());


        while (true) {
            String rtVal = "";
            rtVal = (String) br.verifyRead(CHANGE_COMP_QUERY,rtVal);
            if (rtVal.equals(YES)) {
                String reasons = "";
                reasons=(String)br.verifyRead(INPUT_COMPLAIN,reasons);
                compEmployee = this.customer.changeCompEmp(reasons,compEmployee);
                System.out.println(compEmployee.print());
                System.out.println(SUCCESS_CHANGE_COMP_EMPLOYEE);
                break;
            }else if(rtVal.equals(NO)){
                break;
            }
        }
        accident.setEmployeeId(compEmployee.getId());
        accidentDao = new AccidentDaoImpl();
        accidentDao.updateCompEmployeeId(accident);
    }

    private void showFireAccidentDoc(Accident accident) {
        showCommonAccidentDoc(accident);
        submitFile(accident, AccDocType.PICTUREOFSITE); // 사고현장사진
        submitDocFile(accident, AccDocType.REPAIRESTIMATE); // 수리비 견적서
        submitDocFile(accident,AccDocType.REPAIRRECEIPT); // 수리비 영수증

        boolean submitted = isAllDocSubmitted(accident, AccDocType.CLAIMCOMP, AccDocType.PICTUREOFSITE, AccDocType.REPAIRESTIMATE, AccDocType.REPAIRRECEIPT);
        isFinishedClaimComp(accident, submitted);
    }

    private void showInjuryAccidentDoc(Accident accident) {
        showCommonAccidentDoc(accident);
        submitMedicalConfirmation(accident);

        boolean submitted = isAllDocSubmitted(accident, AccDocType.CLAIMCOMP, AccDocType.MEDICALCERTIFICATION, AccDocType.CONFIRMADMISSIONDISCHARGE);
        isFinishedClaimComp(accident, submitted);
    }

    private boolean isAllDocSubmitted(Accident accident, AccDocType ... accDocTypes) {
        Map<AccDocType, AccidentDocumentFile> accDocFileList = accident.getAccDocFileList();
        for (AccDocType accDocType : accDocTypes) {
            if (!accDocFileList.containsKey(accDocType)) {
                return false;
            }
        }
        return true;
    }


    public void payLogicforTest(Contract contract) {
        payLogic(contract);
    }
}
