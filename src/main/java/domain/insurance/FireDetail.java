package domain.insurance;


import domain.contract.BuildingType;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:00
 */
public class FireDetail extends InsuranceDetail {

	private BuildingType targetBuildingType;
	private long collateralAmountCriterion;

	
	public FireDetail(){
	}

	public BuildingType getTargetBuildingType() {
		return targetBuildingType;
	}

	public FireDetail setTargetBuildingType(BuildingType targetBuildingType) {
		this.targetBuildingType = targetBuildingType;
		return this;
	}

	public long getCollateralAmountCriterion() {
		return collateralAmountCriterion;
	}

	public FireDetail setCollateralAmountCriterion(long collateralAmountCriterion) {
		this.collateralAmountCriterion = collateralAmountCriterion;
		return this;
	}

	public String print() {
		return "화재보험 정보 {" +
				"화재보험정보 ID:" + getId() +
				", 보험료: " + getPremium() +
				", 대상건물종류: " + targetBuildingType +
				", 담보금액 기준: " + collateralAmountCriterion +
				", 보험ID: " + getInsuranceId() +
				"}";
	}
}