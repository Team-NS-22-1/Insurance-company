package main.domain.viewUtils.viewlogic;

import main.domain.contract.*;
import main.domain.customer.Customer;
import main.domain.customer.CustomerList;
import main.domain.customer.CustomerListImpl;
import main.domain.employee.Department;
import main.domain.employee.Employee;
import main.domain.employee.Position;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.InsuranceType;
import main.domain.viewUtils.ViewLogic;

import java.util.Map;
import java.util.Scanner;

import static main.domain.utility.MessageUtil.createMenu;

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
    private Employee employee;
    private String nextCommand;
    private boolean exitCommand;
    private boolean isExitCommand;
    private int contractId;

    @Override
    public void showMenu() {
        createMenu("언더라이팅팀메뉴", "인수심사한다", "돌아간다");

        this.exitCommand = false;
        this.sc = new Scanner(System.in);

        this.employee = new Employee();
        employee.setId(1)
                .setName("윤여찬")
                .setDepartment(Department.UW)
                .setPhone("010-1111-2222")
                .setPosition(Position.DEPTMANAGER);



        Customer customer = new Customer();
        customer.setId(1);
        customer.setAddress("주소");
        customer.setEmail("이메일");
        customer.setJob("직업");
        customer.setName("홍길동");

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

        CustomerListImpl customerList = new CustomerListImpl();
        customerList.create(customer);

        InsuranceListImpl insuranceList = new InsuranceListImpl();
        insuranceList.create(insurance);

        ContractListImpl contractList = new ContractListImpl();
        contractList.create(testContract);
        contractList.create(testContract1);
        contractList.create(testContract2);
    }

    @Override
    public void work(String command) {

        switch (command) {
            // 인수심사한다
            case "1":
                readContractList();
                break;
            // 돌아간다
            case "2":
                break;
            default:
                break;

        }
    }
    public void readContractList() {
        createMenu("보험 선택","건강보험", "자동차보험", "화재보험");

        this.nextCommand = sc.next();
        InsuranceType insuranceType = null;

        switch (nextCommand) {
            case "1":
                insuranceType = InsuranceType.HEALTH;
                break;
            case "2":
                insuranceType = InsuranceType.CAR;
                break;
            case "3":
                insuranceType = InsuranceType.FIRE;
                break;
        }

        while (this.isExitCommand != true) {
            createMenu("계약 ID | 고객 이름 | 인수심사상태");
            printContractList(this.employee.readContract(insuranceType));

            this.exitCommand = false;
            readContractInfo();
        }

    }

    public void readContractInfo() {

        while (this.exitCommand != true) {
            createMenu("인수심사할 계약 ID를 입력하세요.(홈화면으로 돌아간다는 0번)");
            this.contractId = sc.nextInt();

            if (this.contractId == 0) {
                this.isExitCommand = true;
                break;
            }

            updateUwState();
        }
    }


    public void updateUwState() {

        while (this.exitCommand != true) {
            createMenu("계약 정보(계약 ID: " + this.contractId + ")");
            Contract contract = printContractInfo(this.contractId);


            createMenu("명령 선택","승인", "거절", "재심사", "계약 목록 조회");
            this.nextCommand = sc.next();

            switch (nextCommand) {

                case "1": case "2": case "3":
                    createMenu("인수사유를 입력해주세요.");
                    String reasonOfUw = sc.next();
                    ConditionOfUw conditionOfUw = null;

                    switch (nextCommand) {
                        case "1":
                            conditionOfUw = ConditionOfUw.APPROVAL;
                            break;
                        case "2":
                            conditionOfUw = ConditionOfUw.REFUSE;
                            break;
                        case "3":
                            conditionOfUw = ConditionOfUw.RE_AUDIT;
                            break;
                    }
                    confirmUnderWriting(contract.getId(), reasonOfUw, conditionOfUw);
                    break;

                case "4":
                    this.exitCommand = true;
                    break;

            }
        }
    }


    public void confirmUnderWriting(int contractId, String reasonOfUw, ConditionOfUw conditionOfUw) {
        createMenu("인수심사 결과를 반영하시겠습니까?", "예", "아니오");

        switch (sc.next()) {
            case "1":
                employee.underwriting(contractId, reasonOfUw, conditionOfUw);

                createMenu("인수심사 결과가 반영되었습니다.");
                System.out.println(ContractListImpl.getContractList().get(1));
                this.exitCommand = true;
                break;
            case "2":
                break;
        }
    }



    public void printContractList(Map<Integer, Contract> contractList) {

        for (Contract contract : contractList.values()) {
            System.out.println(contract.print());
        }
    }

    public Contract printContractInfo(int customerId) {
        ContractListImpl contractList = new ContractListImpl();
        Contract contract = contractList.read(customerId);
        System.out.println(contract.toString());

        CustomerListImpl customerList = new CustomerListImpl();
        Customer customer = customerList.read(contract.getCustomerId());
        System.out.println(customer.toString());

        InsuranceListImpl insuranceList = new InsuranceListImpl();
        Insurance insurance = insuranceList.read(contract.getInsuranceId());
        // toString으로 수정
        System.out.println(insurance.print());

        return contract;

    }
}
