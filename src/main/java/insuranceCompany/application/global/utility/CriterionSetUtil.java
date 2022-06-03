package insuranceCompany.application.global.utility;

public class CriterionSetUtil {

    public static int setTargetAge(int age) {
        int targetAge = 0;

        if(age >= 100) targetAge = 100;
        else if(age >= 80) targetAge = 80;
        else if(age >= 60) targetAge = 60;
        else if(age >= 50) targetAge = 50;
        else if(age >= 40) targetAge = 40;
        else if(age >= 30) targetAge = 30;
        else if(age >= 20) targetAge = 20;

        return targetAge;
    }

    public static Long setCollateralAmountCriterion (Long collateralAmount){
        long collateralAmountCriterion = 0L;

        if(collateralAmount >= 5000000000L) collateralAmountCriterion = 5000000000L;
        else if(collateralAmount >= 1000000000L) collateralAmountCriterion = 1000000000L;
        else if(collateralAmount >= 500000000L) collateralAmountCriterion = 500000000L;
        else if(collateralAmount >= 100000000L) collateralAmountCriterion = 100000000L;

        return collateralAmountCriterion;
    }

    public static Long setValueCriterion (Long value) {
        long valueCriterion = 0L;

        if (value >= 150000000L) valueCriterion = 150000000L;
        else if (value >= 90000000L) valueCriterion = 90000000L;
        else if (value >= 70000000L) valueCriterion = 70000000L;
        else if (value >= 50000000L) valueCriterion = 50000000L;
        else if (value >= 30000000L) valueCriterion = 30000000L;
        else if (value >= 10000000L) valueCriterion = 10000000L;

        return valueCriterion;
    }

    public static boolean setRiskCriterion (int riskCount) {
        return riskCount >= 4;
    }
}
