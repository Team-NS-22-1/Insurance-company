package insuranceCompany.application.viewlogic;

import insuranceCompany.application.dao.contract.ContractDaoImpl;
import insuranceCompany.application.dao.customer.CustomerDaoImpl;
import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.contract.ConditionOfUw;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.InsuranceType;
import insuranceCompany.application.global.exception.*;
import insuranceCompany.application.global.utility.MyBufferedReader;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import static insuranceCompany.application.global.constant.CommonConstants.*;
import static insuranceCompany.application.global.constant.UnderwritingViewLogicConstants.*;
import static insuranceCompany.application.global.utility.MenuUtil.*;

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

    public UnderwritingViewLogic(Employee employee) {
        this.sc = new Scanner(System.in);
        this.br = new MyBufferedReader(new InputStreamReader(System.in));
        this.employee = employee;
    }

    @Override
    public String showMenu() {
        return createMenuAndLogoutAndInput(UNDERWRITING_MENU, UNDERWRITING_MENU_ELEMENTS);
    }

    @Override
    public void work(String command) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                switch (command) {
                    case ONE -> isExit = selectInsuranceType();
                    case ZERO -> isExit = true;
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
                String command = String .valueOf(br.verifyMenu(
                        createMenuAndExitQuery(INSURANCE_TYPE_MENU, INSURANCE_TYPE_MENU_ELEMENTS),
                        INSURANCE_TYPE_MENU_ELEMENTS.length
                ));

                InsuranceType insuranceType = null;

                switch (command) {
                    case ONE -> { insuranceType = InsuranceType.HEALTH; readContracts(insuranceType); }
                    case TWO -> { insuranceType = InsuranceType.CAR; readContracts(insuranceType); }
                    case THREE -> { insuranceType = InsuranceType.FIRE; readContracts(insuranceType); }
                    case ZERO -> isExit = true;
                    case EXIT -> throw new MyCloseSequence();
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
                // 계약 목록 조회 (read)
                ContractDaoImpl contractDaoImpl = new ContractDaoImpl();
                List<Contract> contractList = contractDaoImpl.readAllByInsuranceType(insuranceType);

                if (contractList.size() == 0) {
                    isExit = true;
                    throw new MyNotExistContractException();
                }
                createMenu(LIST_LINE);
                createMenu(READ_CONTRACTS_COLUMN);
                printContractList(contractList);
                createMenu(LIST_LINE);

                // 계약 ID 입력
                createMenuAndExit(INPUT_CONTRACT_ID);
                String contractId = "";
                contractId = (String) br.verifyRead(INPUT, contractId);

                if (contractId.equals(ZERO)) break;
                if (contractId.equals(EXIT)) throw new MyCloseSequence();

                // 계약 정보 조회(read)
                Contract contract = this.employee.readContract(Integer.parseInt(contractId), insuranceType);
                createMenu(SHOW_CONTRACT_INFO);
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
                // 인수심사 결과 선택
                String command = String.valueOf(br.verifyMenu(
                        createMenuOnlyExitQuery(SELECT_UNDERWRITING_STATE, SELECT_UNDERWRITING_STATE_MENU),
                        SELECT_UNDERWRITING_STATE_MENU.length
                ));

                switch (command) {

                    case ONE: case TWO: case THREE:
                        // 인수 사유 입력
                        createMenu(INPUT_UNDERWRITING_REASON_MENU);
                        String reasonOfUw = "";
                        reasonOfUw = (String) br.verifyRead(INPUT_UNDERWRITING_REASON, reasonOfUw);
                        ConditionOfUw conditionOfUw = null;

                        switch (command) {
                            case ONE -> conditionOfUw = ConditionOfUw.APPROVAL;
                            case TWO -> conditionOfUw = ConditionOfUw.REFUSE;
                            case THREE -> conditionOfUw = ConditionOfUw.RE_AUDIT;
                            default -> new InputInvalidMenuException();
                        }
                        isExit = confirmUnderWriting(contract.getId(), reasonOfUw, conditionOfUw);
                        break;

                    case FOUR:
                        return false;
                    case EXIT:
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


    public boolean confirmUnderWriting(int contractId, String reasonOfUw, ConditionOfUw conditionOfUw) {
        boolean isExit = false;

        while (isExit != true) {

            try {
                // 인수 심사 결과 반영 확인
                String command = String.valueOf(br.verifyMenu(
                        createMenuOnlyExitQuery(CONFIRM_UNDERWRITING_MENU, CONFIRM_UNDERWRITING_MENU_ELEMENTS),
                        CONFIRM_UNDERWRITING_MENU_ELEMENTS.length
                ));

                switch (command) {
                    case ONE:
                        // update
                        this.employee.underwriting(contractId, reasonOfUw, conditionOfUw);

                        createMenu(CONFIRM_UNDERWRITING_MESSAGE);
                        ContractDaoImpl contractDaoImpl = new ContractDaoImpl();
                        System.out.println(contractDaoImpl.read(contractId));
                        isExit = true;
                        break;
                    case TWO:
                        return false;
                    case EXIT:
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
