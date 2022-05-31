package insuranceCompany.application.domain.customer;



import insuranceCompany.application.dao.customer.CustomerDao;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:59
 */
public class CustomerListImpl implements CustomerDao {
	private static Map<Integer, Customer> customerList = new HashMap<>();
	private static int idSequence = 0;
	public CustomerListImpl(){
	}

	@Override
	public void create(Customer customer) {
		customer.setId(++idSequence);
		customerList.put(customer.getId(), customer);
	}

	@Override
	public Customer read(int id) {
		Customer customer = customerList.get(id);
		if (customer == null) {
			throw new MyIllegalArgumentException(id + "에 해당하는 고객정보가 존재하지 않습니다.");
		}
		return customer;
	}

	@Override
	public boolean update(int id) {
		return false;
	}

	@Override
	public boolean delete(int id) {
		Customer remove = customerList.remove(id);
		return remove != null;
	}
}