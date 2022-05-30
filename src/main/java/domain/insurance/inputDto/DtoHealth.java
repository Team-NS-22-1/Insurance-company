package domain.insurance.inputDto;

public class DtoHealth extends DtoTypeInfo {

    private int targetAge;

    // male: true | female: false
    private boolean targetSex;

    // > 3: true | <= 3: false
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
