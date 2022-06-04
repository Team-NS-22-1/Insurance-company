package insuranceCompany.application.viewlogic.dto.contractDto;

import insuranceCompany.application.domain.contract.BuildingType;

public class FireContractDto extends ContractDto {
    private int buildingArea;
    private BuildingType buildingType;
    private Long collateralAmount;
    private boolean isActualResidence;
    private boolean isSelfOwned;

    public FireContractDto () {
    }

    public FireContractDto (int buildingArea, BuildingType buildingType, Long collateralAmount, boolean isActualResidence, boolean isSelfOwned) {
        this.buildingArea = buildingArea;
        this.buildingType = buildingType;
        this.collateralAmount = collateralAmount;
        this.isActualResidence = isActualResidence;
        this.isSelfOwned = isSelfOwned;
    }

    public int getBuildingArea() {
        return buildingArea;
    }

    public FireContractDto setBuildingArea(int buildingArea) {
        this.buildingArea = buildingArea;
        return this;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public FireContractDto setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
        return this;
    }

    public Long getCollateralAmount() {
        return collateralAmount;
    }

    public FireContractDto setCollateralAmount(Long collateralAmount) {
        this.collateralAmount = collateralAmount;
        return this;
    }

    public boolean isActualResidence() {
        return isActualResidence;
    }

    public FireContractDto setActualResidence(boolean actualResidence) {
        isActualResidence = actualResidence;
        return this;
    }

    public boolean isSelfOwned() {
        return isSelfOwned;
    }

    public FireContractDto setSelfOwned(boolean selfOwned) {
        isSelfOwned = selfOwned;
        return this;
    }
}
