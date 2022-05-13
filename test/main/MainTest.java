package main;

import main.domain.contract.Contract;
import main.domain.contract.ContractListImpl;
import main.domain.contract.HealthInfo;
import main.domain.customer.Customer;
import main.domain.customer.CustomerListImpl;
import main.domain.insurance.Insurance;
import main.domain.insurance.InsuranceListImpl;
import main.domain.insurance.InsuranceType;
import main.domain.viewUtils.Application;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void main() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setAddress("주소");
        customer.setEmail("이메일");
        customer.setJob("직업");

        Insurance insurance = new Insurance();
        insurance.setId(1);
        insurance.setName("보험이름");
        insurance.setDescription("보험설명");
        insurance.setInsuranceType(InsuranceType.HEALTH);

        HealthInfo healthInfo = new HealthInfo();
        healthInfo.setDrinking(true);

        Contract testContract =  new Contract();
        testContract.setId(1);
        testContract.setCustomerId(1);
        testContract.setInsuranceId(1);
        testContract.setHealthInfo(healthInfo);

        CustomerListImpl customerList = new CustomerListImpl();
        customerList.create(customer);

        InsuranceListImpl insuranceList = new InsuranceListImpl();
        insuranceList.create(insurance);

        ContractListImpl contractList = new ContractListImpl();
        contractList.create(testContract);

        Application app = new Application();
        app.run();
    }
}