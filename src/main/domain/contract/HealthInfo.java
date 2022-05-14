package main.domain.contract;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class HealthInfo {

	private int height;
	private boolean isDangerActivity;
	private boolean isDrinking;
	private boolean isDriving;
	private boolean isHavingDisease;
	private boolean isSmoking;
	private boolean isTakingDrug;
	private int weight;

	public HealthInfo(){

	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isDangerActivity() {
		return isDangerActivity;
	}

	public void setDangerActivity(boolean dangerActivity) {
		isDangerActivity = dangerActivity;
	}

	public boolean isDrinking() {
		return isDrinking;
	}

	public void setDrinking(boolean drinking) {
		isDrinking = drinking;
	}

	public boolean isDriving() {
		return isDriving;
	}

	public void setDriving(boolean driving) {
		isDriving = driving;
	}

	public boolean isHavingDisease() {
		return isHavingDisease;
	}

	public void setHavingDisease(boolean havingDisease) {
		isHavingDisease = havingDisease;
	}

	public boolean isSmoking() {
		return isSmoking;
	}

	public void setSmoking(boolean smoking) {
		isSmoking = smoking;
	}

	public boolean isTakingDrug() {
		return isTakingDrug;
	}

	public void setTakingDrug(boolean takingDrug) {
		isTakingDrug = takingDrug;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "건강정보 {" +
				"키: " + height +
				", 위험활동여부: " + isDangerActivity +
				", 음주여부: " + isDrinking +
				", 운전여부: " + isDriving +
				", 질병여부: " + isHavingDisease +
				", 흡연여부: " + isSmoking +
				", 약물복용여부: " + isTakingDrug +
				", 몸무게: " + weight +
				'}';
	}
}