package dao;

import domain.contract.BuildingType;

public class InsuranceDatailDao extends Dao{
    public InsuranceDatailDao() {
        super.connect();
    }

    public static int readHealthPremium(int targetAge, boolean targetSex, boolean riskPremiumCriterion){
       return 0;
    }

    public static int readHealthPremium(BuildingType buildingType, Long collateralAmount){
        return 0;
    }

    public static int readHealthPremium(int targetAge, Long value){
        return 0;
    }
}
