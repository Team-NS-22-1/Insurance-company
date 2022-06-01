package insuranceCompany.application.dao.contract;

import insuranceCompany.application.dao.Dao;
import insuranceCompany.application.domain.customer.Customer;
import insuranceCompany.application.domain.insurance.InsuranceType;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.domain.contract.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContractDao extends Dao {
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
            if (rs.next())
                switch (rs.getString("insurance_type")) {
                    case "HEALTH" -> {
                        HealthContract healthContract = (HealthContract) contract;
                        inputquery = "insert into health_contract (contract_id, height, weight, is_danger_activity, " +
                                "is_drinking, is_smoking, is_taking_drug, is_having_disease, is_driving, disease_detail) " +
                                "VALUES (%d, %d, %d, %d, %d, %d, %d, %d, %d, '%s');";
                        input = String.format(inputquery, contract.getId(), healthContract.getHeight(), healthContract.getWeight(),
                                healthContract.isDangerActivity()?1:0, healthContract.isDrinking()?1:0, healthContract.isSmoking()?1:0, healthContract.isSmoking()?1:0,
                                healthContract.isTakingDrug()?1:0, healthContract.isHavingDisease()?1:0, healthContract.isDriving()?1:0, healthContract.getDiseaseDetail());
                        super.create(input);
                    }
                    case "FIRE" -> {
                        FireContract fireContract = (FireContract) contract;
                        inputquery = "insert into fire_contract (contract_id, building_area, building_type, collateral_amount, is_actual_residence, is_self_owned)" +
                                "VALUES (%d, %d, '%s', %d, %d, %d);";
                        input = String.format(inputquery, contract.getId(), fireContract.getBuildingArea(), fireContract.getBuildingType().name(),
                                fireContract.getCollateralAmount(), fireContract.isActualResidence()?1:0, fireContract.isSelfOwned()?1:0);
                        super.create(input);
                    }
                    case "CAR" -> {
                        CarContract carContract = (CarContract) contract;
                        inputquery = "insert into fire_contract (contract_id, car_no, car_type, model_year, model_name, value)" +
                                "values (%d, '%s', '%s', %d, '%s', %d) ";
                        input = String.format(inputquery, contract.getId(), carContract.getCarNo(), carContract.getCarType().name(),
                                carContract.getModelYear(), carContract.getModelName(), carContract.getValue());
                        super.create(input);
                    }
                }
        } finally {
            close();
        }
    }

    public Contract read(int id) {
        String query = "SELECT *\n" +
                "         FROM contract c\n" +
                "LEFT JOIN car_contract cc\n" +
                "       ON c.contract_id = cc.contract_id\n" +
                "LEFT JOIN fire_contract fc\n" +
                "       ON c.contract_id = fc.contract_id\n" +
                "LEFT JOIN health_contract hc\n" +
                "       ON c.contract_id = hc.contract_id\n" +
                "LEFT JOIN insurance i\n" +
                "       ON c.insurance_id = i.insurance_id\n" +
                "WHERE c.contract_id = " + id;

        Contract contract = null;

        try {
            ResultSet rs = super.read(query);

            if (rs.next()) {

                InsuranceType insuranceType = InsuranceType.valueOf(rs.getString("insurance_type"));

                switch (insuranceType) {

                    case HEALTH -> contract = setHealthContract(rs);
                    case CAR -> contract = setCarContract(rs);
                    case FIRE -> contract = setBuildingContract(rs);
                }
                setContract(rs, contract);

            }
            super.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contract;
    }


    public ArrayList<Contract> readAllByInsuranceType(InsuranceType insuranceType) {
        ArrayList<Contract> contractList = new ArrayList<>();
        String query = "SELECT *\n" +
                "         FROM contract c\n" +
                "LEFT JOIN car_contract cc\n" +
                "       ON c.contract_id = cc.contract_id\n" +
                "LEFT JOIN fire_contract fc\n" +
                "       ON c.contract_id = fc.contract_id\n" +
                "LEFT JOIN health_contract hc\n" +
                "       ON c.contract_id = hc.contract_id\n" +
                "LEFT JOIN insurance i\n" +
                "       ON c.insurance_id = i.insurance_id\n" +
                "WHERE i.insurance_type = '" + insuranceType +"'" +
                    "AND c.condition_of_uw = 'WAIT' OR c.condition_of_uw = 'RE_AUDIT'";

        try {
            ResultSet rs = super.read(query);

            while (rs.next()) {
                Contract contract = null;

                switch (insuranceType) {

                    case HEALTH -> contract = setHealthContract(rs);
                    case CAR -> contract = setCarContract(rs);
                    case FIRE -> contract = setBuildingContract(rs);
                }
                setContract(rs,contract);
                contractList.add(contract);
            }
            super.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contractList;
    }

    public void update(Contract contract) {
        String query = "UPDATE contract \n" +
                "SET reason_of_uw = '" + contract.getReasonOfUw() +"',\n" +
                "condition_of_uw = '" + contract.getConditionOfUw() + "',\n" +
                "is_publish_stock = " + (contract.isPublishStock() ? 1 : 0) + "\n" +
                "WHERE contract_id = " + contract.getId();
        super.update(query);

    }

    public Contract setContract(ResultSet rs, Contract contract) {

        try {

            contract.setId(rs.getInt("contract_id"));
            contract.setReasonOfUw(rs.getString("reason_of_uw"));
            contract.setPaymentId(rs.getInt("payment_id"));
            contract.setInsuranceId(rs.getInt("insurance_id"));
            contract.setEmployeeId(rs.getInt("employee_id"));
            contract.setPremium(rs.getInt("premium"));
            contract.setPublishStock((rs.getInt("is_publish_stock") != 0));
            contract.setConditionOfUw(ConditionOfUw.valueOf(rs.getString("condition_of_uw")));
            contract.setCustomerId(rs.getInt("customer_id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contract;

    }

    public CarContract setCarContract(ResultSet rs) {
        CarContract carContract = new CarContract();

        try {
            carContract.setCarNo(rs.getString("car_no"));
            if (rs.getString("car_type") != null)
                carContract.setCarType(CarType.valueOf(rs.getString("car_type")));
            carContract.setModelYear(rs.getInt("model_year"));
            carContract.setModelName(rs.getString("name"));
            carContract.setValue(Long.valueOf(rs.getInt("value")));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carContract;
    }


    public FireContract setBuildingContract(ResultSet rs) {
        FireContract fireContract = new FireContract();

        try {
            fireContract.setBuildingArea(rs.getInt("building_area"));
            fireContract.setBuildingType(BuildingType.valueOf(rs.getString("building_type")));
            fireContract.setActualResidence((rs.getInt("is_actual_residence")) != 0);
            fireContract.setCollateralAmount(Long.valueOf(rs.getInt("collateral_amount")));
            fireContract.setSelfOwned((rs.getInt("is_self_owned")) != 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fireContract;

    }

    public HealthContract setHealthContract(ResultSet rs) {
        HealthContract healthContract = new HealthContract();

        try {
            healthContract.setDangerActivity((rs.getInt("is_danger_activity")) != 0);
            healthContract.setDiseaseDetail(rs.getString("disease_detail"));
            healthContract.setDrinking((rs.getInt("is_drinking")) != 0);
            healthContract.setHeight(rs.getInt("height"));
            healthContract.setDriving((rs.getInt("is_driving")) != 0);
            healthContract.setWeight(rs.getInt("weight"));
            healthContract.setTakingDrug((rs.getInt("is_taking_drug")) != 0);
            healthContract.setSmoking((rs.getInt("is_smoking")) != 0);
            healthContract.setHavingDisease((rs.getInt("is_having_disease")) != 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return healthContract;

    }

    public void delete(Customer customer) {

    }


    public List<Contract> findAllByCustomerId(int customerId) {
        String query = "select * from contract where customer_id = %d";
        String formattedQuery = String.format(query, customerId);

        ResultSet rs = super.read(formattedQuery);
        List<Contract> contracts = new ArrayList<>();
            try {
                while (rs.next()) {
                    Contract contract = new Contract();
                    contract.setId(rs.getInt("contract_id"));
                    contract.setReasonOfUw(rs.getString("reason_of_uw"));
                    contract.setPaymentId(rs.getInt("payment_id"));
                    contract.setInsuranceId(rs.getInt("insurance_id"));
                    contract.setEmployeeId(rs.getInt("employee_id"));
                    contract.setPremium(rs.getInt("premium"));
                    contract.setPublishStock((rs.getInt("is_publish_stock") != 0));
                    contract.setConditionOfUw(ConditionOfUw.valueOf(rs.getString("condition_of_uw")));
                    contract.setCustomerId(rs.getInt("customer_id"));
                    contracts.add(contract);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                close();
            }

        if (contracts.size() == 0)
            throw new MyIllegalArgumentException("해당 ID로 검색되는 계약이 존재하지 않습니다");
        return contracts;
    }

    public void updatePayment(int contractId, int paymentId) {
        String query = "update contract set payment_id = %d where contract_id = %d";
        String formattedQuery = String.format(query, paymentId, contractId);
        super.update(formattedQuery);
        close();
    }
}
