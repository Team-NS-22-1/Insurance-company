package insuranceCompany.application.global.utility;

import insuranceCompany.application.dao.accident.AccidentDaoImpl;
import insuranceCompany.application.dao.employee.EmployeeDaoImpl;
import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.dao.employee.EmployeeDao;

import java.util.List;

/**
 * packageName :  utility
 * fileName : CompAssignUtil
 * author :  규현
 * date : 2022-05-22
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-22                규현             최초 생성
 */
public class CompAssignUtil {

    public static Employee changeCompEmployee(EmployeeDao employeeDao, AccidentDao accidentDao, Employee employee) {
        employeeDao = new EmployeeDaoImpl();
        List<Employee> compEmployees = employeeDao.readAllCompEmployee();
        compEmployees.remove(employee);
        int min = Integer.MAX_VALUE;
        int minId = 0;
        accidentDao = new AccidentDaoImpl();
        for (Employee compEmployee : compEmployees) {
            List<Accident> accidents = accidentDao.readAllByEmployeeId(compEmployee.getId());
            if (min > accidents.size()) {
                min = accidents.size();
                minId = compEmployee.getId();
            }
        }
        employeeDao = new EmployeeDaoImpl();
        return employeeDao.read(minId);
    }

    public static Employee assignCompEmployee(EmployeeDao employeeDao, AccidentDao accidentDao) {
        employeeDao = new EmployeeDaoImpl();
        List<Employee> compEmployees = employeeDao.readAllCompEmployee();
        int min = Integer.MAX_VALUE;
        int minId = 0;

        for (Employee compEmployee : compEmployees) {
            accidentDao = new AccidentDaoImpl();
            List<Accident> accidents = accidentDao.readAllByEmployeeId(compEmployee.getId());
            if (min > accidents.size()) {
                min = accidents.size();
                minId = compEmployee.getId();
            }
        }
        employeeDao = new EmployeeDaoImpl();
        return employeeDao.read(minId);
    }
}
