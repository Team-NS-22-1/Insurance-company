package main;

import main.domain.contract.*;
import main.domain.customer.Customer;
import main.domain.customer.CustomerListImpl;
import main.domain.employee.Department;
import main.domain.employee.Employee;
import main.domain.employee.EmployeeListImpl;
import main.domain.employee.Position;
import main.domain.insurance.*;
import main.domain.payment.*;

import java.time.LocalDate;

/**
 * packageName :  main
 * fileName : TestData
 * author :  규현
 * date : 2022-05-14
 * description : 테스트용 데이터 추가
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-14                규현             최초 생성
 */
public class TestData {
    private CustomerListImpl customerList;
    private PaymentList paymentList;
    private InsuranceListImpl insuranceList;
    private ContractListImpl contractList;
    private EmployeeListImpl employeeList;

    public TestData() {
        this.customerList = new CustomerListImpl();
        this.paymentList = new PaymentListImpl();
        this.insuranceList = new InsuranceListImpl();
        this.contractList = new ContractListImpl();
        this.employeeList = new EmployeeListImpl();

        createEmployee();
        createContract();
        createCustomerData();
        createInsurance();
        createPayment();

    }
    public void createEmployee() {
        this.employeeList.create(new Employee()
                .setName("테스터 직원1")
                .setPhone("010-1234-1234")
                .setDepartment(Department.DEV)
                .setPosition(Position.TEAMLEADER));
        this.employeeList.create(new Employee()
                .setName("테스터 직원2")
                .setPhone("010-2345-2345")
                .setDepartment(Department.SALES)
                .setPosition(Position.MEMBER));
        this.employeeList.create(new Employee()
                .setName("테스터 직원3")
                .setPhone("010-3456-3456")
                .setDepartment(Department.COMP)
                .setPosition(Position.MEMBER));
        this.employeeList.create(new Employee()
                .setName("테스터 직원4")
                .setPhone("010-4567-4567")
                .setDepartment(Department.UW)
                .setPosition(Position.DEPTMANAGER));
        this.employeeList.create(new Employee()
                .setName("테스터 직원5")
                .setPhone("010-5678-5678")
                .setDepartment(Department.EXEC)
                .setPosition(Position.CEO));
        this.employeeList.create(new Employee()
                .setName("테스터 직원6")
                .setPhone("010-5678-5678")
                .setDepartment(Department.DEV)
                .setPosition(Position.MEMBER));
    }

