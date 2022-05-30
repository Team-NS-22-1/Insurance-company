package domain.contract;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class HealthContract extends Contract{

	private int height;
	private int weight;
	private boolean isDrinking;
	private boolean isSmoking;
	private boolean isDriving;
	private boolean isDangerActivity;
	private boolean isHavingDisease;
	private boolean isTakingDrug;
	private String diseaseDetail;

	public HealthContract(){

	}

	public HealthContract setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getWeight() {
		return weight;
	}

	public HealthContract setWeight(int weight) {
		this.weight = weight;
		return this;
	}

	public boolean isDrinking() {
		return isDrinking;
	}

	public HealthContract setDrinking(boolean drinking) {
		isDrinking = drinking;
		return this;
	}

	public boolean isSmoking() {
		return isSmoking;
	}

	public HealthContract setSmoking(boolean smoking) {
		isSmoking = smoking;
		return this;
	}

	public boolean isDriving() {
		return isDriving;
	}

	public HealthContract setDriving(boolean driving) {
		isDriving = driving;
		return this;
	}

	public boolean isDangerActivity() {
		return isDangerActivity;
	}

	public HealthContract setDangerActivity(boolean dangerActivity) {
		isDangerActivity = dangerActivity;
		return this;
	}

	public boolean isHavingDisease() {
		return isHavingDisease;
	}

	public HealthContract setHavingDisease(boolean havingDisease) {
		isHavingDisease = havingDisease;
		return this;
	}

	public boolean isTakingDrug() {
		return isTakingDrug;
	}

	public HealthContract setTakingDrug(boolean takingDrug) {
		isTakingDrug = takingDrug;
		return this;
	}

	public String getDiseaseDetail() {
		return diseaseDetail;
	}

	public HealthContract setDiseaseDetail(String diseaseDetail) {
		this.diseaseDetail = diseaseDetail;
		return this;
	}

	@Override
	public String toString() {
		return super.toString() +
				" 건강정보: {" +
				"키: " + height +
				", 위험활동여부: " + isDangerActivity +
				", 음주여부: " + isDrinking +
				", 운전여부: " + isDriving +
				", 질병여부: " + isHavingDisease +
				", 흡연여부: " + isSmoking +
				", 약물복용여부: " + isTakingDrug +
				", 몸무게: " + weight +
				"}}";
	}
}