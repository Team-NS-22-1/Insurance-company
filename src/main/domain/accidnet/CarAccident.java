package main.domain.accidnet;




/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class CarAccident extends Accident {

	private String carNo;
	private double errorRate;
	private boolean isRequestOnSite;
	private String opposingDriverPhone;
	private String placeAddress;

	public CarAccident(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}