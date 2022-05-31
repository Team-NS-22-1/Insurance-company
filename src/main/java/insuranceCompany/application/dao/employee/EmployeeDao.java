package insuranceCompany.application.dao.employee;

import insuranceCompany.application.dao.Dao;
import insuranceCompany.application.domain.employee.Department;
import insuranceCompany.application.domain.employee.Employee;
import insuranceCompany.application.domain.employee.EmployeeList;
import insuranceCompany.application.domain.employee.Position;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName :  dao
 * fileName : EmployeeDao
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
public class EmployeeDao extends Dao implements EmployeeList {

    public EmployeeDao() {
        super.connect();
    }

    @Override
    public List<Employee> readAllCompEmployee() {
        String query = "select * from employee where department = '%s'";
        String formattedQuery = String.format(query, Department.COMP.name());
        ResultSet rs = super.read(formattedQuery);
        List<Employee> compEmployees = new ArrayList<>();

            try {
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("employee_id"))
                            .setName(rs.getString("name"))
                            .setPhone(rs.getString("phone"))
                            .setDepartment(Department.valueOf(rs.getString("department").toUpperCase()))
                            .setPosition(Position.valueOf(rs.getString("position").toUpperCase()));
                    compEmployees.add(employee);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                close();
            }
        return compEmployees;
    }

    public ArrayList<Employee> readAllSalesEmployee() {
        String query = "select * from employee where department = '%s'";
        String formattedQuery = String.format(query, Department.SALES.name());
        ResultSet rs = super.read(formattedQuery);
        ArrayList<Employee> salesEmployees = new ArrayList<>();

        try {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("employee_id"))
                        .setName(rs.getString("name"))
                        .setPhone(rs.getString("phone"))
                        .setDepartment(Department.valueOf(rs.getString("department").toUpperCase()))
                        .setPosition(Position.valueOf(rs.getString("position").toUpperCase()));
                salesEmployees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return salesEmployees;
    }

    public ArrayList<Employee> readAllDev() {
        ArrayList<Employee> devEmployees = new ArrayList<>();
        String queryFormat =
                "SELECT * FROM employee WHERE department = '%s';";
        String query =
                String.format(queryFormat, Department.DEV.name());
        super.read(query);
        try {
            while(resultSet.next()){
                devEmployees.add(
                        new Employee().setId(resultSet.getInt("employee_id"))
                                .setName(resultSet.getString("name"))
                                .setPhone(resultSet.getString("phone"))
                                .setDepartment(Department.valueOf(resultSet.getString("department").toUpperCase()))
                                .setPosition(Position.valueOf(resultSet.getString("position").toUpperCase()))
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return devEmployees;
    }

    @Override
    public ArrayList<Employee> readAll() {
        return null;
    }

    @Override
    public void create(Employee employee) {

        String query = "insert into employee (name, phone, department, position) values " +
                "('%s', '%s', '%s', '%s')";
        String formattedQuery = String.format(query, employee.getName(),
                employee.getPhone(), employee.getDepartment().name(),
                employee.getPosition().name());
        int id = super.create(formattedQuery);
        employee.setId(id);
        close();
    }

    @Override
    public Employee read(int id) {
        String query = "select * from employee where employee_id = %d";
        String formattedQuery = String.format(query, id);
        ResultSet rs = super.read(formattedQuery);
        Employee employee = null;
        try {
            if (rs.next()) {
                employee = new Employee();
                employee.setId(rs.getInt("employee_id"))
                        .setName(rs.getString("name"))
                        .setPhone(rs.getString("phone"))
                        .setDepartment(Department.valueOf(rs.getString("department").toUpperCase()))
                        .setPosition(Position.valueOf(rs.getString("position").toUpperCase()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return employee;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

}
