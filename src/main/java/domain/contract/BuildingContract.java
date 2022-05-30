package domain.contract;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class BuildingContract extends  Contract {

	private int buildingArea;
	private BuildingType buildingType;
	private int collateralAmount;
	private boolean isActualResidence;
	private boolean isSelfOwned;

	public BuildingContract(){
	}

	public int getBuildingArea() {
		return buildingArea;
	}

	public BuildingContract setBuildingArea(int buildingArea) {
		this.buildingArea = buildingArea;
		return this;
	}

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public BuildingContract setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
		return this;
	}

	public int getCollateralAmount() {
		return collateralAmount;
	}

	public BuildingContract setCollateralAmount(int collateralAmount) {
		this.collateralAmount = collateralAmount;
		return this;
	}

	public boolean isActualResidence() {
		return isActualResidence;
	}

	public BuildingContract setActualResidence(boolean actualResidence) {
		isActualResidence = actualResidence;
		return this;
	}

	public boolean isSelfOwned() {
		return isSelfOwned;
	}

	public BuildingContract setSelfOwned(boolean selfOwned) {
		isSelfOwned = selfOwned;
		return this;
	}

	@Override
	public String toString() {
		return super.toString() +
				" 주택정보: {" +
				"주택면적: " + buildingArea +
				", 건물종류: " + buildingType.getName() +
				", 담보금액: " + collateralAmount +
				", 실거주여부: " + isActualResidence +
				", 자가여부: " + isSelfOwned +
				"}}";
	}
}