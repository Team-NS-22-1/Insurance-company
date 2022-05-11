package main.domain.customer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:59
 */
public class CustomerListImpl implements CustomerList {

<<<<<<< HEAD
	private static ArrayList<Customer> customerList;
	public Customer m_Customer;
=======
	private static Map<Integer, Customer> customerList = new HashMap<>();
	private static int idSequence = 0;
>>>>>>> main

	public CustomerListImpl(){
	}

	@Override
	public boolean create(Customer customer) {
		customer.setId(++idSequence);
		customerList.put(customer.getId(), customer);
		return true;
	}

	@Override
	public Customer read(int id) {
		Customer customer = customerList.get(id);
		if (customer == null) {
			throw new IllegalArgumentException("id");
		}
		return customer;
	}

	@Override
	public boolean delete(int id) {
		Customer remove = customerList.remove(id);
		return remove != null;
	}
}