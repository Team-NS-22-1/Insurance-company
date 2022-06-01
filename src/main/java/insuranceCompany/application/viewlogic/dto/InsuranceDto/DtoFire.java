package insuranceCompany.application.viewlogic.dto.InsuranceDto;

import insuranceCompany.application.domain.contract.BuildingType;

public class DtoFire extends DtoTypeInfo {

    private BuildingType buildingType;

    private long collateralAmount;

    public DtoFire(BuildingType buildingType, long collateralAmount) {
        this.buildingType = buildingType;
        this.collateralAmount = collateralAmount;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public long getCollateralAmount() {
        return collateralAmount;
    }
}
