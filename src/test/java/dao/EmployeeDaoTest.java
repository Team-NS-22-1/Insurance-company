package dao;

import domain.employee.Department;
import domain.employee.Employee;
import domain.employee.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  dao
 * fileName : EmployeeDaoTest
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
class EmployeeDaoTest {

    EmployeeDao dao = new EmployeeDao();

    @Test
    void readAllCompEmployee() {
        dao = new EmployeeDao();
        List<Employee> compEmployee = dao.readAllCompEmployee();
        for (Employee employee : compEmployee) {
            System.out.println(employee.print());
        }
        assertEquals(compEmployee.size(),2);
    }

    @Test
    void readAll() {
    }

    @Test
    void create() {
        Employee employee = new Employee();
        employee.setPhone("test1")
                .setName("tester1")
                .setDepartment(Department.COMP)
                .setPosition(Position.DEPTMANAGER);

        dao = new EmployeeDao();
        dao.create(employee);

        dao = new EmployeeDao();
        Employee read = dao.read(employee.getId());
        System.out.println(read.print());
        Assertions.assertEquals(read.getId(),employee.getId());
    }

    @Test
    void read() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}