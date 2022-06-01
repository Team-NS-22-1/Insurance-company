package insuranceCompany.application.domain.insurance;

public class InsuranceDetail {

    private int id;
    private int premium;
    private int insuranceId;

    public int getId() {
        return id;
    }

    public InsuranceDetail setId(int id) {
        this.id = id;
        return this;
    }

    public int getPremium() {
        return premium;
    }

    public InsuranceDetail setPremium(int premium) {
        this.premium = premium;
        return this;
    }

    public int getInsuranceId() {
        return insuranceId;
    }

    public InsuranceDetail setInsuranceId(int insuranceId) {
        this.insuranceId = insuranceId;
        return this;
    }

    public String print() {
        return null;
    }
}
