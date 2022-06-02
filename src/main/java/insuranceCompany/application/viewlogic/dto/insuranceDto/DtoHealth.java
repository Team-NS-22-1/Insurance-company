package insuranceCompany.application.viewlogic.dto.insuranceDto;

public class DtoHealth extends DtoTypeInfo {

    private int targetAge;

    // male: true | female: false
    private boolean targetSex;

    // riskCount > 3: true | riskCount <= 3: false
    private boolean riskCriterion;

    public DtoHealth(int targetAge, boolean targetSex, boolean riskCriterion) {
        this.targetAge = targetAge;
        this.targetSex = targetSex;
        this.riskCriterion = riskCriterion;
    }

    public int getTargetAge() {
        return targetAge;
    }

    public boolean isTargetSex() {
        return targetSex;
    }

    public boolean getRiskCriterion() {
        return riskCriterion;
    }
}