    private void createCustomerData() {
        this.customerList.create(new Customer()
                .setName("테스터 고객1")
                .setSsn("123456-1234567")
                .setPhone("010-1234-1234")
                .setEmail("test@naver.com")
                .setJob("테스터"));
        this.customerList.create(new Customer()
                .setName("테스터 고객2")
                .setSsn("123456-1234567")
                .setPhone("010-1234-1234")
                .setEmail("test@naver.com")
                .setJob("테스터"));
        this.customerList.create(new Customer()
                .setName("테스터 고객3")
                .setSsn("123456-1234567")
                .setPhone("010-1234-1234")
                .setEmail("test@naver.com")
                .setJob("테스터"));
        this.customerList.create(new Customer()
                .setName("테스터 고객4")
                .setSsn("123456-1234567")
                .setPhone("010-1234-1234")
                .setEmail("test@naver.com")
                .setJob("테스터"));

    }
    private void createInsurance() {
        HealthInsurance h1 = new HealthInsurance();
        h1.setName("테스트 건강보험1");
        h1.setDescription("테스트 건강보험1의 설명입니다.");
        h1.setInsuranceType(InsuranceType.HEALTH);
        h1.setPremium(100000);
        h1.setSalesAuthFile(new SalesAuthFile());
        h1.setDevInfo(new DevInfo()
                .setEmployeeId(1)
                .setDevDate(LocalDate.now())
                .setSalesAuthState(SalesAuthState.PERMISSION)
                .setSalesStartDate(LocalDate.now()));
        insuranceList.create(h1);
        HealthInsurance h2 = new HealthInsurance();
        h2.setName("테스트 건강보험2");
        h2.setDescription("테스트 건강보험2의 설명입니다.");
        h2.setInsuranceType(InsuranceType.HEALTH);
        h2.setPremium(100000);
        h2.setSalesAuthFile(new SalesAuthFile());
        h2.setDevInfo(new DevInfo()
                .setEmployeeId(1)
                .setDevDate(LocalDate.now())
                .setSalesAuthState(SalesAuthState.PERMISSION)
                .setSalesStartDate(LocalDate.now()));
        insuranceList.create(h2);

        insuranceList.create(new CarInsurance().setName("테스트 자동차보험1")
                .setInsuranceType(InsuranceType.CAR)
                .setDescription("테스트 자동차보험1의 설명입니다.")
                .setPremium(20000000)
                .setSalesAuthFile(new SalesAuthFile())
                .setDevInfo(new DevInfo().setEmployeeId(1)
                        .setDevDate(LocalDate.now())
                        .setSalesAuthState(SalesAuthState.WAIT)));
        insuranceList.create(new CarInsurance().setName("테스트 자동차보험2")
                .setInsuranceType(InsuranceType.CAR)
                .setDescription("테스트 자동차보험2의 설명입니다.")
                .setPremium(20000000)
                .setSalesAuthFile(new SalesAuthFile())
                .setDevInfo(new DevInfo().setEmployeeId(1)
                        .setDevDate(LocalDate.now())
                        .setSalesAuthState(SalesAuthState.DISALLOWANCE)));

        insuranceList.create(new FireInsurance().setName("테스트 화재보험1")
                .setInsuranceType(InsuranceType.FIRE)
                .setDescription("테스트 화재보험1의 설명입니다.")
                .setPremium(10000000)
                .setSalesAuthFile(new SalesAuthFile())
                .setDevInfo(new DevInfo().setEmployeeId(6)
                        .setDevDate(LocalDate.now())
                        .setSalesAuthState(SalesAuthState.PERMISSION)
                        .setSalesStartDate(LocalDate.now())));
        insuranceList.create(new FireInsurance().setName("테스트 화재보험2")
                .setInsuranceType(InsuranceType.FIRE)
                .setDescription("테스트 화재보험2의 설명입니다.")
                .setPremium(20000000)
                .setSalesAuthFile(new SalesAuthFile())
                .setDevInfo(new DevInfo().setEmployeeId(6)
                        .setDevDate(LocalDate.now())
                        .setSalesAuthState(SalesAuthState.WAIT)));
    }

    private void createPayment() {
        Card card = new Card();
        card.setCustomerId(1);
        card.setExpiryDate(LocalDate.now());
        card.setCardNo("123123");
        card.setCvcNo("231");
        card.setCardType(CardType.BC);
        card.setPaytype(PayType.CARD);
        paymentList.create(card);

        paymentList.create(new Account().setAccountNo("1111-1111-1111-1111")
                .setBankType(BankType.HANA)
                .setPaytype(PayType.ACCOUNT)
                .setCustomerId(2));
    }

    private void createContract() {
        HealthInfo healthInfo = new HealthInfo();
        CarInfo carInfo = new CarInfo();
        BuildingInfo buildingInfo = new BuildingInfo();

        Contract testContract =  new Contract();
        testContract.setCustomerId(1);
        testContract.setInsuranceId(1);
        testContract.setHealthInfo(healthInfo);
        testContract.setConditionOfUw(ConditionOfUw.WAIT);

        Contract testContract1 =  new Contract();
        testContract1.setCustomerId(2);
        testContract1.setInsuranceId(3);
        testContract1.setCarInfo(carInfo);
        testContract1.setConditionOfUw(ConditionOfUw.WAIT);

        Contract testContract2 =  new Contract();
        testContract2.setCustomerId(3);
        testContract2.setInsuranceId(6);
        testContract2.setBuildingInfo(buildingInfo);
        testContract2.setConditionOfUw(ConditionOfUw.WAIT);

        this.contractList.create(testContract);
        this.contractList.create(testContract1);
        this.contractList.create(testContract2);
    }
}
