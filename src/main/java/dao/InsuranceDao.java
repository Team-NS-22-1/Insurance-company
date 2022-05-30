package dao;

import domain.contract.BuildingType;
import domain.insurance.Insurance;
import domain.insurance.InsuranceType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InsuranceDao extends Dao {

    public InsuranceDao() {
        super.connect();
    }

    public void create(Insurance insurance) {
        String query = "";

        super.create(query);
    }

    public Insurance read(int id) throws SQLException{
        Insurance insurance = null;
        String query = "select * from insurance where insurance_id = " + id;
        ResultSet rs = super.read(query);
        if (rs.next()) {
            insurance = new Insurance();
            insurance.setId(rs.getInt("insurance_id"));
            insurance.setName(rs.getString("name"));
            insurance.setDescription(rs.getString("description"));
            insurance.setContractPeriod(rs.getInt("contract_period"));
            insurance.setPaymentPeriod(rs.getInt("payment_period"));
            switch (rs.getString("insurance_type")) {
                case "HEALTH":
                    insurance.setInsuranceType(InsuranceType.HEALTH);
                    break;
                case "FIRE":
                    insurance.setInsuranceType(InsuranceType.FIRE);
                    break;
                case "CAR":
                    insurance.setInsuranceType(InsuranceType.CAR);
                    break;
            }
        }
        return insurance;
    }

    public /* ArrayList<Insurance>*/Insurance readAll() throws SQLException {
        Insurance insurance = null;
//        ArrayList<Insurance> insuranceList = new ArrayList<>();
        String query = "select * from insurance";
        ResultSet rs = super.read(query);
        while (rs.next()) {
            int i = 0;
            insurance = new Insurance();
            insurance.setId(rs.getInt("insurance_id"));
            insurance.setName(rs.getString("name"));
            insurance.setDescription(rs.getString("description"));
            insurance.setContractPeriod(rs.getInt("contract_period"));
            insurance.setPaymentPeriod(rs.getInt("payment_period"));
            switch (rs.getString("insurance_type")) {
                case "HEALTH":
                    insurance.setInsuranceType(InsuranceType.HEALTH);
                    break;
                case "FIRE":
                    insurance.setInsuranceType(InsuranceType.FIRE);
                    break;
                case "CAR":
                    insurance.setInsuranceType(InsuranceType.CAR);
                    break;
            }
//            insuranceList.add(insurance);
        }
        return insurance;
    }

    public void update(Insurance insurance) {

    }

    public void delete(Insurance insurance) {

    }

    public int readHealthPremium(int targetAge, boolean targetSex, boolean riskPremiumCriterion) {
        return 0;
    }

    public int readCarPremium(int targetAge, Long valueCriterion) {
        return 0;
    }

    public int readFirePremium(BuildingType buildingType, Long collateralAmount) {
        return 0;
    }
}
