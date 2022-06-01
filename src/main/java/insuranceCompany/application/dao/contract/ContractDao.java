package insuranceCompany.application.dao.contract;


import insuranceCompany.application.dao.CrudInterface;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.domain.insurance.Insurance;
import insuranceCompany.application.domain.insurance.InsuranceType;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:01
 */
public interface ContractDao extends CrudInterface<Contract> {
    ArrayList<Contract> readAllByInsuranceType(InsuranceType insuranceType);
    boolean update(Contract contract);
}