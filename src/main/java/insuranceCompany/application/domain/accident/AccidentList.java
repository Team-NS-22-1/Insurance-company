package insuranceCompany.application.domain.accident;


import insuranceCompany.application.dao.CrudInterface;

import java.util.List;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:56
 */
public interface AccidentList extends CrudInterface<Accident> {

    List<Accident> readAllByCustomerId(int customerId);

    List<Accident> readAllByEmployeeId(int employeeid);
    void updateLossReserve(Accident accident);

    void updateLossReserveAndErrorRate(Accident accident);
    void updateCompEmployeeId(Accident accident);
}