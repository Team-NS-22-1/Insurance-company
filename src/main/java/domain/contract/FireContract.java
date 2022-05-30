package domain.contract;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class FireContract extends Contract{

	private int buildingArea;
	private BuildingType buildingType;
	private Long collateralAmount;
	private boolean isActualResidence;
	private boolean isSelfOwned;

	public FireContract(){
	}

	public int getBuildingArea() {
		return buildingArea;
	}

	public FireContract setBuildingArea(int buildingArea) {
		this.buildingArea = buildingArea;
		return this;
	}

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public FireContract setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
		return this;
	}

	public Long getCollateralAmount() {
		return collateralAmount;
	}

	public FireContract setCollateralAmount(Long collateralAmount) {
		this.collateralAmount = collateralAmount;
		return this;
	}

	public boolean isActualResidence() {
		return isActualResidence;
	}

	public FireContract setActualResidence(boolean actualResidence) {
		isActualResidence = actualResidence;
		return this;
	}

	public boolean isSelfOwned() {
		return isSelfOwned;
	}

	public FireContract setSelfOwned(boolean selfOwned) {
		isSelfOwned = selfOwned;
		return this;
	}

	@Override
	public String toString() {
		return "계약정보 {" +
				"계약 ID: " + getId() +
				", 인수심사상태: " + getConditionOfUw() +
				", 인수사유: '" + getReasonOfUw() + '\'' +
				", 증권발행여부: " + isPublishStock() +
				", 보험료: " + getPremium() +
				", 결제수단: " + getPayment() +
				", 주택면적: " + buildingArea +
				", 건물종류: " + buildingType +
				", 담보금액: " + collateralAmount +
				", 실거주여부: " + isActualResidence +
				", 자가여부: " + isSelfOwned +
				'}';
	}
}