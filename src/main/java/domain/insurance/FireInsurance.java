package domain.insurance;


import domain.contract.BuildingType;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:00
 */
public class FireInsurance extends Insurance {

	private BuildingType buildingType;
	private long collateralAmount;

	
	public FireInsurance(){
	}

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public FireInsurance setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
		return this;
	}

	public long getCollateralAmount() {
		return collateralAmount;
	}

	public FireInsurance setCollateralAmount(long collateralAmount) {
		this.collateralAmount = collateralAmount;
		return this;
	}
}