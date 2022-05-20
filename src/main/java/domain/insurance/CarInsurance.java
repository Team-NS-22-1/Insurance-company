package domain.insurance;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:57
 */
public class CarInsurance extends Insurance {

	private int targetAge;
	private int valueCriterion;

	public CarInsurance(){
	}

	public int getTargetAge() {
		return targetAge;
	}

	public CarInsurance setTargetAge(int targetAge) {
		this.targetAge = targetAge;
		return this;
	}

	public int getValueCriterion() {
		return valueCriterion;
	}

	public CarInsurance setValueCriterion(int valueCriterion) {
		this.valueCriterion = valueCriterion;
		return this;
	}
}