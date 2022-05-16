package main.domain.contract;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class CarInfo {

	private String carNo;
	private CarType carType;
	private String modelName;
	private int modelYear;
	private int value;

	public CarInfo(){

	}

	public String getCarNo() {
		return carNo;
	}

	public CarInfo setCarNo(String carNo) {
		this.carNo = carNo;
		return this;
	}

	public CarType getCarType() {
		return carType;
	}

	public CarInfo setCarType(CarType carType) {
		this.carType = carType;
		return this;
	}

	public String getModelName() {
		return modelName;
	}

	public CarInfo setModelName(String modelName) {
		this.modelName = modelName;
		return this;
	}

	public int getModelYear() {
		return modelYear;
	}

	public CarInfo setModelYear(int modelYear) {
		this.modelYear = modelYear;
		return this;
	}

	public int getValue() {
		return value;
	}

	public CarInfo setValue(int value) {
		this.value = value;
		return this;
	}

	@Override
	public String toString() {
		return "{" +
				"자동차번호: '" + carNo + '\'' +
				", 차량유형: " + carType +
				", 연식: " + modelYear +
				", 차명: '" + modelName + '\'' +
				", 차량가액: " + value +
				'}';
	}
}