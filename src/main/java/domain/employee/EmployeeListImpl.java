package domain.employee;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오후 4:39:00
 */
public class EmployeeListImpl implements EmployeeList {

	private static HashMap<Integer, Employee> employeeList = new HashMap<>();
	private static int id = 0;

	public EmployeeListImpl(){
	}

	@Override
	public void create(Employee employee) {
		employeeList.put(employee.setId(++id).getId(), employee);
	}

	@Override
	public Employee read(int id) {
		Employee employee = employeeList.get(id);
		if(employee != null) return employee;
		else return null;
	}

	public ArrayList<Employee> readAll() {
		return new ArrayList<>(employeeList.values());
	}

	@Override
	public boolean delete(int id) {
		Employee employee = employeeList.remove(id);
		if(employee != null) return true;
		else return false;
	}

	@Override
	public List<Employee> readAllCompEmployee() {
		List<Employee> allEmployee = new ArrayList<>(employeeList.values());
		return allEmployee.stream()
				.filter(e -> e.getDepartment() == Department.COMP)
				.collect(Collectors.toList());
	}
}