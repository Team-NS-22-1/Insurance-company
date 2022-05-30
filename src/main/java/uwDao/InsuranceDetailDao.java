package uwDao;

import domain.contract.BuildingType;
import domain.insurance.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsuranceDetailDao extends Dao {

    public InsuranceDetailDao() {
        super.connect();
    }


    public void create(Insurance insurance) {

    }

    public InsuranceDetail read(int id) {
        String query = "SELECT * \n" +
                "FROM insurance i\n" +
                "LEFT JOIN insurance_detail id\n" +
                "ON i.insurance_id = id.insurance_id\n" +
                "LEFT JOIN car_detail cd\n" +
                "ON id.insurance_detail_id = cd.car_detail_id\n" +
                "LEFT JOIN fire_detail fd\n" +
                "ON id.insurance_detail_id = fd.fire_detail_id\n" +
                "LEFT JOIN health_detail hd\n" +
                "ON id.insurance_detail_id = hd.health_detail_id\n" +
                "WHERE i.insurance_id = " + id;

        InsuranceDetail insuranceDetail = null;

        try {

            ResultSet rs = super.read(query);

            while (rs.next()) {

                InsuranceType insuranceType = InsuranceType.valueOf(rs.getString("insurance_type"));

                switch (insuranceType) {
                    case HEALTH -> insuranceDetail = setHealthDetail(rs);
                    case CAR -> insuranceDetail = setCarDetail(rs);
                    case FIRE -> insuranceDetail = setFireDetail(rs);
                }
                insuranceDetail.setPremium(rs.getInt("premium"));
                insuranceDetail.setInsuranceId(rs.getInt("insurance_id"));
                insuranceDetail.setId(rs.getInt("insurance_detail_id"));
            }

            super.close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return insuranceDetail;
    }

    public HealthDetail setHealthDetail(ResultSet rs) {
        HealthDetail healthDetail = new HealthDetail();

        try {
            healthDetail.setRiskCriterion(rs.getInt("risk_criterion"));
            healthDetail.setTargetAge(rs.getInt("target_age"));
            healthDetail.setTargetSex(rs.getInt("target_sex") != 0);
            healthDetail.setId(rs.getInt("health_detail_id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return healthDetail;
    }

    public FireDetail setFireDetail(ResultSet rs) {
        FireDetail fireDetail = new FireDetail();

        try {
            fireDetail.setCollateralAmountCriterion(rs.getInt("collateral_amount_criterion"));
            fireDetail.setTargetBuildingType(BuildingType.valueOf(rs.getString("target_building_type")));
            fireDetail.setId(rs.getInt("fire_detail_id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fireDetail;
    }

    public CarDetail setCarDetail(ResultSet rs) {
        CarDetail carDetail = new CarDetail();

        try {
            carDetail.setValueCriterion(rs.getInt("value_criterion"));
            carDetail.setTargetAge(rs.getInt("target_age"));
            carDetail.setId(rs.getInt("car_detail_id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carDetail;
    }




}