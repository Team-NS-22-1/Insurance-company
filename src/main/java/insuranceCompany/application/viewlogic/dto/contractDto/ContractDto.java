package insuranceCompany.application.viewlogic.dto.contractDto;

public class ContractDto {
    private int employeeId;
    private int premium;

    public int getEmployeeId() {
        return employeeId;
    }

    public ContractDto setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public int getPremium() {
        return premium;
    }

    public ContractDto setPremium(int premium) {
        this.premium = premium;
        return this;
    }
}
