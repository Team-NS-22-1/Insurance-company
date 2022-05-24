package domain.insurance;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:00
 */
public class HealthInsurance extends Insurance {

	private int riskPremiumCriterion;
	private int targetAge;
	private boolean targetSex;

	public HealthInsurance(){
	}

	public int isRiskPremiumCriterion() {
		return riskPremiumCriterion;
	}

	public HealthInsurance setRiskPremiumCriterion(int riskPremiumCriterion) {
		this.riskPremiumCriterion = riskPremiumCriterion;
		return this;
	}

	public int getTargetAge() {
		return targetAge;
	}

	public HealthInsurance setTargetAge(int targetAge) {
		this.targetAge = targetAge;
		return this;
	}

	public boolean isTargetSex() {
		return targetSex;
	}

	public HealthInsurance setTargetSex(boolean targetSex) {
		this.targetSex = targetSex;
		return this;
	}
}