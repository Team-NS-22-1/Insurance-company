import domain.insurance.Insurance;
import domain.insurance.InsuranceList;

import java.sql.*;

public class InsuranceDao extends Dao {

    public InsuranceDao() {
        super.connet();
    }

    public void create(Insurance insurance) {
        String query = "insert into insuracne (user_name, user_phone) value (" +
                "'" + insurance.getName() + "', ";

        super.create(query);
    }

    public void updateByName(String insuranceName) {

    }

    public InsuranceList retriveAll() {
        return null;
    }

    // main
    //InsuranceDao insuranceDao = new InsuranceDao();
    //insuranceDao.create(insurance)


}