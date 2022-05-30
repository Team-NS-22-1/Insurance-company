package dao;

import domain.contract.ConditionOfUw;
import domain.contract.Contract;
import domain.customer.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

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
//            contract.setPayment(PaymentDao.read(rs.getInt("payment_id")));
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
}
