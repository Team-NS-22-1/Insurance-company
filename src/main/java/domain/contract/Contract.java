package domain.contract;

import domain.customer.Customer;
import domain.customer.CustomerListImpl;
import domain.payment.Payment;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class Contract {

	private CarInfo carInfo;
	private ConditionOfUw conditionOfUw;
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


	public Contract(){

	}

	public CarInfo getCarInfo() {
		return carInfo;
	}

	public Contract setCarInfo(CarInfo carInfo) {
		this.carInfo = carInfo;
		return this;
	}

	public int getCustomerId() {
		return customerId;
	}

	public Contract setCustomerId(int customerId) {
		this.customerId = customerId;
		return this;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public Contract setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
		return this;
	}

	public BuildingInfo getBuildingInfo() {
		return buildingInfo;
	}

	public Contract setBuildingInfo(BuildingInfo buildingInfo) {
		this.buildingInfo = buildingInfo;
		return this;
	}

	public HealthInfo getHealthInfo() {
		return healthInfo;
	}

	public Contract setHealthInfo(HealthInfo healthInfo) {
		this.healthInfo = healthInfo;
		return this;
	}

	public int getId() {
		return id;
	}

	public Contract setId(int id) {
		this.id = id;
		return this;
	}

	public int getInsuranceId() {
		return insuranceId;
	}

	public Contract setInsuranceId(int insuranceId) {
		this.insuranceId = insuranceId;
		return this;
	}

	public boolean isPublishStock() {
		return isPublishStock;
	}

	public Contract setPublishStock(boolean publishStock) {
		isPublishStock = publishStock;
		return this;
	}

	public Payment getPayment() {
		return payment;
	}

	public Contract setPayment(Payment payment) {
		this.payment = payment;
		return this;
	}

	public int getPremium() {
		return premium;
	}

	public Contract setPremium(int premium) {
		this.premium = premium;
		return this;
	}

	public String getReasonOfUw() {
		return reasonOfUw;
	}
	public Contract setReasonOfUw(String reasonOfUw) {
		this.reasonOfUw = reasonOfUw;
		return this;
	}

	public ConditionOfUw getConditionOfUw() {
		return conditionOfUw;
	}

	public Contract setConditionOfUw(ConditionOfUw conditionOfUw) {
		this.conditionOfUw = conditionOfUw;
		return this;
	}

	@Override
	public String toString() {
		String text =
		 "계약 정보 {" +
				 "계약 ID: " + id +
				", 인수심사상태: " + conditionOfUw +
				 ", 인수사유: '" + reasonOfUw + '\'' +
				 ", 증권발행여부: " + isPublishStock +
				 ", 보험료: " + premium +
				", 결제수단: " + payment +
				 ", 건강정보: " + healthInfo +
				 ", 자동차정보: " + carInfo +
				 ", 화재정보: " + buildingInfo + '\'' +
				'}';

		if (carInfo != null) text.concat(carInfo.toString());
		if (buildingInfo != null) text.concat(buildingInfo.toString());
		if (healthInfo != null) text.concat(healthInfo.toString());

		 return text;
	}

}