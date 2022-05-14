package main.domain.insurance.inputDto;

public class DtoCar extends DtoTypeInfo {

    private int targetAge;

    private long valueCriterion;

    public DtoCar(int targetAge, long valueCriterion) {
        this.targetAge = targetAge;
        this.valueCriterion = valueCriterion;
    }

    public int getTargetAge() {
        return targetAge;
    }

    public long getValueCriterion() {
        return valueCriterion;
    }
}
