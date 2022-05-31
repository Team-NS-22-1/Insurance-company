package domain.insurance;

import dao.CrudInterface;

import java.util.ArrayList;

public interface InsuranceDetailList extends CrudInterface<InsuranceDetail> {
    ArrayList<InsuranceDetail> readByInsuranceId(int id);
}
