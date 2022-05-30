package dao;

import domain.contract.ConditionOfUw;
import domain.contract.Contract;
import domain.contract.ContractList;
import domain.customer.Customer;
import exception.MyIllegalArgumentException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContractDao extends Dao{
    public ContractDao() {
        super.connect();
    }

    public void create(Contract contract) {
        String query = "insert into contract (insurance_id, customer_id, employee_id, premium, is_publish_stock, condition_of_uw) values (%d, %d, %d, %d, %b, %s);";
        String contractQuery = String.format(query, contract.getInsuranceId(), contract.getCustomerId(), contract.getEmployeeId(), contract.getPremium(), false, ConditionOfUw.WAIT);
        int id = super.create(contractQuery);
        contract.setId(id);

        close();
    }

    public Contract read(int id) throws SQLException {
        Contract contract = new Contract();
        String query = "select * from customer where customer_id = " + id;
        ResultSet rs = super.read(query);
        if (rs.next()) {
            contract.setId(rs.getInt("contract_id"));
            contract.setReasonOfUw(rs.getString("reason_of_uw"));
            contract.setPaymentId(rs.getInt("payment_id"));
            contract.setInsuranceId(rs.getInt("insurance_id"));
            contract.setCustomerId(rs.getInt("customer_id"));
            contract.setEmployeeId(rs.getInt("employee_id"));
            contract.setPremium(rs.getInt("premium"));
            contract.setPublishStock(rs.getBoolean("is_publish_stock"));
            switch (rs.getString("condition_of_uw")) {
                case "WAIT":
                    contract.setConditionOfUw(ConditionOfUw.WAIT);
                    break;
                case "APPROVAL":
                    contract.setConditionOfUw(ConditionOfUw.APPROVAL);
                    break;
                case "RE_AUDIT":
                    contract.setConditionOfUw(ConditionOfUw.RE_AUDIT);
                    break;
                case "REFUSE":
                    contract.setConditionOfUw(ConditionOfUw.REFUSE);
                    break;
            }
        }
        return contract;
    }

    public void update(Customer customer) {

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
            }

        if (contracts.size() == 0)
            throw new MyIllegalArgumentException("해당 ID로 검색되는 계약이 존재하지 않습니다");
        return contracts;
    }

    public void updatePayment(int contractId, int paymentId) {
        String query = "update contract set payment_id = %d where contract_id = %d";
        String formattedQuery = String.format(query, paymentId, contractId);
        super.update(formattedQuery);
    }
}
