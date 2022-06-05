package insuranceCompany.application.domain.dao.contract;


import insuranceCompany.application.domain.dao.CrudInterface;
import insuranceCompany.application.domain.contract.Contract;
import insuranceCompany.application.viewlogic.dto.contractDto.ContractwithTypeDto;

import java.util.List;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:58
 */
public interface ContractDao extends CrudInterface<Contract> {

    List<ContractwithTypeDto> findAllContractWithTypeByCustomerId(int customerId);
    List<Contract> findAllByCustomerId(int customerId);
    void updatePayment(int contractId, int paymentId);
}