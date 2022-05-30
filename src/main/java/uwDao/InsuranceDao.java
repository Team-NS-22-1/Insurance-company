package uwDao;

import domain.contract.ConditionOfUw;
import domain.contract.Contract;
import domain.insurance.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class InsuranceDao extends Dao {

    public InsuranceDao() {
        super.connect();
    }


    public void create(Insurance insurance) {

    }

    public Insurance read(int id) {
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
                "LEFT JOIN dev_info di\n" +
                "ON i.insurance_id = di.insurance_id\n" +
                "LEFT JOIN sales_auth_file saf\n" +
                "ON i.insurance_id = saf.insurance_id\n" +
                "WHERE i.insurance_id = " + id;

        String grantQuery = "SELECT *\n"+
                "FROM insurance i\n" +
                "LEFT JOIN guarantee g\n" +
                "ON i.insurance_id = g.insurance_id\n" +
                "WHERE i.insurance_id = " + id;

        Insurance insurance = new Insurance();
        DevInfo devInfo = new DevInfo();
        SalesAuthFile salesAuthFile = new SalesAuthFile();
        ArrayList<Guarantee> guaranteeList = new ArrayList<>();

        try {
            ResultSet rs = super.read(query);

            if (rs.next()) {
                insurance.setId(rs.getInt("insurance_id"));
                insurance.setName(rs.getString("name"));
                insurance.setDescription(rs.getString("description"));
                insurance.setContractPeriod(rs.getInt("contract_period"));
                insurance.setPaymentPeriod(rs.getInt("payment_period"));
                insurance.setInsuranceType(InsuranceType.valueOf(rs.getString("insurance_type")));

                devInfo.setInsuranceId(rs.getInt("insurance_id"));
                devInfo.setId(rs.getInt("dev_info_id"));
                devInfo.setEmployeeId(rs.getInt("employee_id"));
                devInfo.setSalesStartDate(rs.getDate("sales_start_date").toLocalDate());
                devInfo.setDevDate(rs.getDate("dev_date").toLocalDate());
                devInfo.setSalesAuthState(SalesAuthState.valueOf(rs.getString("sales_auth_state")));
                insurance.setDevInfo(devInfo);

                salesAuthFile.setInsuranceId(rs.getInt("insurance_id"));
                salesAuthFile.setId(rs.getInt("sales_auth_files_id"));
                salesAuthFile.setProdDeclaration(rs.getString("prod_declaration"));
                salesAuthFile.setfSSOfficialDoc(rs.getString("fss_official_doc"));
                salesAuthFile.setIsoVerification(rs.getString("iso_verification"));
                salesAuthFile.setSrActuaryVerification(rs.getString("sr_actuary_verification"));
                insurance.setSalesAuthFile(salesAuthFile);
            }
            if (rs != null) rs.close();

            ResultSet grantRs = super.read(grantQuery);

            while (grantRs.next()) {
                Guarantee guarantee = new Guarantee();
                guarantee.setId(grantRs.getInt("guarantee_id"));
                guarantee.setInsuranceId(grantRs.getInt("insurance_id"));
                guarantee.setName(grantRs.getString("name"));
                guarantee.setDescription(grantRs.getString("description"));
                guarantee.setGuaranteeAmount(Long.valueOf(grantRs.getInt("amount")));
                guaranteeList.add(guarantee);

            }
            insurance.setGuarantee(guaranteeList);
            super.close(grantRs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return insurance;
    }



}