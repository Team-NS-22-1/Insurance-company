package main.application.viewlogic;

import main.domain.contract.*;
import main.domain.customer.Customer;
import main.domain.customer.CustomerListImpl;
import main.domain.employee.Department;
import main.domain.employee.Employee;
import main.domain.employee.EmployeeListImpl;
import main.domain.employee.Position;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.InsuranceType;
import main.application.ViewLogic;
import main.exception.InputException;
import main.exception.MyCloseSequence;
import main.exception.MyIllegalArgumentException;

import java.util.Map;
import java.util.Scanner;

import static main.utility.MessageUtil.createMenu;

/**
 * packageName :  main.domain.viewUtils.viewlogic
 * fileName : UWViewLogic
 * author :  규현
 * date : 2022-05-10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class UWViewLogic implements ViewLogic {

    private Scanner sc;
    private EmployeeListImpl employeeList;
    private CustomerListImpl customerList;
    private InsuranceListImpl insuranceList;
    private ContractListImpl contractList;

    public UWViewLogic(EmployeeListImpl employeeList, CustomerListImpl customerList, InsuranceListImpl insuranceList, ContractListImpl contractList) {
        this.sc = new Scanner(System.in);
        this.employeeList = employeeList;
        this.customerList = customerList;
        this.insuranceList = insuranceList;
        this.contractList = contractList;

        Employee employee = new Employee();
        employee.setId(1)
                .setName("윤여찬")
                .setDepartment(Department.UW)
                .setPhone("010-1111-2222")
                .setPosition(Position.DEPTMANAGER);

        Customer customer = new Customer();
        customer.setId(1);
        customer.setAddress("서울시");
        customer.setEmail("test@gmail.com");
        customer.setJob("학생");
        customer.setName("홍길동");
        customer.setPhone("01044445555");
        customer.setSsn("992837-1010223");

        Insurance insurance = new Insurance();
        insurance.setId(1);
        insurance.setName("보험이름");
        insurance.setDescription("보험설명");
        insurance.setInsuranceType(InsuranceType.HEALTH);

        HealthInfo healthInfo = new HealthInfo();
        CarInfo carInfo = new CarInfo();
        BuildingInfo buildingInfo = new BuildingInfo();

        Contract testContract =  new Contract();
        testContract.setId(1);
        testContract.setCustomerId(1);
        testContract.setInsuranceId(1);
        testContract.setHealthInfo(healthInfo);
        testContract.setConditionOfUw(ConditionOfUw.WAIT);

        Contract testContract1 =  new Contract();
        testContract1.setId(2);
        testContract1.setCustomerId(1);
        testContract1.setInsuranceId(1);
        testContract1.setCarInfo(carInfo);
        testContract1.setConditionOfUw(ConditionOfUw.WAIT);

        Contract testContract2 =  new Contract();
        testContract2.setId(3);
        testContract2.setCustomerId(1);
        testContract2.setInsuranceId(1);
        testContract2.setBuildingInfo(buildingInfo);
        testContract2.setConditionOfUw(ConditionOfUw.WAIT);

        this.employeeList.create(employee);
        this.customerList.create(customer);
        this.insuranceList.create(insurance);
        this.contractList.create(testContract);
        this.contractList.create(testContract1);
        this.contractList.create(testContract2);
    }

    @Override
    public void showMenu() {

        createMenu("<<언더라이팅팀메뉴>>", "인수심사한다");
    }

    @Override
    public void work(String command) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                switch (command) {
                    case "1" -> isExit = selectInsuranceType();
                    case "0" -> isExit = true;
                    default -> throw new InputException.InvalidMenuException();
                }

            } catch (InputException.InvalidMenuException e) {
                System.out.println("잘못된 명령을 입력했습니다. 다시 입력해주세요.");
                command = sc.next();
            }
        }

    }

    public void createMenuAndExit(String menuName, String ... elements) {
        createMenu(menuName, elements);
        System.out.println("0 : 취소하기");
        System.out.println("exit : 시스템 종료");
    }

    public void createMenuOnlyExit(String menuName, String ... elements) {
        createMenu(menuName, elements);
        System.out.println("exit : 시스템 종료");
    }

    public boolean selectInsuranceType() {
        boolean isExit = false;

        while (isExit != true) {

            try {
                createMenuAndExit("<<보험 종류 선택>>","건강보험", "자동차보험", "화재보험");

                InsuranceType insuranceType = null;

                switch (sc.next()) {
                    case "1"-> { insuranceType = InsuranceType.HEALTH; readContract(insuranceType); }
                    case "2"-> { insuranceType = InsuranceType.CAR; readContract(insuranceType); }
                    case "3"-> { insuranceType = InsuranceType.FIRE; readContract(insuranceType); }
                    case "0" -> isExit = true;
                    case "exit" -> throw new MyCloseSequence();
                    default -> throw new InputException.InvalidMenuException();
                }
            } catch (InputException.InvalidMenuException e) {
                System.out.println("잘못된 명령을 입력했습니다. 다시 입력해주세요.");
            }
        }
        return true;
    }

    public void readContract(InsuranceType insuranceType) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                createMenu("계약 ID | 고객 이름 | 인수심사상태");
                printContractList(this.employeeList.read(1).readContract(insuranceType));

                createMenuAndExit("<<인수심사할 계약 ID를 입력하세요.>>");
                String contractId = sc.next();

                if (contractId.equals("0")) break;
                if (contractId.equals("exit")) throw new MyCloseSequence();

                createMenu("<<계약 정보(계약 ID: " + contractId + ")>>");
                Contract contract = printContractInfo(Integer.parseInt(contractId));

                selectUwState(contract);

            } catch (NumberFormatException e) {
                System.out.println("잘못된 명령을 입력했습니다. 다시 입력해주세요.");
            } catch (MyIllegalArgumentException e) {
                System.out.println("계약 정보가 존재하지 않습니다.");
            }
        }
    }


    public boolean selectUwState(Contract contract) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                createMenuOnlyExit("<<인수심사결과 선택>>","승인", "거절", "재심사", "계약 목록 조회");
                String command = sc.next();

                switch (command) {

                    case "1": case "2": case "3":
                        createMenu("<<인수사유를 입력해주세요.>>");
                        String reasonOfUw = sc.next();
                        ConditionOfUw conditionOfUw = null;

                        switch (command) {
                            case "1"-> conditionOfUw = ConditionOfUw.APPROVAL;
                            case "2"-> conditionOfUw = ConditionOfUw.REFUSE;
                            case "3"-> conditionOfUw = ConditionOfUw.RE_AUDIT;
                            default -> new InputException.InvalidMenuException();
                        }
                        isExit = confirmUnderWriting(contract.getId(), reasonOfUw, conditionOfUw);
                        break;

                    case "4":
                        return false;
                    case "exit":
                        throw new MyCloseSequence();
                    default:
                        throw new InputException.InvalidMenuException();
                }
            } catch (InputException.InvalidMenuException e) {
                System.out.println("잘못된 명령을 입력했습니다. 다시 입력해주세요.");
            } catch (MyIllegalArgumentException e) {
                System.out.println("계약 정보가 존재하지 않습니다.");
            }
        }
        return true;

    }


    public boolean confirmUnderWriting(int contractId, String reasonOfUw, ConditionOfUw conditionOfUw) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                createMenuOnlyExit("<<인수심사 결과를 반영하시겠습니까?>>", "예", "아니오");

                switch (sc.next()) {
                    case "1":
                        this.employeeList.read(1).underwriting(contractId, reasonOfUw, conditionOfUw);

                        createMenu("인수심사 결과가 반영되었습니다.");
                        System.out.println(ContractListImpl.getContractList().get(contractId));
                        isExit = true;
                        break;
                    case "2":
                        return false;
                    case "exit":
                        throw new MyCloseSequence();
                    default:
                        throw new InputException.InvalidMenuException();
                }
            } catch (InputException.InvalidMenuException e) {
                System.out.println("잘못된 명령을 입력했습니다. 다시 입력해주세요.");
            }
        }
        return true;

    }



    public void printContractList(Map<Integer, Contract> contractList) {

        for (Contract contract : contractList.values()) {
            System.out.println(contract.print());
        }
    }

    public Contract printContractInfo(int customerId) {
        Contract contract = this.contractList.read(customerId);
        System.out.println(contract.toString());

        Customer customer = this.customerList.read(contract.getCustomerId());
        System.out.println(customer.toString());

        Insurance insurance = this.insuranceList.read(contract.getInsuranceId());
        System.out.println(insurance.print());

        return contract;

    }
}
