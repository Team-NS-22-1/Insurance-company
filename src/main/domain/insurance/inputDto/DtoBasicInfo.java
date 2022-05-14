package main.domain.insurance.inputDto;

public class DtoBasicInfo {

    private String name;
    private String description;
    private int paymentPeriod;
    private int contractPeriod;

    public DtoBasicInfo(String name, String description, int paymentPeriod, int contractPeriod) {
        this.name = name;
        this.description = description;
        this.paymentPeriod = paymentPeriod;
        this.contractPeriod = contractPeriod;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPaymentPeriod() {
        return paymentPeriod;
    }

    public int getContractPeriod() {
        return contractPeriod;
    }
}
