package application.viewlogic;

import domain.contract.*;
import domain.customer.Customer;
import domain.customer.CustomerListImpl;
import domain.employee.Department;
import domain.employee.Employee;
import domain.employee.EmployeeListImpl;
import domain.employee.Position;
import domain.insurance.Insurance;
import domain.insurance.InsuranceListImpl;
import domain.insurance.InsuranceType;
import application.ViewLogic;
import exception.InputException;
import exception.MyCloseSequence;
import exception.MyIllegalArgumentException;

import java.util.Map;
import java.util.Scanner;

import static utility.MessageUtil.createMenu;

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
                createMenu("-------------------------------");
                createMenu("계약 ID | 고객 이름 | 인수심사상태");
                Map<Integer, Contract> contractList = this.employeeList.read(1).readContract(insuranceType);
                printContractList(contractList);
                createMenu("-------------------------------");

                createMenuAndExit("<<인수심사할 계약 ID를 입력하세요.>>");
                String contractId = sc.next();

                if (contractId.equals("0")) break;
                if (contractId.equals("exit")) throw new MyCloseSequence();

                createMenu("<<계약 정보(계약 ID: " + contractId + ")>>");
                if (!contractList.containsKey(Integer.parseInt(contractId))) throw new MyIllegalArgumentException();

                Contract contract = printContractInfo(contractList.get(Integer.parseInt(contractId)));

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
                createMenuOnlyExit("<<인수심사결과 선택>>","승인", "거절", "보류", "계약 목록 조회");
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
            Customer customer = this.customerList.read(contract.getCustomerId());
            System.out.println(contract.getId() + "        " + customer.getName() + "        " + contract.getConditionOfUw());
        }
    }

    public Contract printContractInfo(Contract contract) {
        System.out.println(contract.toString());

        Customer customer = this.customerList.read(contract.getCustomerId());
        System.out.println(customer.toString());

        Insurance insurance = this.insuranceList.read(contract.getInsuranceId());
        System.out.println(insurance.print());

        return contract;

    }
}
