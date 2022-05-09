package main.domain.insurance;


import main.domain.customer.Customer;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class Car extends Insuarance {

	private int targetAge;
	private int valueCriterion;

	public Car(){

	}

	@Override
	public float calcuationRatio(Customer customer) {
		return 0;
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}