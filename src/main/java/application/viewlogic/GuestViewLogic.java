package application.viewlogic;

import application.ViewLogic;
import domain.contract.*;
import domain.customer.Customer;
import domain.customer.CustomerListImpl;
import domain.insurance.Insurance;
import domain.insurance.InsuranceListImpl;
import domain.insurance.SalesAuthState;
import exception.InputException;
import exception.MyInadequateFormatException;

import java.util.Scanner;

import static domain.contract.BuildingType.*;
import static domain.contract.CarType.*;
import static utility.CustomerInfoFormatUtil.*;
import static utility.MessageUtil.createMenu;

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

    private InsuranceListImpl insuranceList;
    private ContractListImpl contractList;
    private CustomerListImpl customerList;

    public GuestViewLogic(InsuranceListImpl insuranceList, ContractListImpl contractList, CustomerListImpl customerList) {
        this.sc = new Scanner(System.in);
        this.insuranceList = insuranceList;
        this.contractList = contractList;
        this.customerList = customerList;
    }

    @Override
    public void showMenu() {
        createMenu("보험가입희망자메뉴", "보험가입");
    }

    @Override
    public void work(String command) {

        try {
            switch (command) {
                case "1":
                    selectInsurance();
                // 해결을?
                case "":
                    throw new InputException.InputNullDataException();
                default:
                    throw new InputException.InvalidMenuException();
            }
        } catch (InputException.InputNullDataException e) {
            System.out.println(e.getMessage());
        } catch (InputException.InvalidMenuException e){
            System.out.println("올바른 메뉴번호를 입력해주세요");
        }
    }

    private void selectInsurance() {
        boolean isLoop = true;

        while (isLoop) {

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
            } catch (InputException.InputNullDataException e) {
                System.out.println("보험 코드를 입력해주세요");
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 코드를 입력해주세요");
            } catch (InputException.NoResultantException e) {
                System.out.println("조회된 보험상품이 없습니다. 다시 입력해주세요.");
            }
        }
    }
    private void decideSigning(Insurance insurance) {
        boolean isLoop = true;
        while (isLoop) {
            try {
                createMenu("해당 보험상품을 가입하시겠습니까?", "가입", "취소");
                command = sc.nextLine();

                switch (command) {
                    // 가입
                    case "1":
                        isLoop = false;
                        Contract contract = new Contract();
                        contract.setInsuranceId(insurance.getId());
                        inputCustomerInfo(contract);
                        break;
                    // 취소
                    case "2":
                        isLoop = false;
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
        Customer customer = new Customer();
        String question;
        String ssn;
        String email;
        String phone;
        String name;
//        String address;
//        String job;

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

        customer.setName(name)
                .setSsn(ssn)
                .setPhone(phone)
                .setAddress(address)
                .setEmail(email)
                .setJob(job);

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

        contract.setHealthInfo(new HealthInfo().setHeight(height)
                .setWeight(weight)
                .setDrinking(isDrinking)
                .setSmoking(isSmoking)
                .setDriving(isDriving)
                .setDangerActivity(isDangerActivity)
                .setTakingDrug(isTakingDrug)
                .setHavingDisease(isHavingDisease)
                .setDiseaseDetail(diseaseDetail)
        );

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

        System.out.println(isSelfOwned);
        System.out.println(isActualResidence);

        contract.setBuildingInfo(new BuildingInfo().setBuildingType(buildingType)
                .setBuildingArea(buildingArea)
                .setCollateralAmount(collateralAmount)
                .setSelfOwned(isSelfOwned)
                .setActualResidence(isActualResidence)
        );

        signContract(contract, customer);
    }

    private void inputCarInfo(Contract contract, Customer customer) {
        String question;
        CarType carType;
        String carNo;
//        String modelName;

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

        contract.setCarInfo(new CarInfo().setCarNo(carNo)
                .setCarType(carType)
                .setModelName(modelName)
                .setModelYear(modelYear)
                .setValue(value)
        );

        signContract(contract, customer);
    }

    private void signContract(Contract contract, Customer customer) {
        boolean isLoop = true;
        int premium = insuranceList.readPremium(contract.getInsuranceId());
        // premium = employee.planHealthInsurance(age, sex, riskPremiumCriterion);
        // premium = employee.planFireInsurance(buildingType, collateralAmount);
        // premium = employee.planCarInsurance(driverAge, value);

        System.out.println("조회된 귀하의 보험료는: " + premium + "원입니다.");
        while (isLoop) {
            try {
                createMenu("보험가입을 신청하시겠습니까?", "가입", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        isLoop = false;
                        customerList.create(customer);
                        contract.setPremium(premium)
                                .setCustomerId(customer.getId())
                                .setConditionOfUw(ConditionOfUw.WAIT);
                        contractList.create(contract);
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
