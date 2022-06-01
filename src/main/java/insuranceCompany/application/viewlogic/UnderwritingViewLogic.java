package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.contract.ContractDao;
import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.dao.employee.EmployeeDao;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.contract.ConditionOfUw;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.InsuranceType;
import insuranceCompany.application.global.exception.InputException;
import insuranceCompany.application.global.exception.InvalidMenuException;
import insuranceCompany.application.global.exception.MyCloseSequence;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.global.utility.MessageUtil;
import insuranceCompany.application.global.utility.MyBufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import static insuranceCompany.application.global.utility.MessageUtil.createMenu;

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
        EmployeeDao employeeDao = new EmployeeDao();
        this.employee = employeeDao.read(2);
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
                    default -> throw new InvalidMenuException();
                }

            } catch (InvalidMenuException e) {
                System.out.println("잘못된 명령을 입력했습니다. 다시 입력해주세요.");
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
                    case "1"-> { insuranceType = InsuranceType.HEALTH; readContract(insuranceType); }
                    case "2"-> { insuranceType = InsuranceType.CAR; readContract(insuranceType); }
                    case "3"-> { insuranceType = InsuranceType.FIRE; readContract(insuranceType); }
                    case "0" -> isExit = true;
                    case "exit" -> throw new MyCloseSequence();
                    default -> throw new InvalidMenuException();
                }
            } catch (InvalidMenuException e) {
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

                // read
                List<Contract> contractList = this.employee.readContract(insuranceType);
                printContractList(contractList);
                createMenu("-------------------------------");

                MessageUtil.createMenuAndExit("<<인수심사할 계약 ID를 입력하세요.>>");
                String contractId = sc.next();

                if (contractId.equals("0")) break;
                if (contractId.equals("exit")) throw new MyCloseSequence();

                createMenu("<<계약 정보(계약 ID: " + contractId + ")>>");

                ContractDao contractDao = new ContractDao();
                InsuranceDaoImpl insuranceDao = new InsuranceDaoImpl();
                Contract contract = contractDao.read(Integer.parseInt(contractId));
                Insurance insurance = insuranceDao.read(contract.getInsuranceId());

                if (contract.getId() == 0) throw new MyIllegalArgumentException();
                if (!insurance.getInsuranceType().equals(insuranceType)) throw new MyIllegalArgumentException();

                // read
                printContractInfo(contract);
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
                            default -> new InvalidMenuException();
                        }
                        isExit = confirmUnderWriting(contract.getId(), reasonOfUw, conditionOfUw);
                        break;

                    case "4":
                        return false;
                    case "exit":
                        throw new MyCloseSequence();
                    default:
                        throw new InvalidMenuException();
                }
            } catch (InvalidMenuException e) {
                System.out.println("잘못된 명령을 입력했습니다. 다시 입력해주세요.");
            } catch (MyIllegalArgumentException e) {
                System.out.println("계약 정보가 존재하지 않습니다.");
            } catch (IOException e) {
                System.out.println("인수사유가 잘못되었습니다.");
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
                        ContractDao contractDao = new ContractDao();
                        System.out.println(contractDao.read(contractId));
                        isExit = true;
                        break;
                    case "2":
                        return false;
                    case "exit":
                        throw new MyCloseSequence();
                    default:
                        throw new InvalidMenuException();
                }
            } catch (InvalidMenuException e) {
                System.out.println("잘못된 명령을 입력했습니다. 다시 입력해주세요.");
            }
        }
        return true;

    }

    public void printContractList(List<Contract> contractList) {

        for (Contract contract : contractList) {
            CustomerDaoImpl customerDao = new CustomerDaoImpl();
            Customer customer = customerDao.read(contract.getCustomerId());
            System.out.println(contract.getId() + "        " + customer.getName() + "     " + contract.getConditionOfUw().getName());
        }
    }

    public Contract printContractInfo(Contract contract) {
        System.out.println(contract.toString());

        CustomerDaoImpl customerDao = new CustomerDaoImpl();
        Customer customer = customerDao.read(contract.getCustomerId());
        System.out.println(customer.toString());

        InsuranceDaoImpl insuranceDao = new InsuranceDaoImpl();
        Insurance insurance = insuranceDao.read(contract.getInsuranceId());
        System.out.println(insurance.print());

        //InsuranceDetailDao insuranceDetailDao = new InsuranceDetailDao();
        //InsuranceDetail insuranceDetail = insuranceDetailDao.read(contract.getInsuranceId());
        //System.out.println(insuranceDetail.print());
        return contract;

    }
}
