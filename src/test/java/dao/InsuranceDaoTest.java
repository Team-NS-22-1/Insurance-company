package dao;

import insuranceCompany.application.dao.insurance.InsuranceDaoImpl;
import insuranceCompany.application.domain.insurance.Insurance;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

class InsuranceDaoTest {

    @Test
    void create() {

    }

    @Test
    void read() {
        Insurance insurance = new InsuranceDaoImpl().read(1);
        System.out.println(insurance.print());
    }

    @Test
    void readByEmployeeId() {
        ArrayList<Insurance> insurances = new InsuranceDaoImpl().readByEmployeeId(1);
        for(Insurance insurance : insurances)
            System.out.println(insurance.print());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}