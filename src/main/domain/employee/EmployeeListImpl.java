package main.domain.employee;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:00
 */
public class EmployeeListImpl implements EmployeeList {

	private HashMap<Integer, Employee> employeeList = new HashMap<>();
	private static int id = 0;

	public EmployeeListImpl(){
	}

	@Override
	public boolean create(Employee employee) {
		if(this.employeeList.put(employee.setId(++id).getId(), employee) != null) return true;
		else return false;
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