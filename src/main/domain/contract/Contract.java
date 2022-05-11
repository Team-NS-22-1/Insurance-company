package main.domain.contract;

import main.domain.customer.Payment;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class Contract {

	private CarInfo carInfo;
	//private enum conditionOfUw;
	private int customerId;
	private int employeeId;
	private BuildingInfo buildingInfo;
	private HealthInfo healthInfo;
	private int id;
	private int insuranceId;
	private boolean isPublishStock;
	private Payment payment;
	private int premium;
	private String reasonOfUw;
	public BuildingInfo m_BuildingInfo;
	public CarInfo m_CarInfo;
	public HealthInfo m_HealthInfo;

	public Contract(){

	}


}