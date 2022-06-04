package insuranceCompany.application.global.utility;

import insuranceCompany.application.dao.accident.AccidentDaoImpl;
import insuranceCompany.application.dao.employee.EmployeeDaoImpl;
import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.dao.accident.AccidentDao;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.dao.employee.EmployeeDao;
import insuranceCompany.application.global.exception.NoResultantException;

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
    private CompAssignUtil() {

    }

    public static Employee changeCompEmployee(Employee employee) {
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        List<Employee> compEmployees = employeeDao.readAllCompEmployee();
        compEmployees.remove(employee);
        return getEmployee(compEmployees);
    }

    public static Employee assignCompEmployee() {

        EmployeeDao employeeDao = new EmployeeDaoImpl();
        List<Employee> compEmployees = employeeDao.readAllCompEmployee();
        return getEmployee(compEmployees);

    }

    private static Employee getEmployee(List<Employee> compEmployees) {
        EmployeeDao employeeDao;
        int min = Integer.MAX_VALUE;
        int minId = 0;
        AccidentDao accidentDao = new AccidentDaoImpl();
        for (Employee compEmployee : compEmployees) {
            try{
                List<Accident> accidents = accidentDao.readAllByEmployeeId(compEmployee.getId());
                if (min > accidents.size()) {
                    min = accidents.size();
                    minId = compEmployee.getId();
                }
            }catch (NoResultantException e) {
                System.out.print("");
            }
        }
        if(minId == 0)
            minId = compEmployees.get(0).getId();

        employeeDao = new EmployeeDaoImpl();
        return employeeDao.read(minId);
    }


}
