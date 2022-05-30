package dao;

import domain.contract.BuildingType;
import domain.insurance.DevInfo;
import domain.insurance.Insurance;
import domain.insurance.InsuranceType;
import domain.insurance.SalesAuthState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<Insurance> readAll() throws SQLException {
        Insurance insurance;
        ArrayList<Insurance> insuranceList = new ArrayList<>();
        String query = "select * from insurance";
        ResultSet rs = super.read(query);
        while (rs.next()) {
            insurance = new Insurance();
            insurance.setId(rs.getInt("insurance_id"));
            insurance.setName(rs.getString("name"));
            insurance.setDescription(rs.getString("description"));
            insurance.setContractPeriod(rs.getInt("contract_period"));
            insurance.setPaymentPeriod(rs.getInt("payment_period"));
            switch (rs.getString("insurance_type")) {
                case "건강":
                    insurance.setInsuranceType(InsuranceType.HEALTH);
                    break;
                case "화재":
                    insurance.setInsuranceType(InsuranceType.FIRE);
                    break;
                case "자동차":
                    insurance.setInsuranceType(InsuranceType.CAR);
                    break;
            }
            insuranceList.add(insurance);
        }
        return insuranceList;
    }

    public DevInfo readDevInfo(int id) throws SQLException {
        DevInfo devInfo = null;
        String query = "select * from dev_info where insurance_id = " + id;
        ResultSet rs = super.read(query);

        if (rs.next()) {
            devInfo = new DevInfo();
            devInfo.setId(rs.getInt("dev_info_id"));
            devInfo.setInsuranceId(rs.getInt("insurance_id"));
            devInfo.setEmployeeId(rs.getInt("employee_id"));
            devInfo.setDevDate(rs.getDate("dev_date").toLocalDate());
            devInfo.setSalesStartDate(rs.getDate("sales_start_date").toLocalDate());
            switch (rs.getString("sales_auth_state")) {
                case "대기":
                    devInfo.setSalesAuthState(SalesAuthState.WAIT);
                    break;
                case "허가":
                    devInfo.setSalesAuthState(SalesAuthState.PERMISSION);
                    break;
                case "불허":
                    devInfo.setSalesAuthState(SalesAuthState.DISALLOWANCE);
                    break;
            }
        }
        return devInfo;
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
