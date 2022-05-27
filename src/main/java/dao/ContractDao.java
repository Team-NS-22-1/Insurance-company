package dao;

import domain.contract.*;
import domain.insurance.Insurance;
import domain.insurance.InsuranceType;
import kr.dogfoot.hwplib.object.bodytext.control.ctrlheader.CtrlHeaderHeader;


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
        String query = "";
        super.update(query);
    }

    public Contract read(int id) {
        String query = "SELECT * " +
                        "FROM contract c" +
                        "LEFT JOIN payment p" +
                        "ON c.payment_id = p.payment_id" +
                        "WHERE contract_id = " + id;

        Contract contract = new Contract();

        try {
            ResultSet rs = super.read(query);

            if (rs.next()) {
                contract.setId(rs.getInt("contract_id"));
                contract.setReasonOfUw(rs.getString("reason_of_uw"));
                //contract.setPayment(rs.getInt("payment_id"));
                contract.setInsuranceId(rs.getInt("insurance_id"));
                contract.setEmployeeId(rs.getInt("employee_id"));
                contract.setPremium(rs.getInt("premium"));
                contract.setPublishStock((rs.getInt("is_publish_stock") != 0));
                contract.setConditionOfUw(Enum.valueOf(ConditionOfUw.class, rs.getString("condition_of_uw")));
                contract.setCustomerId(rs.getInt("customer_id"));
            }
        } catch (SQLException e) {

        }

        return contract;
    }


    public ArrayList<Contract> readAll() {
        ArrayList<Contract> contractList = new ArrayList<>();
        String query = "";
        Contract contract = new Contract();
        CarInfo carInfo = null;
        BuildingInfo buildingInfo = null;
        HealthInfo healthInfo = null;

        try {
            ResultSet rs = super.read(query);

            if (rs.next()) {
                carInfo.setCarNo(rs.getString("car_no"));
                //carInfo.setCarType(rs.getString("car_type"));
                carInfo.setModelYear(rs.getInt("model_year"));
                carInfo.setModelName(rs.getString("name"));
                //carInfo.set(rs.getString("owner"));
                carInfo.setValue(rs.getInt("value"));
                contract.setCarInfo(carInfo);

                contract.setId(rs.getInt("contract_id"));
                //contract.setId(rs.getInt("reason_of_uw"));
                //contract.(rs.getInt("payment_id"));
                contract.setInsuranceId(rs.getInt("insurance_id"));
                contract.setEmployeeId(rs.getInt("employee_id"));
                contract.setPremium(rs.getInt("premium"));
                //contract.setPublishStock(rs.getInt("is_publish_stock"));
                //contract.(rs.getInt("condition_of_uw"));
                contract.setCustomerId(rs.getInt("customer_id"));
                contractList.add(contract);
            }








        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contractList;
    }


}