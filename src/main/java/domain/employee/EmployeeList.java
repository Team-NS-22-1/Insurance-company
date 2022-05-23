package domain.employee;


import domain.ifs.CrudInterface;

import java.util.List;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:59
 */
public interface EmployeeList extends CrudInterface<Employee> {

    public List<Employee> readAllCompEmployee();
}