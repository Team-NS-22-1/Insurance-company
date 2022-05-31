package insuranceCompany.application.domain.contract;


import insuranceCompany.application.dao.CrudInterface;

import java.util.List;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:58
 */
public interface ContractList extends CrudInterface<Contract> {

    List<Contract> findAllByCustomerId(int customerId);
    void updatePayment(int contractId, int paymentId);
}