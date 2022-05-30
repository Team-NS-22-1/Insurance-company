package test;

import domain.accident.AccidentList;
import domain.accident.AccidentListImpl;
import domain.accident.AccidentType;
import domain.accident.InjuryAccident;
import domain.accident.accDocFile.AccDocFile;
import domain.accident.accDocFile.AccDocFileList;
import domain.accident.accDocFile.AccDocFileListImpl;
import domain.accident.accDocFile.AccDocType;
import domain.contract.*;
import domain.customer.Customer;
import domain.customer.CustomerListImpl;
import domain.employee.Department;
import domain.employee.Employee;
import domain.employee.EmployeeListImpl;
import domain.employee.Position;
import domain.insurance.*;
import domain.payment.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName :  main
 * fileName : test.TestData
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
    private InsuranceDetailList insuranceDetailList;
    private ContractListImpl contractList;
    private EmployeeListImpl employeeList;
    private AccidentList accidentList;
    private AccDocFileList accDocFileList;

    public TestData() {
        this.customerList = new CustomerListImpl();
        this.paymentList = new PaymentListImpl();
        this.insuranceList = new InsuranceListImpl();
        this.insuranceDetailList = new InsuranceDetailListImpl();
        this.contractList = new ContractListImpl();
        this.employeeList = new EmployeeListImpl();
        this.accidentList = new AccidentListImpl();
        this.accDocFileList = new AccDocFileListImpl();


        createEmployee();
        createContract();
        createCustomerData();
        createInsurance();
        createPayment();
        createAccident();

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
        insuranceList.create(new Insurance().setName("테스트 건강보험1")
                .setDescription("테스트 건강보험1의 설명입니다.")
                .setContractPeriod(90)
                .setPaymentPeriod(30)
                .setInsuranceType(InsuranceType.HEALTH)
                .setDevInfo(new DevInfo().setEmployeeId(1)
                        .setDevDate(LocalDate.now())
                        .setSalesAuthState(SalesAuthState.PERMISSION)
                        .setSalesStartDate(LocalDate.now()))
                .setSalesAuthFile(new SalesAuthFile()));
        insuranceDetailList.create(new HealthDetail().setTargetAge(20)
                .setTargetSex(true)
                .setRiskCriterion(3)
                .setPremium(100000)
                .setInsuranceId(1));

        insuranceList.create(new Insurance().setName("테스트 자동차보험1")
                .setDescription("테스트 자동차보험1의 설명입니다.")
                .setContractPeriod(1)
                .setPaymentPeriod(1)
                .setInsuranceType(InsuranceType.CAR)
                .setDevInfo(new DevInfo().setEmployeeId(1)
                        .setDevDate(LocalDate.now())
                        .setSalesAuthState(SalesAuthState.PERMISSION))
                .setSalesAuthFile(new SalesAuthFile()));
        insuranceDetailList.create(new CarDetail().setTargetAge(20)
                .setValueCriterion(20000000)
                .setPremium(1000000)
                .setInsuranceId(2));

        insuranceList.create(new Insurance().setName("테스트 화재보험1")
                .setDescription("테스트 화재보험1의 설명입니다.")
                .setContractPeriod(10)
                .setPaymentPeriod(10)
                .setInsuranceType(InsuranceType.FIRE)
                .setDevInfo(new DevInfo().setEmployeeId(6)
                        .setDevDate(LocalDate.now())
                        .setSalesAuthState(SalesAuthState.PERMISSION))
                .setSalesAuthFile(new SalesAuthFile()));
        insuranceDetailList.create(new FireDetail().setTargetBuildingType(BuildingType.COMMERCIAL)
                .setCollateralAmountCriterion(100000000)
                .setPremium(10000000)
                .setInsuranceId(2));

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
        HealthContract healthContract = new HealthContract();
        CarContract carContract = new CarContract();
        FireContract fireContract = new FireContract();

        Contract testContract =  new Contract();
        testContract.setCustomerId(1);
        testContract.setInsuranceId(1);
//        testContract.setHealthInfo(healthContract);
        testContract.setConditionOfUw(ConditionOfUw.WAIT);

        Contract testContract1 =  new Contract();
        testContract1.setCustomerId(2);
        testContract1.setInsuranceId(3);
//        testContract1.setCarInfo(carContract);
        testContract1.setConditionOfUw(ConditionOfUw.WAIT);

        Contract testContract2 =  new Contract();
        testContract2.setCustomerId(3);
        testContract2.setInsuranceId(6);
//        testContract2.setBuildingInfo(buildingInfo);
        testContract2.setConditionOfUw(ConditionOfUw.WAIT);

        this.contractList.create(testContract);
        this.contractList.create(testContract1);
        this.contractList.create(testContract2);
    }

    private void createAccident() {
        InjuryAccident accident = new InjuryAccident();
        accident.setAccidentType(AccidentType.INJURYACCIDENT);
        accident.setEmployeeId(3);
        accident.setCustomerId(1);
        accident.setDateOfAccident(LocalDateTime.now());
        accident.setDateOfReport(LocalDateTime.now());
        accidentList.create(accident);

        AccDocFile accDocFile = new AccDocFile();
        accDocFile.setAccidentId(accident.getId());
        accDocFile.setType(AccDocType.CLAIMCOMP);
        accDocFile.setFileAddress("./AccDocFile/submit/"+accident.getCustomerId()+"/"+accident.getId()
        +"/"+accDocFile.getType().getDesc()+".hwp");

        AccDocFile accDocFile2 = new AccDocFile();
        accDocFile2.setAccidentId(accident.getId());
        accDocFile2.setType(AccDocType.MEDICALCERTIFICATION);
        accDocFile2.setFileAddress("./AccDocFile/submit/"+accident.getCustomerId()+"/"+accident.getId()
                +"/"+accDocFile2.getType().getDesc()+".hwp");

        AccDocFile accDocFile3 = new AccDocFile();
        accDocFile3.setAccidentId(accident.getId());
        accDocFile3.setType(AccDocType.CONFIRMADMISSIONDISCHARGE);
        accDocFile3.setFileAddress("./AccDocFile/submit/"+accident.getCustomerId()+"/"+accident.getId()
                +"/"+ accDocFile3.getType().getDesc()+".hwp");

       accDocFileList.create(accDocFile);
        accDocFileList.create(accDocFile2);
        accDocFileList.create(accDocFile3);

    }
}
