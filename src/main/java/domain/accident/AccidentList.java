package domain.accident;


import ifs.CrudInterface;

import java.util.List;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:56
 */
public interface AccidentList extends CrudInterface<Accident> {

    List<Accident> readAllByCustomerId(int customerId);

    List<Accident> readAllByEmployeeId(int employeeid);
    void update(Accident accident);
}