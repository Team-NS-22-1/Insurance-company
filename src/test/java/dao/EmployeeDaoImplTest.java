package dao;

import insuranceCompany.application.dao.employee.EmployeeDaoImpl;
import insuranceCompany.application.domain.employee.Department;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.employee.Position;
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
class EmployeeDaoImplTest {

    EmployeeDaoImpl dao = new EmployeeDaoImpl();

    @Test
    void readAllCompEmployee() {
        dao = new EmployeeDaoImpl();
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

        dao = new EmployeeDaoImpl();
        dao.create(employee);

        dao = new EmployeeDaoImpl();
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