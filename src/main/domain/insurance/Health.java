package main.domain.insurance;


import main.domain.customer.Customer;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class Health extends Insuarance {

	private boolean riskPremiumCriterion;
	private int targetAge;
	private boolean targetSex;

	public Health(){

	}

	@Override
	public float calcuationRatio(Customer customer) {
		return 0;
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}