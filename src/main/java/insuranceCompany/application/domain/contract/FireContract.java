package insuranceCompany.application.domain.contract;


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
		return super.toString() +
				", 주택정보: {" +
				"주택면적: " + buildingArea +
				", 건물종류: " + buildingType.getName() +
				", 담보금액: " + collateralAmount +
				", 실거주여부: " + isActualResidence +
				", 자가여부: " + isSelfOwned +
				"}}";
	}
}