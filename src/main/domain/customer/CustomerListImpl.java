package main.domain.customer;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:59
 */
public class CustomerListImpl implements CustomerList {

	private ArrayList<Customer> customerList = new ArrayList<>();

	public CustomerListImpl(){
	}

	@Override
	public boolean create(Customer customer) {
		if(this.customerList.add(customer)) return true;
		return false;
	}

	@Override
	public Customer read(int id) {
		for(Customer customer : this.customerList)
			if(customer.getId() == id)
				return customer;
		return null;
	}

	@Override
	public boolean delete(int id) {
		boolean delete = false;
		Iterator it = this.customerList.iterator();
		while(it.hasNext()){
			Customer customer = (Customer) it.next();
			if(customer.getId() == id) {
				it.remove();
				delete = true;
				break;
			}
		}
		return delete;
	}
}