package main.domain.insurance.inputDto;

public class DtoHealth extends DtoTypeInfo {

    private int targetAge;

    // male: true | female: false
    private boolean targetSex;

    private int riskCriterion;

    public DtoHealth(int targetAge, boolean targetSex, int riskCriterion) {
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

    public int getRiskCriterion() {
        return riskCriterion;
    }
}
