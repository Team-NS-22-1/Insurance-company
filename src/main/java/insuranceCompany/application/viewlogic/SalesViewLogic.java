package insuranceCompany.application.viewlogic;


import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.dao.employee.EmployeeDao;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.contract.*;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Department;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.SalesAuthorizationState;
import insuranceCompany.application.global.exception.InputException;
import insuranceCompany.application.global.utility.InputValidation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static insuranceCompany.application.domain.contract.BuildingType.*;
import static insuranceCompany.application.domain.contract.CarType.*;
import static insuranceCompany.application.global.utility.MessageUtil.createMenu;
import static insuranceCompany.application.global.utility.MessageUtil.createMenuAndClose;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : SalesViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class SalesViewLogic implements ViewLogic {
    String command;
    private Scanner sc;

    private HealthContract healthContract;
    private FireContract fireContract;
    private CarContract carContract;
    private Employee employee;
    private Customer customer;
    private Insurance insurance;
    private InputValidation input;

    public SalesViewLogic() {
        this.sc = new Scanner(System.in);
        this.input = new InputValidation();
    }

    @Override
    public void showMenu() {
        createMenuAndClose("영업팀 메뉴", "보험상품설계");
    }

    @Override
    public void work(String command) {
        try {
            switch (command) {
                // 보험상품설계
                case "1":
                    initEmployee();
                    planInsurance();
                    break;
                case "":
                    throw new InputException.InputNullDataException();
                default:
                    throw new InputException.InvalidMenuException();
            }
        } catch(InputException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initEmployee() {
        EmployeeDao employeeDao = new EmployeeDao();
        ArrayList<Employee> employees = employeeDao.readAllSalesEmployee();
        while(true) {
            try {
                System.out.println("직원 ID을 입력하세요.");
                for(Employee employee : employees) {
                    System.out.println(employee.print());
                }
                command = sc.nextLine();
                if (command.isBlank()){
                    throw new InputException.InputNullDataException();
                }
                employeeDao = new EmployeeDao();
                this.employee = employeeDao.read(Integer.parseInt(command));
                if (employee != null && employee.getDepartment() == Department.SALES)  {
                    break;
                }
                else {
                    throw new InputException.NoResultantException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 Id를 입력해주세요");
            }
        }
    }


    public void planInsurance() throws SQLException {
        InsuranceDaoImpl insuranceDao = new InsuranceDaoImpl();
        ArrayList<Insurance> insurances = insuranceDao.readAll();
        if(insurances.size() == 0)
            throw new InputException.NoResultantException();
        while (true) {
            for (Insurance insurance : insurances) {
                if (insurance.devInfo.getSalesAuthState() == SalesAuthorizationState.PERMISSION)
                    System.out.println("보험코드 : " + insurance.getId() + "\t보험이름 : " + insurance.getName() + "\t보험종류 : " + insurance.getInsuranceType());
            }

            try {
                System.out.println("설계할 보험상품의 보험코드를 입력하세요. \t(0 : 취소하기)");
                command = sc.nextLine();
                if (command.equals("0")) {
                    break;
                }
                if (command.isBlank()){
                    throw new InputException.InputNullDataException();
                }
                insuranceDao = new InsuranceDaoImpl();
                insurance = insuranceDao.read(Integer.parseInt(command));
                if (insurance.devInfo.getSalesAuthState() == SalesAuthorizationState.PERMISSION) {
                    System.out.println("보험설명: " + insurance.getDescription() + "\n보장내역: " + insurance.getGuaranteeList());
                    switch (insurance.getInsuranceType()) {
                        case HEALTH:
                            planHealthInsurance();
                            break;
                        case FIRE:
                            planFireInsurance();
                            break;
                        case CAR:
                            planCarInsurance();
                            break;
                    }
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

    private void planHealthInsurance() {
        int count = 0;
        String question;

        question = "대상 나이를 입력하세요.";
        int age = input.validateIntFormat(question);

        question = "대상 성별을 입력하세요. \t1. 남 \t2. 여";
        boolean sex = input.validateBooleanFormat(question);

        question = "음주 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isDrinking = input.validateBooleanFormat(question);
        if (isDrinking)
            count++;

        question = "흡연 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isSmoking = input.validateBooleanFormat(question);
        if (isSmoking)
            count++;

        question = "운전 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isDriving = input.validateBooleanFormat(question);
        if (isDriving)
            count++;

        question = "위험 취미 활동 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isDangerActivity = input.validateBooleanFormat(question);
        if (isDangerActivity)
            count++;

        question = "약물 복용 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isTakingDrug = input.validateBooleanFormat(question);
        if (isTakingDrug)
            count++;

        question = "질병 이력 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isHavingDisease = input.validateBooleanFormat(question);
        if (isHavingDisease)
            count++;

        boolean riskPremiumCriterion = count >= 4;

        int premium = employee.planHealthInsurance(age, sex, riskPremiumCriterion, insurance);

        System.out.println("조회된 귀하의 보험료는 " + premium + "원 입니다.");

        while (true) {
            try {
                createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        healthContract = employee.concludeHealthContract(insurance.getId(), premium, isDrinking, isSmoking, isDriving, isDangerActivity, isTakingDrug, isHavingDisease);
                        inputCustomerInfo();
                        break;
                    case "2":
                        System.out.println("계약이 취소되었습니다.");
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
                break;
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void planFireInsurance() {
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
            } catch (InputException e){
                System.out.println(e.getMessage());
            }
        }

        question = "담보 금액을 입력해주세요. \t(단워 : 원)";
        Long collateralAmount = input.validateLongFormat(question);

        int premium = employee.planFireInsurance(buildingType, collateralAmount, insurance);

        System.out.println("귀하의 보험료는 " + premium + "원 입니다.");

        while (true) {
            try {
                createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        fireContract = employee.concludeFireContract(insurance.getId(), premium, buildingType, collateralAmount);
                        inputCustomerInfo();
                        break;
                    case "2":
                        System.out.println("계약이 취소되었습니다.");
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
                break;
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void planCarInsurance() {
        String question;

        question = "대상 나이를 입력하세요.";
        int age = input.validateIntFormat(question);

        question = "차량가액을 입력하세요.";
        Long value = input.validateLongFormat(question);

        int premium = employee.planCarInsurance(age, value, insurance);

        System.out.println("귀하의 보험료는 " + premium + "원 입니다.");

        while (true) {
            try {
                createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        carContract = employee.concludeCarContract(insurance.getId(), premium, value);
                        inputCustomerInfo();
                        break;
                    case "2":
                        System.out.println("계약이 취소되었습니다.");
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
                break;
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void inputCustomerInfo() {
        boolean isLoop = true;

        while (isLoop) {
            try {
                createMenu("등록된 고객입니까?", "예", "아니요");
                command = sc.nextLine();

                switch (command) {
                    // 등록 고객
                    case "1":
                        System.out.println("고객 ID를 입력해주세요.");
                        int customerId = sc.nextInt();
                        CustomerDaoImpl customerDao = new CustomerDaoImpl();
                        customer = customerDao.read(customerId);
                        isLoop = false;
                        break;
                    // 미등록 고객
                    case "2":
                        String question;

                        question = "고객 이름을 입력해주세요.";
                        String name = input.validateDistinctFormat(question, 1);

                        question = "고객 주민번호를 입력해주세요. \t(______-*******)";
                        String ssn = input.validateDistinctFormat(question, 2);

                        question = "고객 연락처를 입력해주세요. \t(0__-____-____)";
                        String phone = input.validateDistinctFormat(question, 3);

                        question = "고객 주소를 입력해주세요.";
                        String address = input.validateStringFormat(question);

                        question = "고객 이메일을 입력해주세요. \t(_____@_____.___)";
                        String email = input.validateDistinctFormat(question, 4);

                        question = "고객 직업을 입력해주세요.";
                        String job = input.validateStringFormat(question);

                        customer = employee.inputCustomerInfo(name, ssn, phone, address, email, job);

                        isLoop = false;
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }

        switch (insurance.getInsuranceType()) {
            case HEALTH:
                inputHealthInfo();
                break;
            case FIRE:
                inputFireInfo();
                break;
            case CAR:
                inputCarInfo();
                break;
        }
    }
    private void inputHealthInfo() {
        String question;
        String diseaseDetail;

        question = "고객 키를 입력해주세요. \t(단위 : cm)";
        int height = input.validateIntFormat(question);

        question = "고객 몸무게를 입력해주세요. \t(단위 : kg)";
        int weight = input.validateIntFormat(question);

        if (healthContract.isHavingDisease()) {
            question = "질병에 대한 상세 내용를 입력해주세요.";
            diseaseDetail = input.validateStringFormat(question);
        } else
            diseaseDetail = null;

        healthContract = employee.inputHealthInfo(healthContract, height, weight, diseaseDetail);
        concludeContract(healthContract);
    }

    private void inputFireInfo() {
        String question;

        question = "대상 건물면적을 입력해주세요. \t(단위 : m^2 )";
        int buildingArea = input.validateIntFormat(question);

        question = "고객 자가 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isSelfOwned = input.validateBooleanFormat(question);

        question = "고객 실거주 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isActualResidence = input.validateBooleanFormat(question);

        fireContract = employee.inputFireInfo(fireContract, buildingArea, isSelfOwned, isActualResidence);
        concludeContract(fireContract);
    }

    private void inputCarInfo() {
        String question;
        CarType carType;

        question = "고객 차량번호를 입력해주세요. \t(__-**_-****, 처음 두 자리는 지역명)";
        String carNo = input.validateDistinctFormat(question, 5);

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
            } catch (InputException e){
                System.out.println(e.getMessage());
            }
        }

        question = "모델이름을 입력해주세요.";
        String modelName = input.validateStringFormat(question);

        question = "차량연식을 입력해주세요. \t(단위 : 년)";
        int modelYear = input.validateIntFormat(question);

        carContract = employee.inputCarInfo(carContract, carNo, carType, modelName, modelYear);
        concludeContract(carContract);
    }


    private void concludeContract(Contract contract) {
        while(true) {
            try {
                createMenu("보험 계약을을 체결하시겠습니까?", "계약체결", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        employee.registerContract(customer , contract, employee);
                        System.out.println(customer);
                        System.out.println(contract);
                        System.out.println("계약을 체결하였습니다.");
                        break;
                    case "2":
                        System.out.println("계약을 취소되었습니다.");
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InvalidMenuException();
                }
                break;
            } catch (InputException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
