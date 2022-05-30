package utility;

import dao.AccidentDao;
import dao.EmployeeDao;
import domain.accident.Accident;
import domain.accident.AccidentList;
import domain.employee.Employee;
import domain.employee.EmployeeList;

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

    public static Employee changeCompEmployee(EmployeeList employeeList, AccidentList accidentList, Employee employee) {
        employeeList = new EmployeeDao();
        List<Employee> compEmployees = employeeList.readAllCompEmployee();
        compEmployees.remove(employee);
        int min = Integer.MAX_VALUE;
        int minId = 0;
        accidentList = new AccidentDao();
        for (Employee compEmployee : compEmployees) {
            List<Accident> accidents = accidentList.readAllByEmployeeId(compEmployee.getId());
            if (min > accidents.size()) {
                min = accidents.size();
                minId = compEmployee.getId();
            }
        }
        employeeList = new EmployeeDao();
        return employeeList.read(minId);
    }

    public static Employee assignCompEmployee(EmployeeList employeeList, AccidentList accidentList) {
        employeeList = new EmployeeDao();
        List<Employee> compEmployees = employeeList.readAllCompEmployee();
        int min = Integer.MAX_VALUE;
        int minId = 0;

        for (Employee compEmployee : compEmployees) {
            accidentList = new AccidentDao();
            List<Accident> accidents = accidentList.readAllByEmployeeId(compEmployee.getId());
            if (min > accidents.size()) {
                min = accidents.size();
                minId = compEmployee.getId();
            }
        }
        employeeList = new EmployeeDao();
        return employeeList.read(minId);
    }
}
