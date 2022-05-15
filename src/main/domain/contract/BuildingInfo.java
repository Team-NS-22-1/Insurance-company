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

	public void setBuildingArea(int buildingArea) {
		this.buildingArea = buildingArea;
	}

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}

	public int getCollateralAmount() {
		return collateralAmount;
	}

	public void setCollateralAmount(int collateralAmount) {
		this.collateralAmount = collateralAmount;
	}

	public boolean isActualResidence() {
		return isActualResidence;
	}

	public void setActualResidence(boolean actualResidence) {
		isActualResidence = actualResidence;
	}

	public boolean isSelfOwned() {
		return isSelfOwned;
	}

	public void setSelfOwned(boolean selfOwned) {
		isSelfOwned = selfOwned;
	}

	@Override
	public String toString() {
		return "{" +
				"주택면적: " + buildingArea +
				", 건물종류: " + buildingType +
				", 담보금액: " + collateralAmount +
				", 실거주여부: " + isActualResidence +
				", 자가여부: " + isSelfOwned +
				'}';
	}
}