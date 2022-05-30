package dao;

import domain.insurance.Insurance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class InsuranceDaoTest {

    @Test
    void create() {
    }

    @Test
    void read() {
        Insurance insurance = new InsuranceDao().read(1);
        System.out.println(insurance.print());
    }

    @Test
    void readByEmployeeId() {
        ArrayList<Insurance> insurances = new InsuranceDao().readByEmployeeId(1);
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