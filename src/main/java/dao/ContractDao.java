package dao;

import domain.contract.*;
import domain.customer.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractDao extends Dao{
    public ContractDao() {
        super.connect();
    }

    public void create(Contract contract) throws SQLException {
        String query = "insert into contract (insurance_id, customer_id, employee_id, premium, is_publish_stock, condition_of_uw) values (%d, %d, %d, %d, %b, %s);";
        String contractQuery = String.format(query, contract.getInsuranceId(), contract.getCustomerId(), contract.getEmployeeId(), contract.getPremium(), false, ConditionOfUw.WAIT);
        int id = super.create(contractQuery);
        contract.setId(id);

        String joinningquery = "select insurance_type from insurance on contract.insurance_id = insurance.insurance_id";
        ResultSet rs = super.read(joinningquery);
        String inputquery = null;
        String input;
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
        close();
    }

//    public void create(FireContract fireContract) {
//        String query = "insert into fire_contract (contract_id, building_area, building_type, collarteral_amount, is_actual_residence, is_self_owned) values (%d, %d, %s, %d, %b, %b);";
//        String contractQuery = String.format(query, contract.getInsuranceId(), contract.getCustomerId(), contract.getEmployeeId(), contract.getPremium(), false, ConditionOfUw.WAIT);
//        int id = super.create(contractQuery);
//        contract.setId(id);
//
//        close();
//    }
//
//    public Contract read(int id) throws SQLException {
//        Contract contract = new Contract();
//        String query = "select * from customer where customer_id = " + id;
//        try {
//            ResultSet rs = super.read(query);
//            if (rs.next()) {
//                contract.setId(rs.getInt("contract_id"));
//                contract.setReasonOfUw(rs.getString("reason_of_uw"));
////            contract.setPayment(PaymentDao.read(rs.getInt("payment_id")));
//                contract.setInsuranceId(rs.getInt("insurance_id"));
//                contract.setCustomerId(rs.getInt("customer_id"));
//                contract.setEmployeeId(rs.getInt("employee_id"));
//                contract.setPremium(rs.getInt("premium"));
//                contract.setPublishStock(rs.getBoolean("is_publish_stock"));
//                switch (rs.getString("condition_of_uw")) {
//                    case "WAIT":
//                        contract.setConditionOfUw(ConditionOfUw.WAIT);
//                        break;
//                    case "APPROVAL":
//                        contract.setConditionOfUw(ConditionOfUw.APPROVAL);
//                        break;
//                    case "RE_AUDIT":
//                        contract.setConditionOfUw(ConditionOfUw.RE_AUDIT);
//                        break;
//                    case "REFUSE":
//                        contract.setConditionOfUw(ConditionOfUw.REFUSE);
//                        break;
//                }
//            }
//            return contract;
//        } catch (
//
//        )
//
//    }

    public void update(Customer customer) {

    }

    public void delete(Customer customer) {

    }
}
