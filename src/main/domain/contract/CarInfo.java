package main.domain.contract;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class CarInfo {

	private String carNo;
	private CarType carType;
	private int modelYear;
	private String name;
	private String owner;
	private int value;

	public CarInfo(){

	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "자동차정보 {" +
				"자동차번호: '" + carNo + '\'' +
				", 차량유형: " + carType +
				", 연식: " + modelYear +
				", 차명: '" + name + '\'' +
				", 차주: '" + owner + '\'' +
				", 차량가액: " + value +
				'}';
	}
}