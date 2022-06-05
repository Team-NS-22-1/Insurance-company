package insuranceCompany.application.domain.insurance;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:00
 */
public class HealthDetail extends InsuranceDetail {

	private int targetAge;
	private boolean targetSex;
	private boolean riskCriterion;

	public HealthDetail(){
	}

	public boolean isRiskCriterion() {
		return riskCriterion;
	}

	public HealthDetail setRiskCriterion(boolean riskCriterion) {
		this.riskCriterion = riskCriterion;
		return this;
	}

	public int getTargetAge() {
		return targetAge;
	}

	public HealthDetail setTargetAge(int targetAge) {
		this.targetAge = targetAge;
		return this;
	}

	public boolean isTargetSex() {
		return targetSex;
	}

	public HealthDetail setTargetSex(boolean targetSex) {
		this.targetSex = targetSex;
		return this;
	}

	public String getTargetSex() {
		return isTargetSex() ? "남성" : "여성";
	}

	public String printTargetAge() {
		 String ageRange = switch (targetAge) {
			case 100 -> "100세 이상";
			case 80 -> "80 ~ 100세";
			case 60 -> "60 ~ 80세";
			case 50 -> "50 ~ 60세";
			case 40 -> "40 ~ 50세";
			case 30 -> "30 ~ 40세";
			case 20 -> "20 ~ 30세";
			default -> "20세 이하";
		 };
		 return ageRange;
	}


	public String getRiskCriterion() {
		return isRiskCriterion() ? "해당" : "해당없음";
	}

	public String print() {
		return "건강보험 정보 {" +
				"건강보험정보 ID:" + getId() +
				", 보험료: " + getPremium() +
				", 대상나이: " + targetAge +
				", 대상성별: " + getTargetSex() +
				", 위험부담 기준: " + riskCriterion +
				"}";
	}
}