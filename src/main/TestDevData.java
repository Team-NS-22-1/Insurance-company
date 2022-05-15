package main;

import main.domain.employee.Department;
import main.domain.employee.Employee;
import main.domain.employee.EmployeeListImpl;
import main.domain.employee.Position;

public class TestDevData {
    private EmployeeListImpl employeeList;
    private static int id = 0;

    public TestDevData(EmployeeListImpl employeeList) {
        this.employeeList = employeeList;
    }

    public EmployeeListImpl createEmployee() {
        this.employeeList.create(new Employee().setId(++id)
                .setName("테스터 직원1")
                .setPhone("010-1234-1234")
                .setDepartment(Department.DEV)
                .setPosition(Position.TEAMLEADER));
        this.employeeList.create(new Employee().setId(++id)
                .setName("테스터 직원2")
                .setPhone("010-2345-2345")
                .setDepartment(Department.SALES)
                .setPosition(Position.MEMBER));
        this.employeeList.create(new Employee().setId(++id)
                .setName("테스터 직원3")
                .setPhone("010-3456-3456")
                .setDepartment(Department.COMP)
                .setPosition(Position.MEMBER));
        this.employeeList.create(new Employee().setId(++id)
                .setName("테스터 직원4")
                .setPhone("010-4567-4567")
                .setDepartment(Department.UW)
                .setPosition(Position.DEPTMANAGER));
        this.employeeList.create(new Employee().setId(++id)
                .setName("테스터 직원5")
                .setPhone("010-5678-5678")
                .setDepartment(Department.EXEC)
                .setPosition(Position.CEO));
        return this.employeeList;
    }

}