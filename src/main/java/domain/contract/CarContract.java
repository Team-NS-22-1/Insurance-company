package domain.contract;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class CarContract extends Contract{

	private String carNo;
	private CarType carType;
	private String modelName;
	private String owner;
	private int modelYear;
	private int value;

	public CarContract(){

	}

	public String getCarNo() {
		return carNo;
	}

	public CarContract setCarNo(String carNo) {
		this.carNo = carNo;
		return this;
	}

	public CarType getCarType() {
		return carType;
	}

	public CarContract setCarType(CarType carType) {
		this.carType = carType;
		return this;
	}

	public String getModelName() {
		return modelName;
	}

	public CarContract setModelName(String modelName) {
		this.modelName = modelName;
		return this;
	}

	public int getModelYear() {
		return modelYear;
	}

	public CarContract setModelYear(int modelYear) {
		this.modelYear = modelYear;
		return this;
	}

	public int getValue() {
		return value;
	}

	public CarContract setValue(int value) {
		this.value = value;
		return this;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return super.toString() +
				" 자동차정보: {" +
				"자동차번호: '" + carNo + '\'' +
				", 차량유형: " + carType.getName() +
				", 연식: " + modelYear +
				", 차명: '" + modelName + '\'' +
				", 차주: '" + owner + '\'' +
				", 차량가액: " + value +
				"}}";

	}
}