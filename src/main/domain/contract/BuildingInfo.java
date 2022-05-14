package main.domain.contract;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class BuildingInfo {

	private int buildingArea;
	private BuildingType buildingType;
	private int collateralAmount;
	private boolean isActualResidence;
	private boolean isSelfOwned;

	public BuildingInfo(){
	}

	public int getBuildingArea() {
		return buildingArea;
	}

	public BuildingInfo setBuildingArea(int buildingArea) {
		this.buildingArea = buildingArea;
		return this;
	}

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public BuildingInfo setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
		return this;
	}

	public int getCollateralAmount() {
		return collateralAmount;
	}

	public BuildingInfo setCollateralAmount(int collateralAmount) {
		this.collateralAmount = collateralAmount;
		return this;
	}

	public boolean isActualResidence() {
		return isActualResidence;
	}

	public BuildingInfo setActualResidence(boolean actualResidence) {
		isActualResidence = actualResidence;
		return this;
	}

	public boolean isSelfOwned() {
		return isSelfOwned;
	}

	public BuildingInfo setSelfOwned(boolean selfOwned) {
		isSelfOwned = selfOwned;
		return this;
	}

	@Override
	public String toString() {
		return "화재정보 {" +
				"주택면적: " + buildingArea +
				", 건물종류: " + buildingType +
				", 담보금액: " + collateralAmount +
				", 실거주여부: " + isActualResidence +
				", 자가여부: " + isSelfOwned +
				'}';
	}
}