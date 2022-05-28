package dao;

import domain.insurance.Insurance;

import java.sql.ResultSet;

public class InsuranceDao extends Dao {

    public InsuranceDao() {
        super.connect();
    }

    public void create(Insurance insurance) {
        String query = "";

        super.create(query);
    }

    public ResultSet read(Insurance insurance) {

        return null;
    }

    public void update(Insurance insurance) {

    }

    public void delete(Insurance insurance) {

    }
}
