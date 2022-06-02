package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.contract.ContractDaoImpl;
import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.dao.employee.EmployeeDaoImpl;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.contract.ConditionOfUw;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.InsuranceType;
import insuranceCompany.application.global.exception.*;
import insuranceCompany.application.global.utility.MessageUtil;
import insuranceCompany.application.global.utility.MyBufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import static insuranceCompany.application.global.utility.MessageUtil.*;

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
public class UnderwritingViewLogic implements ViewLogic {

    private Scanner sc;
    private MyBufferedReader br;
    private Employee employee;

    public UnderwritingViewLogic() {
        this.sc = new Scanner(System.in);
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();
        this.employee = employeeDaoImpl.read(2);
    }

    public UnderwritingViewLogic(Employee employee) {
        this.sc = new Scanner(System.in);
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        this.employee = employee;
    }

    @Override
    public void showMenu() {

        createMenuAndLogout("<<언더라이팅팀메뉴>>", "인수심사한다");
    }

    @Override
    public void work(String command) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                switch (command) {
                    case "1" -> isExit = selectInsuranceType();
                    case "0" -> isExit = true;
                    default -> throw new InputInvalidMenuException();
                }

            } catch (InputInvalidMenuException e) {
                System.out.println(e.getMessage());
                command = sc.next();
            }
        }

    }

    public boolean selectInsuranceType() {
        boolean isExit = false;

        while (isExit != true) {

            try {
                MessageUtil.createMenuAndExit("<<보험 종류 선택>>","건강보험", "자동차보험", "화재보험");

                InsuranceType insuranceType = null;

                switch (sc.next()) {
                    case "1"-> { insuranceType = InsuranceType.HEALTH; readContracts(insuranceType); }
                    case "2"-> { insuranceType = InsuranceType.CAR; readContracts(insuranceType); }
                    case "3"-> { insuranceType = InsuranceType.FIRE; readContracts(insuranceType); }
                    case "0" -> isExit = true;
                    case "exit" -> throw new MyCloseSequence();
                    default -> throw new InputInvalidMenuException();
                }
            } catch (InputInvalidMenuException e) {
                System.out.println(e.getMessage());
            }
        }
        return true;
    }

    public void readContracts(InsuranceType insuranceType) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                // read
                ContractDaoImpl contractDaoImpl = new ContractDaoImpl();
                List<Contract> contractList = contractDaoImpl.readAllByInsuranceType(insuranceType);

                if (contractList.size() == 0) {
                    isExit = true;
                    throw new MyNotExistContractException();
                }

                createMenu("-------------------------------");
                createMenu("계약 ID | 고객 이름 | 인수심사상태");
                printContractList(contractList);
                createMenu("-------------------------------");

                MessageUtil.createMenuAndExit("<<인수심사할 계약 ID를 입력하세요.>>");
                String contractId = sc.next();
                if (contractId.equals("0")) break;
                if (contractId.equals("exit")) throw new MyCloseSequence();

                // read
                Contract contract = this.employee.readContract(Integer.parseInt(contractId), insuranceType);
                createMenu("<<계약 정보>>");
                printContractInfo(contract);

                selectUwState(contract);

            } catch (MyNotExistContractException | MyIllegalArgumentException | InputInvalidDataException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println(new InputInvalidDataException().getMessage());
            }
        }
    }


    public boolean selectUwState(Contract contract) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                MessageUtil.createMenuOnlyExit("<<인수심사결과 선택>>","승인", "거절", "보류", "계약 목록 조회");
                String command = sc.next();

                switch (command) {

                    case "1": case "2": case "3":
                        createMenu("<<인수사유를 입력해주세요.>>");
                        String reasonOfUw = "";
                        reasonOfUw = (String) br.verifyRead("인수사유: ", reasonOfUw);
                        ConditionOfUw conditionOfUw = null;

                        switch (command) {
                            case "1"-> conditionOfUw = ConditionOfUw.APPROVAL;
                            case "2"-> conditionOfUw = ConditionOfUw.REFUSE;
                            case "3"-> conditionOfUw = ConditionOfUw.RE_AUDIT;
                            default -> new InputInvalidMenuException();
                        }
                        isExit = confirmUnderWriting(contract.getId(), reasonOfUw, conditionOfUw);
                        break;

                    case "4":
                        return false;
                    case "exit":
                        throw new MyCloseSequence();
                    default:
                        throw new InputInvalidMenuException();
                }
            } catch (InputInvalidMenuException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return true;

    }


    public boolean confirmUnderWriting(int contractId, String reasonOfUw, ConditionOfUw conditionOfUw) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                MessageUtil.createMenuOnlyExit("<<인수심사 결과를 반영하시겠습니까?>>", "예", "아니오");

                switch (sc.next()) {
                    case "1":
                        // update
                        this.employee.underwriting(contractId, reasonOfUw, conditionOfUw);

                        createMenu("인수심사 결과가 반영되었습니다.");
                        ContractDaoImpl contractDaoImpl = new ContractDaoImpl();
                        System.out.println(contractDaoImpl.read(contractId));
                        isExit = true;
                        break;
                    case "2":
                        return false;
                    case "exit":
                        throw new MyCloseSequence();
                    default:
                        throw new InputInvalidMenuException();
                }
            } catch (InputInvalidMenuException e) {
                System.out.println(e.getMessage());
            }
        }
        return true;

    }

    public void printContractList(List<Contract> contractList) {

        for (Contract contract : contractList) {
            CustomerDaoImpl customerDaoImpl = new CustomerDaoImpl();
            Customer customer = customerDaoImpl.read(contract.getCustomerId());
            System.out.println(contract.getId() + "        " + customer.getName() + "          " + contract.getConditionOfUw().getName());
        }
    }

    public Contract printContractInfo(Contract contract) {
        System.out.println(contract.toString());

        CustomerDaoImpl customerDaoImpl = new CustomerDaoImpl();
        Customer customer = customerDaoImpl.read(contract.getCustomerId());
        System.out.println(customer.toString());

        InsuranceDaoImpl insuranceDaoImpl = new InsuranceDaoImpl();
        Insurance insurance = insuranceDaoImpl.read(contract.getInsuranceId());
        System.out.println(insurance.print());

        return contract;

    }
}
