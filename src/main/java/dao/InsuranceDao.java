package dao;

import domain.insurance.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class InsuranceDao extends Dao {

    public InsuranceDao() {
        super.connect();
    }

    public void create(Insurance insurance) {
        try {
            // CREATE insurance
            String queryFormat =
                    "INSERT INTO insurance (name, description, contract_period, payment_period, insurance_type) VALUES ('%s','%s',%d,%d,'%s');";
            String queryInsurance =
                    String.format(queryFormat, insurance.getName(), insurance.getDescription(), insurance.getContractPeriod(), insurance.getPaymentPeriod(), insurance.getInsuranceType().getName());
            int insuranceId = super.create(queryInsurance);
            insurance.setId(insuranceId);

            // CREATE guarantee
            ArrayList<Guarantee> guarantees = insurance.getGuaranteeList();
            for(Guarantee guarantee : guarantees) {
                String queryFormatGuarantee =
                        "INSERT INTO guarantee (insurance_id, name, description, amount) VALUES (%d, '%s', '%s', %d);";
                String queryGuarantee =
                        String.format(queryFormatGuarantee, insuranceId, guarantee.getName(), guarantee.getDescription(), guarantee.getGuaranteeAmount());
                super.create(queryGuarantee);
            }

            // CREATE devInfo
            DevInfo devInfo = insurance.getDevInfo();
            String queryFormatDevInfo =
                    "INSERT INTO dev_info (insurance_id, employee_id, dev_date, sales_auth_state) VALUES (%d, %d, '%s', '%s');";
            String queryDevInfo =
                    String.format(queryFormatDevInfo, insuranceId, devInfo.getEmployeeId(), java.sql.Date.valueOf(devInfo.getDevDate()), devInfo.getSalesAuthState().getName());
            super.create(queryDevInfo);

            // CREATE insurance detail
            switch (insurance.getInsuranceType()) {
                case HEALTH -> {
                    ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();
                    for(InsuranceDetail insuranceDetail : insuranceDetails) {
                        String queryFormatInsuranceDetail =
                                "INSERT INTO insurance_detail (premium, insurance_id) VALUES (%d, %d);";
                        String queryInsuranceDetail =
                                String.format(queryFormatInsuranceDetail, insuranceDetail.getPremium(), insuranceId);
                        int insuranceDetailId = super.create(queryInsuranceDetail);

                        HealthDetail healthDetail = (HealthDetail) insuranceDetail;
                        // True(Male): 0 , False(Female): 1
                        int targetSex = healthDetail.isTargetSex() ? 0 : 1;
                        String queryFormatHealthDetail =
                                "INSERT INTO health_detail (health_detail_id, target_age, target_sex, risk_criterion) VALUES (%d, %d, %d, %d);";
                        String queryHealthDetail =
                                String.format(queryFormatHealthDetail, insuranceDetailId, healthDetail.getTargetAge(), targetSex, healthDetail.getRiskPremiumCriterion());
                        super.create(queryHealthDetail);
                    }
                }
                case CAR ->  {
                    ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();
                    for(InsuranceDetail insuranceDetail : insuranceDetails) {
                        String queryFormatInsuranceDetail =
                                "INSERT INTO insurance_detail (premium, insurance_id) VALUES (%d, %d);";
                        String queryInsuranceDetail =
                                String.format(queryFormatInsuranceDetail, insuranceDetail.getPremium(), insuranceDetail.getInsuranceId());
                        int insuranceDetailId = super.create(queryInsuranceDetail);

                        CarDetail carDetail = (CarDetail) insuranceDetail;
                        String queryFormatCarDetail =
                                "INSERT INTO car_detail (car_detail_id, target_age, value_criterion) VALUES (%d, %d, %d);";
                        String queryCarDetail =
                                String.format(queryFormatCarDetail, insuranceDetailId, carDetail.getTargetAge(), carDetail.getValueCriterion());
                        super.create(queryCarDetail);
                    }
                }
                case FIRE -> {
                    ArrayList<InsuranceDetail> insuranceDetails = insurance.getInsuranceDetailList();
                    for(InsuranceDetail insuranceDetail : insuranceDetails) {
                        String queryFormatInsuranceDetail =
                                "INSERT INTO insurance_detail (premium, insurance_id) VALUES (%d, %d);";
                        String queryInsuranceDetail =
                                String.format(queryFormatInsuranceDetail, insuranceDetail.getPremium(), insuranceDetail.getInsuranceId());
                        int insuranceDetailId = super.create(queryInsuranceDetail);

                        FireDetail fireDetail = (FireDetail) insuranceDetail;
                        String queryFormatFireDetail =
                                "INSERT INTO fire_detail (fire_detail_id, target_building_type, collateral_amount_criterion) VALUES (%d, '%s', %d);";
                        String queryFireDetail =
                                String.format(queryFormatFireDetail, insuranceDetailId, fireDetail.getTargetBuildingType().getName(), fireDetail.getCollateralAmountCriterion());
                        super.create(queryFireDetail);
                    }
                }
            }
        }
        finally {
            super.close();
        }
    }

    public ArrayList<Insurance> read(int id) throws SQLException {
        ArrayList<Insurance> insuranceList = new ArrayList<>();
        Insurance insurance = null;
        try {
            String query = "SELECT * FROM insurance WHERE insurance_id = "+id;
            super.read(query);
            if(resultSet.next()){
                insurance.setId(resultSet.getInt("insurance_id"))
                        .setName(resultSet.getString("name"))
                        .setDescription(resultSet.getString("description"))
                        .setContractPeriod(resultSet.getInt("contract_period"))
                        .setPaymentPeriod(resultSet.getInt("payment_period"))
                        .setInsuranceType(InsuranceType.valueOf(resultSet.getString("insurance_type")));
                insuranceList.add(insurance);
            }
        }
        finally {
            super.close();
        }
        return insuranceList;
    }

    public void update(Insurance insurance) {

    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM insurance WHERE insurance_id = ?";
    }

}
