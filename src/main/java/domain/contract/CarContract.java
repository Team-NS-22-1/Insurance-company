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
	private int modelYear;
	private Long value;

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

	public Long getValue() {
		return value;
	}

	public CarContract setValue(Long value) {
		this.value = value;
		return this;
	}

	@Override
	public String toString() {
		return "계약정보 {" +
				"계약 ID: " + getId() +
				", 인수심사상태: " + getConditionOfUw() +
				", 인수사유: '" + getReasonOfUw() + '\'' +
				", 증권발행여부: " + isPublishStock() +
				", 보험료: " + getPremium() +
				", 자동차번호: '" + carNo + '\'' +
				", 차량유형: " + carType +
				", 연식: " + modelYear +
				", 차명: '" + modelName + '\'' +
				", 차량가액: " + value +
				'}';
	}
}