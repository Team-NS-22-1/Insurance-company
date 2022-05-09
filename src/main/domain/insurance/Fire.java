package main.domain.insurance;


import main.domain.customer.Customer;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class Fire extends Insuarance {

	//private enum buildingType;
	private int collateralAmount;
	


	public Fire(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	public float calcuationRatio(Customer customer) {
		//if (customer.getHousePrice() > 1000000000) return 1.1f;
		//else if (customer.getHousePrice() > 100000000) return 1.0f;
		return 0.9f;
		
	}
	
	

}