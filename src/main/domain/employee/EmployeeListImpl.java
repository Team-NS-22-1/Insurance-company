package main.domain.employee;


import java.io.*;
import java.util.HashMap;

/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오후 4:39:00
 */
public class EmployeeListImpl implements EmployeeList {

	private HashMap<Integer, Employee> employeeList = new HashMap<>();
	private static int id = 0;

	public EmployeeListImpl(){
	}

	@Override
	public void create(Employee employee) {
		this.employeeList.put(employee.setId(++id).getId(), employee);
	}

	@Override
	public Employee read(int id) {
		Employee employee = this.employeeList.get(id);
		if(employee != null) return employee;
		else return null;
	}

	@Override
	public boolean delete(int id) {
		Employee employee = this.employeeList.remove(id);
		if(employee != null) return true;
		else return false;
	}

}