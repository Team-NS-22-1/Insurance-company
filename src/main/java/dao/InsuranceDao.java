package dao;

import domain.contract.BuildingType;
import domain.insurance.*;

import java.sql.Date;
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
                    String.format(queryFormat, insurance.getName(), insurance.getDescription(), insurance.getContractPeriod(), insurance.getPaymentPeriod(), insurance.getInsuranceType().name());
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
                    String.format(queryFormatDevInfo, insuranceId, devInfo.getEmployeeId(), java.sql.Date.valueOf(devInfo.getDevDate()), devInfo.getSalesAuthState().name());
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
                        // True(Male): 1 , False(Female): 0
                        int targetSex = healthDetail.isTargetSex() ? 1 : 0;
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
                                String.format(queryFormatFireDetail, insuranceDetailId, fireDetail.getTargetBuildingType().name(), fireDetail.getCollateralAmountCriterion());
                        super.create(queryFireDetail);
                    }
                }
            }

            // CREATE sales auth file
            SalesAuthFile salesAuthFile = insurance.getSalesAuthFile();
            salesAuthFile.setInsuranceId(insuranceId);
            String queryFormatSalesAuthFile =
                    "INSERT INTO sales_auth_file (insurance_id) VALUES (%d);";
            String querySalesAuthFile =
                    String.format(queryFormatSalesAuthFile, insuranceId);
            super.create(querySalesAuthFile);

        }
        finally {
            super.close();
        }
    }

    public Insurance read(int id) {
        Insurance insurance = new Insurance();
        try {
            // READ insurance
            String query = "SELECT * FROM insurance WHERE insurance_id = " + id + ";";
            super.read(query);
            if(resultSet.next()){
                insurance.setId(resultSet.getInt("insurance_id"))
                        .setName(resultSet.getString("name"))
                        .setDescription(resultSet.getString("description"))
                        .setContractPeriod(resultSet.getInt("contract_period"))
                        .setPaymentPeriod(resultSet.getInt("payment_period"))
                        .setInsuranceType(InsuranceType.valueOf(resultSet.getString("insurance_type")));
            }

            // READ guarantee
            ArrayList<Guarantee> guarantees = new ArrayList<>();
            String queryGuarantee = "SELECT * FROM guarantee WHERE insurance_id = " + id + ";";
            super.read(queryGuarantee);
            while(resultSet.next()) {
                guarantees.add(
                        new Guarantee().setId(resultSet.getInt("guarantee_id"))
                                .setName(resultSet.getString("name"))
                                .setDescription(resultSet.getString("description"))
                                .setGuaranteeAmount(resultSet.getLong("amount"))
                                .setInsuranceId(resultSet.getInt("insurance_id"))
                );
            }
            insurance.setGuaranteeList(guarantees);

            // READ devInfo
            DevInfo devInfo = new DevInfo();
            String queryDevInfo = "SELECT * FROM dev_info WHERE insurance_id = " + id + ";";
            super.read(queryDevInfo);
            if(resultSet.next()){
                devInfo.setId(resultSet.getInt("dev_info_id"))
                        .setEmployeeId(resultSet.getInt("employee_id"))
                        .setDevDate(resultSet.getDate("dev_date").toLocalDate())
                        .setSalesAuthState(SalesAuthState.valueOf(resultSet.getString("sales_auth_state")))
                        .setInsuranceId(resultSet.getInt("insurance_id"));
                Date salesStartDate = resultSet.getDate("sales_start_date");
                devInfo.setSalesStartDate(resultSet.wasNull() ? null : salesStartDate.toLocalDate());
            }
            insurance.setDevInfo(devInfo);

            // READ insuranceDetail
            ArrayList<InsuranceDetail> insuranceDetails = new ArrayList<>();
            switch (insurance.getInsuranceType()) {
                case HEALTH -> {
                    String queryHealthDetail =
                            "SELECT * FROM insurance_detail " +
                                    "INNER JOIN health_detail " +
                                    "ON insurance_detail.insurance_detail_id = health_detail.health_detail_id " +
                                "WHERE insurance_id = " + id + ";";
                    super.read(queryHealthDetail);
                    while(resultSet.next()){
                        insuranceDetails.add(
                                new HealthDetail().setTargetAge(resultSet.getInt("target_age"))
                                        .setTargetSex(resultSet.getInt("target_sex")==1 ? true : false)
                                        .setRiskCriterion(resultSet.getInt("risk_criterion"))
                                        .setId(resultSet.getInt("health_detail_id"))
                                        .setPremium(resultSet.getInt("premium"))
                                        .setInsuranceId(resultSet.getInt("insurance_id"))
                        );
                    }
                    insurance.setInsuranceDetailList(insuranceDetails);
                }
                case CAR -> {
                    String queryCarDetail =
                            "SELECT * FROM insurance_detail " +
                                    "INNER JOIN car_detail " +
                                    "ON insurance_detail.insurance_detail_id = car_detail.car_detail_id " +
                                "WHERE insurance_id = " + id + ";";
                    super.read(queryCarDetail);
                    while(resultSet.next()) {
                        insuranceDetails.add(
                                new CarDetail().setTargetAge(resultSet.getInt("target_age"))
                                        .setValueCriterion(resultSet.getLong("value_criterion"))
                                        .setId(resultSet.getInt("car_detail"))
                                        .setPremium(resultSet.getInt("premium"))
                                        .setInsuranceId(resultSet.getInt("insurance_id"))
                        );
                    }
                    insurance.setInsuranceDetailList(insuranceDetails);
                }
                case FIRE -> {
                    String queryFireDetail =
                            "SELECT * FROM insurance_detail " +
                                    "INNER JOIN fire_detail " +
                                    "ON insurance_detail.insurance_detail_id = fire_detail.fire_detail_id " +
                                "WHERE insurance_id = " + id + ";";
                    super.read(queryFireDetail);
                    while(resultSet.next()) {
                        insuranceDetails.add(
                                new FireDetail().setTargetBuildingType(BuildingType.valueOf(resultSet.getString("target_building_type")))
                                        .setCollateralAmountCriterion(resultSet.getLong("collateral_amount_criterion"))
                                        .setId(resultSet.getInt("fire_detail_id"))
                                        .setPremium(resultSet.getInt("premium"))
                                        .setInsuranceId(resultSet.getInt("insurance_id"))
                        );
                    }
                    insurance.setInsuranceDetailList(insuranceDetails);
                }
            }

            // READ salesAuthFile
            SalesAuthFile salesAuthFile = new SalesAuthFile();
            String querySalesAuthFile =
                    "SELECT * FROM sales_auth_file WHERE insurance_id = " + id + ";";
            super.read(querySalesAuthFile);
            if (resultSet.next()) {
                salesAuthFile.setInsuranceId(resultSet.getInt("insurance_id"));
                String fssOfficialDoc = resultSet.getString("fss_official_doc");
                salesAuthFile.setFssOfficialDoc(resultSet.wasNull() ? null : fssOfficialDoc);
                java.sql.Timestamp modifiedFss = resultSet.getTimestamp("modified_fss");
                salesAuthFile.setModifiedFss(resultSet.wasNull() ? null : modifiedFss.toLocalDateTime());

                String isoVerification = resultSet.getString("iso_verification");
                salesAuthFile.setIsoVerification(resultSet.wasNull() ? null : isoVerification);
                java.sql.Timestamp modifiedIso = resultSet.getTimestamp("modified_iso");
                salesAuthFile.setModifiedIso(resultSet.wasNull() ? null : modifiedIso.toLocalDateTime());

                String prodDeclaration = resultSet.getString("prod_declaration");
                salesAuthFile.setProdDeclaration(resultSet.wasNull() ? null : prodDeclaration);
                java.sql.Timestamp modifiedProd = resultSet.getTimestamp("modified_prod");
                salesAuthFile.setModifiedProd(resultSet.wasNull() ? null : modifiedProd.toLocalDateTime());

                String srActuaryVerification = resultSet.getString("sr_actuary_verification");
                salesAuthFile.setSrActuaryVerification(resultSet.wasNull() ? null : srActuaryVerification);
                java.sql.Timestamp modifiedSrActuary = resultSet.getTimestamp("modified_sr_actuary");
                salesAuthFile.setModifiedSrActuary(resultSet.wasNull() ? null : modifiedSrActuary.toLocalDateTime());

            }
            insurance.setSalesAuthFile(salesAuthFile);

        }
        catch (NullPointerException | SQLException e) {
            e.printStackTrace();
        }
        finally {
            super.close();
        }
        return insurance;
    }

    public ArrayList<Insurance> readByEmployeeId(int eid) {
        ArrayList<Integer> insuranceIds = new ArrayList<>();
        ArrayList<Insurance> insurances = new ArrayList<>();
        try {
            String query =
                    "select * from insurance\n" +
                            "where insurance_id = (\n" +
                            "    select insurance_id\n" +
                            "    from dev_info\n" +
                            "    where employee_id = " + eid +
                            ");";
            super.read(query);
            while (resultSet.next()) {
                insuranceIds.add(resultSet.getInt("insurance_id"));
            }
            for(Integer insuranceId : insuranceIds) {
                insurances.add(this.read(insuranceId));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return insurances;
    }

    public void update(Insurance insurance) {

    }

    public void updateByFss(Insurance insurance) {
        SalesAuthFile salesAuthFile = insurance.getSalesAuthFile();
        String queryFormat =
                "UPDATE sales_auth_file SET fss_official_doc = '%s', modified_fss = '%s' WHERE insurance_id = %d;";
        String query =
                String.format(queryFormat, salesAuthFile.getFssOfficialDoc(), java.sql.Timestamp.valueOf(salesAuthFile.getModifiedFss()), insurance.getId());
        super.update(query);
    }

    public void updateByIso(Insurance insurance) {
        SalesAuthFile salesAuthFile = insurance.getSalesAuthFile();
        String queryFormat =
                "UPDATE sales_auth_file SET iso_verification = '%s', modified_iso = '%s' WHERE insurance_id = %d;";
        String query =
                String.format(queryFormat, salesAuthFile.getIsoVerification(), java.sql.Timestamp.valueOf(salesAuthFile.getModifiedIso()), insurance.getId());
        super.update(query);
    }

    public void updateByProd(Insurance insurance) {
        SalesAuthFile salesAuthFile = insurance.getSalesAuthFile();
        String queryFormat =
                "UPDATE sales_auth_file SET prod_declaration = '%s', modified_prod = '%s' WHERE insurance_id = %d;";
        String query =
                String.format(queryFormat, salesAuthFile.getProdDeclaration(), java.sql.Timestamp.valueOf(salesAuthFile.getModifiedProd()), insurance.getId());
        super.update(query);
    }

    public void updateBySrActuary(Insurance insurance) {
        SalesAuthFile salesAuthFile = insurance.getSalesAuthFile();
        String queryFormat =
                "UPDATE sales_auth_file SET sr_actuary_verification = '%s', modified_sr_actuary = '%s' WHERE insurance_id = %d;";
        String query =
                String.format(queryFormat, salesAuthFile.getSrActuaryVerification(), java.sql.Timestamp.valueOf(salesAuthFile.getModifiedSrActuary()), insurance.getId());
        super.update(query);
    }

    public void updateBySalesAuthState(Insurance insurance) {
        String queryFormat =
                "UPDATE dev_info SET sales_auth_state = '%s' WHERE insurance_id = %d;";
        String query =
                String.format(queryFormat, insurance.getDevInfo().getSalesAuthState().name(), insurance.getId());
        super.update(query);
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM insurance WHERE insurance_id = " + id + ";";
    }

}
