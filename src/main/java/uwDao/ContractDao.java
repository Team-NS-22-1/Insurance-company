package uwDao;

import domain.contract.*;
import domain.insurance.DevInfo;
import domain.insurance.InsuranceType;
import domain.insurance.SalesAuthState;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ContractDao extends Dao {

    public ContractDao() {
        super.connect();
    }


    public void create(Contract contract) {


    }

    public void update(Contract contract) {
        String query = "UPDATE contract \n" +
                "SET reason_of_uw = '" + contract.getReasonOfUw() +"',\n" +
                "condition_of_uw = '" + contract.getConditionOfUw() + "',\n" +
                "is_publish_stock = " + (contract.isPublishStock() ? 1 : 0) + "\n" +
                "WHERE contract_id = " + contract.getId();
        String[] columnNames = {"reason_of_uw", "condition_of_uw", "is_publish_stock"};
        super.update(query, columnNames);

    }

    public Contract read(int id) {
        String query = "SELECT *\n" +
                "         FROM contract c\n" +
                "LEFT JOIN car_contract cc\n" +
                "       ON c.contract_id = cc.contract_id\n" +
                "LEFT JOIN building_contract bc\n" +
                "       ON c.contract_id = bc.contract_id\n" +
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
            super.close(rs);
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
                "LEFT JOIN building_contract bc\n" +
                "       ON c.contract_id = bc.contract_id\n" +
                "LEFT JOIN health_contract hc\n" +
                "       ON c.contract_id = hc.contract_id\n" +
                "LEFT JOIN insurance i\n" +
                "       ON c.insurance_id = i.insurance_id\n" +
                "WHERE i.insurance_type = '" + insuranceType +"'";

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
            super.close(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contractList;
    }

    public Contract setContract(ResultSet rs, Contract contract) {

        try {

            contract.setId(rs.getInt("contract_id"));
            contract.setReasonOfUw(rs.getString("reason_of_uw"));
            //contract.setPayment(rs.getInt("payment_id"));
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
            carContract.setOwner(rs.getString("owner"));
            carContract.setValue(rs.getInt("value"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carContract;
    }

    public BuildingContract setBuildingContract(ResultSet rs) {
        BuildingContract buildingContract = new BuildingContract();

        try {
            buildingContract.setBuildingArea(rs.getInt("building_area"));
            buildingContract.setBuildingType(BuildingType.valueOf(rs.getString("building_type")));
            buildingContract.setActualResidence((rs.getInt("is_actual_residence")) != 0);
            buildingContract.setCollateralAmount(rs.getInt("collateral_amount"));
            buildingContract.setSelfOwned((rs.getInt("is_self_owned")) != 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buildingContract;

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

}