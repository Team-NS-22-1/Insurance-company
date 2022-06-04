package insuranceCompany.application.viewlogic.dto.contractDto;

import insuranceCompany.application.domain.contract.CarType;

public class CarContractDto extends ContractDto {
    private String carNo;
    private CarType carType;
    private String modelName;
    private int modelYear;
    private Long value;

    public CarContractDto () {
    }

    public CarContractDto (String carNo, CarType carType, String modelName, int modelYear, Long value) {
        this.carNo = carNo;
        this.carType = carType;
        this.modelName = modelName;
        this.modelYear = modelYear;
        this.value = value;
    }

    public String getCarNo() {
        return carNo;
    }

    public CarContractDto setCarNo(String carNo) {
        this.carNo = carNo;
        return this;
    }

    public CarType getCarType() {
        return carType;
    }

    public CarContractDto setCarType(CarType carType) {
        this.carType = carType;
        return this;
    }

    public String getModelName() {
        return modelName;
    }

    public CarContractDto setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public int getModelYear() {
        return modelYear;
    }

    public CarContractDto setModelYear(int modelYear) {
        this.modelYear = modelYear;
        return this;
    }

    public Long getValue() {
        return value;
    }

    public CarContractDto setValue(Long value) {
        this.value = value;
        return this;
    }
}
