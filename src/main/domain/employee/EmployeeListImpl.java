package main.domain.employee;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:00
 */
public class EmployeeListImpl implements EmployeeList {

	private ArrayList<Employee> employeeList = new ArrayList<>();

	public EmployeeListImpl(){
	}

	@Override
	public boolean create(Employee employee) {
		if(this.employeeList.add(employee)) return true;
		else return false;
	}

	@Override
	public Employee read(int id) {
		for(Employee employee : this.employeeList)
			if(employee.getId() == id)
				return employee;
		return null;
	}

	@Override
	public boolean delete(int id) {
		boolean delete = false;
		Iterator it = this.employeeList.iterator();
		while(it.hasNext()){
			Employee employee = (Employee) it.next();
			if(employee.getId() == id) {
				it.remove();
				delete = true;
				break;
			}
		}
		return delete;
	}
}