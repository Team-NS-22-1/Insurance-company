import application.Application;
import domain.contract.*;
import domain.customer.Customer;
import domain.customer.CustomerListImpl;
import domain.insurance.Insurance;
import domain.insurance.InsuranceListImpl;
import domain.insurance.InsuranceType;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void UWTest() {
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

        HealthContract healthInfo = new HealthContract();
        CarContract carInfo = new CarContract();
        FireContract buildingInfo = new FireContract();

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

        Application app = new Application();
        app.run();
    }
}