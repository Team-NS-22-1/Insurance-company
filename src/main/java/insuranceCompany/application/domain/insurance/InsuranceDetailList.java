package insuranceCompany.application.domain.insurance;

import insuranceCompany.application.dao.CrudInterface;

import java.util.ArrayList;

public interface InsuranceDetailList extends CrudInterface<InsuranceDetail> {
    ArrayList<InsuranceDetail> readByInsuranceId(int id);
}
