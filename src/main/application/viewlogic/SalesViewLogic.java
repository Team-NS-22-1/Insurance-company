package main.application.viewlogic;


import main.application.ViewLogic;
import main.domain.contract.*;
import main.domain.customer.Customer;
import main.domain.customer.CustomerListImpl;
import main.domain.employee.Department;
import main.domain.employee.Employee;
import main.domain.employee.EmployeeListImpl;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.SalesAuthState;

import java.util.Scanner;

import static main.domain.contract.BuildingType.*;
import static main.domain.contract.CarType.*;
import static main.utility.MessageUtil.createMenu;

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
    int command;
    private Scanner sc;

    private InsuranceListImpl insuranceList;
    private ContractListImpl contractList;
    private CustomerListImpl customerList;
    private EmployeeListImpl employeeList;
    private Employee employee;
    private Customer customer;

    public SalesViewLogic(InsuranceListImpl insuranceList, ContractListImpl contractList, CustomerListImpl customerList, EmployeeListImpl employeeList) {
        this.sc = new Scanner(System.in);
        this.insuranceList = insuranceList;
        this.contractList = contractList;
        this.customerList = customerList;
        this.employeeList = employeeList;
    }

    @Override
    public void showMenu() {
        createMenu("영업팀 메뉴", "보험상품설계");
    }

    @Override
    public void work(String command) {

        switch (command) {
            // 보험상품설계
            case "1":
                initEmployee();
                planInsurance();
                break;
            default:
                System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
                break;
        }
    }

    private void initEmployee() {
        System.out.println("직원 ID을 입력하세요.");
        for(Employee employee : this.employeeList.readAll()) {
            if (employee.getDepartment() == Department.SALES)
                System.out.println(employee.print());
        }
        int employeeId = sc.nextInt();
        this.employee = this.employeeList.read(employeeId);
    }


    public void planInsurance() {
        boolean isLoop = true;

        while(isLoop) {
            for (Insurance insurance : insuranceList.readAll()) {
                if (insurance.devInfo.getSalesAuthState() == SalesAuthState.PERMISSION)
                    System.out.println("보험코드: " + insurance.getId() + "\t보험이름: " + insurance.getName() + "\t보험종류: " + insurance.getInsuranceType());
            }

            System.out.println("설계할 보험상품의 보험코드를 입력하세요.");


            int choice = sc.nextInt();
            Insurance insurance = insuranceList.read(choice);

            if (insurance == null) {
                System.out.println("해당 보험상품이 없습니다.");
                break;
            } else {
                System.out.println("보험설명: " + insurance.getDescription() + "\n보장내역: " + insurance.getGuarantee());
                switch (insurance.getInsuranceType()) {
                    case HEALTH:
                        planHealthInsurance(insurance.getId());
                        break;
                    case FIRE:
                        planFireInsurance(insurance.getId());
                        break;
                    case CAR:
                        planCarInsurance(insurance.getId());
                        break;
                }


            }
        }
    }

    private void planHealthInsurance(int insuranceId) {
        boolean isLoop = true;
        int premium;
        int count = 0;
        boolean riskPremiumCriterion;

        System.out.println("대상 나이를 입력하세요.");
        int age = sc.nextInt();
        createMenu("대상 성별을 입력하세요.", "남", "여");
        command = sc.nextInt();
        boolean sex = command == 1;

        createMenu("음주 여부를 입력해주세요.", "예", "아니요");
        command = sc.nextInt();
        boolean isDrinking = false;
        if (command == 1) {
            isDrinking = true;
            count++;
        }

        createMenu("흡연 여부를 입력해주세요.", "예", "아니요");
        command = sc.nextInt();
        boolean isSmoking = false;
        if (command == 1) {
            isSmoking = true;
            count++;
        }

        createMenu("운전 여부를 입력해주세요.", "예", "아니요");
        command = sc.nextInt();
        boolean isDriving = false;
        if (command == 1) {
            isDriving = true;
            count++;
        }

        createMenu("위험 취미 활동 여부를 입력해주세요.", "예", "아니요");
        command = sc.nextInt();
        boolean isDangerActivity = false;
        if (command == 1) {
            isDangerActivity = true;
            count++;
        }

        createMenu("약물 복용 여부를 입력해주세요.", "예", "아니요");
        command = sc.nextInt();
        boolean isTakingDrug = false;
        if (command == 1) {
            isTakingDrug = true;
            count++;
        }

        createMenu("질병 이력 여부를 입력해주세요.", "예", "아니요");
        command = sc.nextInt();
        boolean isHavingDisease = false;
        if (command == 1) {
            isHavingDisease = true;
            count++;
        }

        riskPremiumCriterion = count >= 4;

        // premium = employee.planHealthInsurance(age, sex, riskPremiumCriterion);
        premium = insuranceList.readPremium(insuranceId);

        System.out.println("귀하의 보험료는 " + premium + " 입니다.");
        createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
        int command = sc.nextInt();

        switch (command) {
            // 계약
            case 1:
                isLoop = false;
                Contract contract = new Contract();
                contract.setInsuranceId(insuranceId)
                        .setPremium(premium)
                        .setHealthInfo(new HealthInfo().setDrinking(isDrinking)
                                .setSmoking(isSmoking)
                                .setDriving(isDriving)
                                .setDangerActivity(isDangerActivity)
                                .setTakingDrug(isTakingDrug)
                                .setHavingDisease(isHavingDisease)
                        );
                inputCustomerInfo(contract);
                break;
            // 취소
            case 2:
                isLoop = false;
                break;
            default:
                System.out.println("잘못 입력하셨습니다.");
                break;
        }
    }

    private void planFireInsurance(int insuranceId) {
        boolean isLoop = true;
        int premium;

        BuildingType buildingType = null;
        createMenu("건물종류를 입력해주세요.", "주거용", "상업용", "산업용", "공업용");
        command = sc.nextInt();
        switch (command) {
            case 1:
                buildingType = RESIDENTIAL;
                break;
            case 2:
                buildingType = COMMERCIAL;
                break;
            case 3:
                buildingType = INDUSTRIAL;
                break;
            case 4:
                buildingType = INSTITUTIONAL;
                break;
        }
        System.out.println("담보금액을 입력하세요.");
        int collateralAmount = sc.nextInt();

        // premium = employee.planFireInsurance(buildingType, collateralAmount);
        premium = insuranceList.readPremium(insuranceId);

        System.out.println("귀하의 보험료는 " + premium + " 입니다.");
        createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
        command = sc.nextInt();

        switch (command) {
            // 계약
            case 1:
                isLoop = false;
                Contract contract = new Contract();
                contract.setInsuranceId(insuranceId)
                        .setPremium(premium)
                        .setBuildingInfo(new BuildingInfo().setBuildingType(buildingType)
                                .setCollateralAmount(collateralAmount));
                inputCustomerInfo(contract);
                break;
            // 취소
            case 2:
                isLoop = false;
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }

    private void planCarInsurance(int insuranceId) {
        boolean isLoop = true;
        int premium;

        System.out.println("대상 나이를 입력하세요.");
        int driverAge = sc.nextInt();
        System.out.println("차량가액을 입력하세요.");
        int value = sc.nextInt();

        // premium = employee.planCarInsurance(driverAge, value);
        premium = insuranceList.readPremium(insuranceId);

        System.out.println("귀하의 보험료는 " + premium + " 입니다.");
        createMenu("보험계약을 진행하시겠습니까?","계약", "취소");
        command = sc.nextInt();

        switch (command) {
            // 계약
            case 1:
                isLoop = false;
                Contract contract = new Contract();
                contract.setInsuranceId(insuranceId)
                        .setPremium(premium)
                        .setCarInfo(new CarInfo().setValue(value));
                inputCustomerInfo(contract);
                break;
            // 취소
            case 2:
                isLoop = false;
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }

    private void inputCustomerInfo(Contract contract) {
        boolean isLoop = true;
        while (isLoop) {
            try {
                createMenu("등록된 고객입니까?", "예", "아니요");
                command = sc.nextInt();

                switch (command) {
                    // 등록 고객
                    case 1:
                        System.out.println("고객 ID를 입력해주세요.");
                        int customerId = sc.nextInt();
                        customer = customerList.read(customerId);
                        isLoop = false;
                        break;
                    // 미등록 고객
                    case 2:
                        customer = new Customer();

                        System.out.print("고객 이름: ");
                        String name = sc.next();

                        System.out.print("고객 주민번호:");
                        String ssn = sc.next();

                        System.out.print("고객 연락처: ");
                        String phone = sc.next();

                        System.out.print("고객 주소: ");
                        String address = sc.next();

                        System.out.print("고객 이메일 주소: ");
                        String email = sc.next();

                        System.out.print("고객 직업: ");
                        String job = sc.next();

                        customer.setName(name)
                                .setSsn(ssn)
                                .setPhone(phone)
                                .setAddress(address)
                                .setEmail(email)
                                .setJob(job);
                        isLoop = false;
                        break;
                    default:
                        throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("잘못된 입력입니다.");
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
        String diseaseDetail;
        System.out.print("고객 키: ");
        int height = sc.nextInt();

        System.out.print("고객 몸무게: ");
        int weight = sc.nextInt();

        if (contract.getHealthInfo().isHavingDisease()) {
            System.out.println("고객 질병 상세 내용: ");
            diseaseDetail = sc.next();
        } else {
            diseaseDetail = null;
        }

        contract.setHealthInfo(new HealthInfo().setHeight(height)
                .setWeight(weight)
                .setDiseaseDetail(diseaseDetail)
        );

        concludeContract(contract, customer);
    }

    private void inputFireInfo(Customer customer, Contract contract) {
        System.out.println("대상 건물면적: ");
        int buildingArea = sc.nextInt();

        createMenu("고객 자가 여부", "예", "아니요");
        command = sc.nextInt();
        boolean isSelfOwned = command == 1;

        createMenu("고객 실거주 여부", "예", "아니요");
        command = sc.nextInt();
        boolean isActualResidence = command == 1;

        contract.setBuildingInfo(new BuildingInfo().setBuildingArea(buildingArea)
                .setSelfOwned(isSelfOwned)
                .setActualResidence(isActualResidence)
        );

        concludeContract(contract, customer);
    }

    private void inputCarInfo(Customer customer, Contract contract) {
        System.out.println("차량번호: ");
        String carNo = sc.next();

        CarType carType = null;
        createMenu("차종", "경형", "소형", "준중형", "중형", "준대형", "대형", "스포츠카");
        command = sc.nextInt();
        switch (command) {
            case 1:
                carType = URBAN;
                break;
            case 2:
                carType = SUBCOMPACT;
                break;
            case 3:
                carType = COMPACT;
                break;
            case 4:
                carType = MIDSIZE;
                break;
            case 5:
                carType = LARGESIZE;
                break;
            case 6:
                carType = FULLSIZE;
                break;
            case 7:
                carType = SPORTS;

                break;
        }

        System.out.println("모델이름을 입력해주세요.");
        String modelName = sc.next();

        System.out.println("차량연식을 입력해주세요.");
        int modelYear = sc.nextInt();

        contract.setCarInfo(new CarInfo().setCarNo(carNo)
                .setCarType(carType)
                .setModelName(modelName)
                .setModelYear(modelYear)
        );

        concludeContract(contract, customer);
    }


    private void concludeContract(Contract contract, Customer customer) {
        createMenu("보험 계약을을 체결하시겠습니까?","계약체결", "취소");
        command = sc.nextInt();
        switch (command) {
            case 1:
                if (customer.getId() == 0) {
                    customerList.create(customer);
                }
                contract.setConditionOfUw(ConditionOfUw.WAIT)
                        .setEmployeeId(employee.getId())
                        .setCustomerId(customer.getId());
                contractList.create(contract);
                System.out.println(customerList.read(customer.getId()));
                System.out.println(contractList.read(contract.getId()));
                System.out.println("계약을 체결하였습니다.");
                break;
            case 2:
                System.out.println("계약을 취소되었습니다.");
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
}
