package dao;

import domain.contract.CarContract;
import domain.contract.Contract;
import domain.contract.FireContract;
import domain.contract.HealthContract;
import domain.customer.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractDao extends Dao{
    public ContractDao() {
        super.connect();
    }

    public void create(Contract contract) throws SQLException {
        try {
            String query = "insert into contract (insurance_id, customer_id, employee_id, premium, is_publish_stock, condition_of_uw) values ('%d', '%d', '%d', '%d', '%d', '%s');";
            String contractQuery = String.format(query, contract.getInsuranceId(), contract.getCustomerId(), contract.getEmployeeId(), contract.getPremium(), contract.isPublishStock() ? 1 : 0, contract.getConditionOfUw());
            int id = super.create(contractQuery);
            contract.setId(id);

            String joinningquery = "select insurance_type from insurance " +
                    "inner join contract " +
                    "on contract.insurance_id = insurance.insurance_id " +
                    "where insurance.insurance_id = " + contract.getInsuranceId() + ";";

            ResultSet rs = super.read(joinningquery);
            String inputquery = null;
            String input;
            if(rs.next())
                switch (rs.getString("insurance_type")) {
                    case "건강":
                        HealthContract healthContract = (HealthContract) contract;
                        inputquery = "insert into health_contract (contract_id, height, weight, is_danger_activity, " +
                                "is_drinking, is_smoking, is_taking_drug, is_having_disease, is_danger_activity, disease_detail";
                        input = String.format(inputquery, contract.getId(), healthContract.getHeight(), healthContract.getWeight(),
                                healthContract.isDangerActivity(), healthContract.isDrinking(), healthContract.isSmoking(), healthContract.isSmoking(),
                                healthContract.isTakingDrug(), healthContract. isHavingDisease(), healthContract.isDangerActivity(), healthContract.getDiseaseDetail());
                        super.create(input);
                    case "화재":
                        FireContract fireContract = (FireContract) contract;
                        inputquery = "insert into fire_contract (contract_id, building_area, building_type, collateral_amount, " +
                                "is_actual_residence, is_self_owned";
                        input = String.format(inputquery, contract.getId(), fireContract.getBuildingArea(), fireContract.getBuildingType(),
                                fireContract.getCollateralAmount(), fireContract.isActualResidence(), fireContract.isSelfOwned());
                        super.create(input);
                    case "자동차":
                        CarContract carContract = (CarContract) contract;
                        inputquery = "insert into fire_contract (contract_id, car_no, car_type, model_year, model_name, value ";
                        input = String.format(inputquery, contract.getId(), carContract.getCarNo(), carContract.getCarType(),
                                carContract.getModelYear(), carContract.getModelName(), carContract.getValue() );
                        super.create(input);
                }
        }finally {
            close();
        }
    }


    public void update(Customer customer) {

    }

    public void delete(Customer customer) {

    }
}
