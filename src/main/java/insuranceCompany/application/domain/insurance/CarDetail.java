package insuranceCompany.application.domain.insurance;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:57
 */
public class CarDetail extends InsuranceDetail {

	private int targetAge;
	private long valueCriterion;

	public CarDetail(){
	}

	public int getTargetAge() {
		return targetAge;
	}

	public CarDetail setTargetAge(int targetAge) {
		this.targetAge = targetAge;
		return this;
	}

	public long getValueCriterion() {
		return valueCriterion;
	}

	public CarDetail setValueCriterion(long valueCriterion) {
		this.valueCriterion = valueCriterion;
		return this;
	}

	public String print() {
		return "자동차보험 정보 {" +
				"자동차보험정보 ID:" + getId() +
				", 보험료: " + getPremium() +
				", 대상나이: " + targetAge +
				", 차량가액 기준: " + valueCriterion +
				", 보험ID: " + getInsuranceId() +
				"}";
	}
}