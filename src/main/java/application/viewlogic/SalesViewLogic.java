package application.viewlogic;


import application.ViewLogic;
import domain.contract.*;
import domain.customer.Customer;
import domain.customer.CustomerList;
import domain.employee.Department;
import domain.employee.Employee;
import domain.employee.EmployeeList;
import domain.insurance.Insurance;
import domain.insurance.InsuranceList;
import domain.insurance.SalesAuthState;
import exception.InputException;
import utility.InputValidation;

import java.util.Scanner;

import static domain.contract.BuildingType.*;
import static domain.contract.CarType.*;
import static utility.MessageUtil.*;

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

    private InsuranceList insuranceList;
    private ContractList contractList;
    private CustomerList customerList;
    private EmployeeList employeeList;
    private Employee employee;
    private Customer customer;
    private InputValidation input;

    public SalesViewLogic(InsuranceList insuranceList, ContractList contractList, CustomerList customerList, EmployeeList employeeList) {
        this.sc = new Scanner(System.in);
        this.insuranceList = insuranceList;
        this.contractList = contractList;
        this.customerList = customerList;
        this.employeeList = employeeList;
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
        }
    }

    private void initEmployee() {
        while(true) {
            try {
                System.out.println("직원 ID을 입력하세요.");
                for(Employee employee : this.employeeList.readAll()) {
                    if (employee.getDepartment() == Department.SALES)
                        System.out.println(employee.print());
                }
                command = sc.nextLine();
                if (command.isBlank()){
                    throw new InputException.InputNullDataException();
                }
                this.employee = this.employeeList.read(Integer.parseInt(command));
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


    public void planInsurance() {
        if(insuranceList.readAll().size() == 0)
            throw new InputException.NoResultantException();

        while(true) {
            for (Insurance insurance : insuranceList.readAll()) {
                if (insurance.devInfo.getSalesAuthState() == SalesAuthState.PERMISSION)
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
                Insurance insurance = insuranceList.read(Integer.parseInt(command));
                if (insurance != null && insurance.devInfo.getSalesAuthState() == SalesAuthState.PERMISSION)  {
                    System.out.println("보험설명 : " + insurance.getDescription() + "\n보장내역 : " + insurance.getGuarantee());
                    switch (insurance.getInsuranceType()) {
                        case HEALTH:
                            planHealthInsurance(insurance);
                            break;
                        case FIRE:
                            planFireInsurance(insurance);
                            break;
                        case CAR:
                            planCarInsurance(insurance);
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

    private void planHealthInsurance(Insurance insurance) {
        Contract contract;
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

        int premium = employee.planHealthInsurance(age, sex, riskPremiumCriterion);

        System.out.println("조회된 귀하의 보험료는 " + premium + "원 입니다.");

        while (true) {
            try {
                createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        contract = employee.concludeHealthContract(insurance.getId(), premium, isDrinking, isSmoking, isDriving, isDangerActivity, isTakingDrug, isHavingDisease);
                        inputCustomerInfo(contract);
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

    private void planFireInsurance(Insurance insurance) {
        Contract contract;
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
        int collateralAmount = input.validateIntFormat(question);

        int premium = employee.planFireInsurance(buildingType, collateralAmount);

        System.out.println("귀하의 보험료는 " + premium + "원 입니다.");

        while (true) {
            try {
                createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        contract = employee.concludeFireContract(insurance.getId(), premium, buildingType, collateralAmount);
                        inputCustomerInfo(contract);
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

    private void planCarInsurance(Insurance insurance) {
        Contract contract;
        String question;

        question = "대상 나이를 입력하세요.";
        int age = input.validateIntFormat(question);

        System.out.println("차량가액을 입력하세요.");
        int value = input.validateIntFormat(question);

        int premium = employee.planCarInsurance(age, value);

        System.out.println("귀하의 보험료는 " + premium + "원 입니다.");

        while (true) {
            try {
                createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
                        contract = employee.concludeCarContract(insurance.getId(), premium, value);
                        inputCustomerInfo(contract);
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

    private void inputCustomerInfo(Contract contract) {
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
                        customer = customerList.read(customerId);
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

        switch (insuranceList.read(contract.getInsuranceId()).getInsuranceType()) {
            case HEALTH:
                inputHealthInfo(customer, contract);
                break;
            case FIRE:
                inputFireInfo(customer, contract);
                break;
            case CAR:
                inputCarInfo(customer, contract);
                break;
        }
    }
    private void inputHealthInfo(Customer customer, Contract contract) {
        String question;
        String diseaseDetail;

        question = "고객 키를 입력해주세요. \t(단위 : cm)";
        int height = input.validateIntFormat(question);

        question = "고객 몸무게를 입력해주세요. \t(단위 : kg)";
        int weight = input.validateIntFormat(question);

        if (contract.getHealthInfo().isHavingDisease()) {
            question = "질병에 대한 상세 내용를 입력해주세요.";
            diseaseDetail = input.validateStringFormat(question);
        } else
            diseaseDetail = null;

        contract = employee.inputHealthInfo(contract, height, weight, diseaseDetail);
        concludeContract(contract, customer);
    }

    private void inputFireInfo(Customer customer, Contract contract) {
        String question;

        question = "대상 건물면적을 입력해주세요. \t(단위 : m^2 )";
        int buildingArea = input.validateIntFormat(question);

        question = "고객 자가 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isSelfOwned = input.validateBooleanFormat(question);

        question = "고객 실거주 여부를 입력해주세요. \t1. 예 \t2. 아니요";
        boolean isActualResidence = input.validateBooleanFormat(question);

        contract = employee.inputFireInfo(contract, buildingArea, isSelfOwned, isActualResidence);
        concludeContract(contract, customer);
    }

    private void inputCarInfo(Customer customer, Contract contract) {
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

        contract = employee.inputCarInfo(contract, carNo, carType, modelName, modelYear);
        concludeContract(contract, customer);
    }


    private void concludeContract(Contract contract, Customer customer) {
        while(true) {
            try {
                createMenu("보험 계약을을 체결하시겠습니까?", "계약체결", "취소");
                command = sc.nextLine();
                switch (command) {
                    case "1":
//                        if (customer.getId() == 0) {
//                            customerList.create(customer);
//                        }
//                        contract.setConditionOfUw(ConditionOfUw.WAIT)
//                                .setEmployeeId(employee.getId())
//                                .setCustomerId(customer.getId());
//                        contractList.create(contract);
//                        System.out.println(customerList.read(customer.getId()));
//                        System.out.println(contractList.read(contract.getId()));
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
            }
        }
    }
}
