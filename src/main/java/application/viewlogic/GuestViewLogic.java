package application.viewlogic;

import application.ViewLogic;
import domain.contract.*;
import domain.customer.Customer;
import domain.customer.CustomerList;
import domain.employee.Employee;
import domain.insurance.Insurance;
import domain.insurance.InsuranceList;
import domain.insurance.SalesAuthState;
import exception.InputException;
import exception.MyInadequateFormatException;

import java.util.Scanner;

import static domain.contract.BuildingType.*;
import static domain.contract.CarType.*;
import static main.utility.CustomerInfoFormatUtil.*;

import static utility.MessageUtil.*;
import static utility.PremiumInfoFinder.customerAgeFinder;
import static utility.PremiumInfoFinder.customerSexFinder;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : GuestViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class GuestViewLogic implements ViewLogic {
    String command;
    private Scanner sc;

    private InsuranceList insuranceList;
    private ContractList contractList;
    private CustomerList customerList;
    private Employee employee = new Employee();

    public GuestViewLogic(InsuranceList insuranceList, ContractList contractList, CustomerList customerList) {
        this.sc = new Scanner(System.in);
        this.insuranceList = insuranceList;
        this.contractList = contractList;
        this.customerList = customerList;
    }

    @Override
    public void showMenu() {
        createMenuAndClose("보험가입희망자메뉴", "보험가입");
    }

    @Override
    public void work(String command) {

        try {
            switch (command) {
                case "1":
//                    selectInsurance();
                    // 해결을?
                case "":
                    throw new InputException.InputNullDataException();
                default:
                    throw new InputException.InvalidMenuException();
            }
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
        selectInsurance();
    }

    private void selectInsurance() {
        if(insuranceList.readAll().size() == 0)
            throw new InputException.NoResultantException();

        while (true) {
            for (Insurance insurance : insuranceList.readAll()) {
                if (insurance.devInfo.getSalesAuthState() == SalesAuthState.PERMISSION)
                    System.out.println("보험코드: " + insurance.getId() + "\t보험이름: " + insurance.getName() + "\t보험종류: " + insurance.getInsuranceType());
            }

            try {
                System.out.println("가입할 보험상품의 보험코드를 입력하세요. \t(0 : 취소하기)");
                command = sc.nextLine();
                if (command.equals("0")) {
                    break;
                }
                if (command.isBlank()){
                    throw new InputException.InputNullDataException();
                }
                Insurance insurance = insuranceList.read(Integer.parseInt(command));
                if (insurance != null && insurance.devInfo.getSalesAuthState() == SalesAuthState.PERMISSION) {
                    System.out.println("보험설명: " + insurance.getDescription() + "\n보장내역: " + insurance.getGuarantee());
                    decideSigning(insurance);
                }
                else {
                    throw new InputException.NoResultantException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 코드를 입력해주세요");
            }
        }
    }
    private void decideSigning(Insurance insurance) {
        while (true) {
            try {
                createMenu("해당 보험상품을 가입하시겠습니까?", "가입", "취소");
                command = sc.nextLine();

                switch (command) {
                    // 가입
                    case "1":
                        Contract contract = new Contract();
                        contract.setInsuranceId(insurance.getId());
                        inputCustomerInfo(contract);
                        break;
                    // 취소
                    case "2":
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
            } catch (InputException.InputNullDataException e) {
                System.out.println(e.getMessage());
            } catch (InputException.InvalidMenuException e) {
                System.out.println("올바른 메뉴번호를 입력해주세요");
            }
        }
    }

    private void inputCustomerInfo(Contract contract) {
        Customer customer;
        String question;
        String ssn;
        String email;
        String phone;
        String name;

        while(true) {
            try {
                System.out.println("이름을 입력해주세요.");
                name = validateNameFormat(sc.nextLine());
                break;
            } catch (InputException.InputNullDataException | InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }

        while(true){
            try{
                System.out.println("주민번호를 입력해주세요. \t(______-*******)");
                ssn = validateSsnFormat(sc.nextLine());
                break;
            } catch (InputException.InputNullDataException | InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }

        while(true){
            try{
                System.out.println("연락처를 입력해주세요. \t(0__-____-____)");
                phone = validatePhoneFormat(sc.nextLine());
                break;
            } catch (InputException.InputNullDataException | InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }

        question = "주소를 입력해주세요.";
        String address = validateStringFormat(question);

        while (true){
            try{
                System.out.println("이메일을 입력해주세요. \t(_____@_____.___)");
                email = validateEmailFormat(sc.nextLine());
                break;
            } catch (InputException.InputNullDataException | InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }

        question = "직업을 입력해주세요.";
        String job = validateStringFormat(question);

        customer = employee.inputCustomerInfo(name, ssn, phone, address, email, job);

        switch (insuranceList.read(contract.getInsuranceId()).getInsuranceType()) {
            case HEALTH:
                inputHealthInfo(contract, customer);
                break;
            case FIRE:
                inputFireInfo(contract, customer);
                break;
            case CAR:
                inputCarInfo(contract, customer);
                break;
        }
    }

    private void inputHealthInfo(Contract contract, Customer customer) {
        int count = 0;
        String question;
        String diseaseDetail;

        question = "키를 입력해주세요. \t(단위: cm)";
        int height = validateIntFormat(question);

        question = "몸무게를 입력해주세요. \t(단위: kg)";
        int weight = validateIntFormat(question);

        question = "음주 여부를 입력해주세요. \t1.예 \t2.아니요 (기본은 아니요로 설정)";
        boolean isDrinking = validateBooleanFormat(question);
        if (isDrinking)
            count++;

        question = "흡연 여부를 입력해주세요. \t1.예 \t2.아니요 (기본은 아니요로 설정)";
        boolean isSmoking = validateBooleanFormat(question);
        if (isSmoking)
            count++;

        question = "운전 여부를 입력해주세요. \t1.예 \t2.아니요 (기본은 아니요로 설정)";
        boolean isDriving = validateBooleanFormat(question);
        if (isDriving)
            count++;

        question = "위험 취미 활동 여부를 입력해주세요. \t1.예 \t2.아니요 (기본은 아니요로 설정)";
        boolean isDangerActivity = validateBooleanFormat(question);
        if (isDangerActivity)
            count++;

        question = "약물 복용 여부를 입력해주세요. \t1.예 \t2.아니요 (기본은 아니요로 설정)";
        boolean isTakingDrug = validateBooleanFormat(question);
        if (isTakingDrug)
            count++;

        question = "질병 이력 여부를 입력해주세요. \t1.예 \t2.아니요 (기본은 아니요로 설정)";
        boolean isHavingDisease = validateBooleanFormat(question);
        if (isHavingDisease)
            count++;

        if (isHavingDisease) {
            question = "질병에 대한 상세 내용를 입력해주세요.";
            diseaseDetail = validateStringFormat(question);
        } else
            diseaseDetail = null;

        int age = customerAgeFinder(customer.getSsn());
        boolean sex = customerSexFinder(customer.getSsn());
        boolean riskPremiumCriterion = count >= 4;

        int premium = employee.planHealthInsurance(age, sex, riskPremiumCriterion);

        contract = employee.inputHealthInfo(height, weight, isDrinking, isSmoking, isDriving, isDangerActivity,
                isTakingDrug, isHavingDisease, diseaseDetail, premium);
        signContract(contract, customer);
    }



    private void inputFireInfo(Contract contract, Customer customer) {
        String question;
        BuildingType buildingType;

        while(true) {
            try {
                createMenu("건물종류를 입력해주세요.","주거용", "상업용", "산업용", "공업용");
                command  = sc.nextLine();
                switch (command) {
                    case "1":
                        buildingType = RESIDENTIAL;
                        break;
                    case "2":
                        buildingType = COMMERCIAL;
                        break;
                    case "3":
                        buildingType = INDUSTRIAL;
                        break;
                    case "4":
                        buildingType = INSTITUTIONAL;
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InputInvalidDataException();
                }
                break;
            } catch (InputException.InputNullDataException | InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }

        question = "건물면적을 입력해주세요. \t(단위: m^2 )";
        int buildingArea = validateIntFormat(question);

        question = "담보 금액을 입력해주세요. \t(단워: 원)";
        int collateralAmount = validateIntFormat(question);

        question = "자가 여부를 입력해주세요. \t1.예 \t2.아니요 (기본은 아니요로 설정)";
        boolean isSelfOwned = validateBooleanFormat(question);

        question = "실거주 여부를 입력해주세요. \t1.예 \t2.아니요 (기본은 아니요로 설정)";
        boolean isActualResidence = validateBooleanFormat(question);

        int premium = employee.planFireInsurance(buildingType, collateralAmount);

        contract = employee.inputFireInfo(buildingType, buildingArea, collateralAmount, isSelfOwned, isActualResidence, premium);
        signContract(contract, customer);
    }

    private void inputCarInfo(Contract contract, Customer customer) {
        String question;
        CarType carType;
        String carNo;

        while (true){
            try{
                System.out.println("차량번호를 입력해주세요. \t(지역명:__-**_-****))");
                carNo = validateCarNoFormat(sc.nextLine());
                break;
            } catch (InputException.InputNullDataException | InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }

        while(true) {
            try {
                createMenu("차종을 입력해주세요.", "경형", "소형", "준중형", "중형", "준대형", "대형", "스포츠카");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        carType = URBAN;
                        break;
                    case "2":
                        carType = SUBCOMPACT;
                        break;
                    case "3":
                        carType = COMPACT;
                        break;
                    case "4":
                        carType = MIDSIZE;
                        break;
                    case "5":
                        carType = LARGESIZE;
                        break;
                    case "6":
                        carType = FULLSIZE;
                        break;
                    case "7":
                        carType = SPORTS;
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InputInvalidDataException();
                }
                break;
            } catch (InputException.InputNullDataException | InputException.InputInvalidDataException e){
                System.out.println(e.getMessage());
            }
        }

        question = "모델이름을 입력해주세요.";
        String modelName = validateStringFormat(question);

        question = "차량연식을 입력해주세요. \t(단위: 년)";
        int modelYear = validateIntFormat(question);

        question = "차량가액을 입력해주세요. \t(단위: 년)";
        int value = validateIntFormat(question);

        int age = customerAgeFinder(customer.getSsn());
        int premium = employee.planCarInsurance(age, value);

        contract = employee.inputCarInfo(carNo, carType, modelName, modelYear, value, premium);
        signContract(contract, customer);
    }

    private void signContract(Contract contract, Customer customer) {
        System.out.println("조회된 귀하의 보험료는: " + contract.getPremium() + "원입니다.");
        while (true) {
            try {
                createMenu("보험가입을 신청하시겠습니까?", "가입", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        employee.registerContract(customerList, contractList, customer , contract);
                        System.out.println(customerList.read(customer.getId()));
                        System.out.println(contractList.read(contract.getId()));
                        System.out.println("가입이 완료되었습니다.");
                        break;
                    case "2":
                        System.out.println("가입이 취소되었습니다.");
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
                break;
            } catch (InputException.InputNullDataException e) {
                System.out.println(e.getMessage());
            } catch (InputException.InvalidMenuException e) {
                System.out.println("올바른 메뉴번호를 입력해주세요");
            }
        }
    }

    private String validateNameFormat(String name) {
        if(name.isBlank())
            throw new InputException.InputNullDataException();
        if(!isName(name))
            throw new InputException.InputInvalidDataException();
        return name;
    }

    private String validateSsnFormat(String ssn) {
        if(ssn.isBlank())
            throw new InputException.InputNullDataException();
        if(!isSsn(ssn))
            throw new InputException.InputInvalidDataException();
        return ssn;
    }

    private String validatePhoneFormat(String phone) {
        if(phone.isBlank())
            throw new InputException.InputNullDataException();
        if(!isPhone(phone))
            throw new MyInadequateFormatException();
        return phone;
    }

    private String validateEmailFormat(String email) {
        if(email.isBlank())
            throw new InputException.InputNullDataException();
        if(!isEmail(email))
            throw new MyInadequateFormatException();
        return email;
    }

    private String validateCarNoFormat(String carNo) {
        if(carNo.isBlank())
            throw new InputException.InputNullDataException();
        if(!isEmail(carNo))
            throw new MyInadequateFormatException();
        return carNo;
    }

    private int validateIntFormat(String question) {
        while (true) {
            try {
                System.out.println(question);
                String temp = sc.nextLine();
                if (temp.isBlank()){
                    throw new InputException.InputNullDataException();
                }
                return Integer.parseInt(temp);
            } catch (InputException.InputNullDataException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("ERROR!! : 유효하지 않은 값을 입력하였습니다.\n");
            }
        }
    }

    private boolean validateBooleanFormat(String question) {
        while (true) {
            try {
                System.out.println(question);
                String input = sc.nextLine();
                boolean temp = false;
                switch (input) {
                    case "1":
                        temp = true;
                        break;
                    case "2":
                    case "":
                        break;
                    default:
                        throw new InputException.InputInvalidDataException();
                }
                return temp;
            } catch (InputException.InputInvalidDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String validateStringFormat(String question){
        while (true){
            try{
                System.out.println(question);
                String temp = sc.nextLine();
                if(temp.isBlank())
                    throw new InputException.InputNullDataException();
                return temp;
            } catch (InputException.InputNullDataException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
