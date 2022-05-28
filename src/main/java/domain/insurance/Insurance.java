package domain.insurance;


import java.util.ArrayList;

/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오후 4:39:01
 */
public class Insurance {

	public int id;
	public String name;
	public String description;
	public int contractPeriod;
	public int paymentPeriod;
	public InsuranceType insuranceType;
	public ArrayList<Guarantee> guaranteeList = new ArrayList<>();
	public ArrayList<InsuranceDetail> insuranceDetailList = new ArrayList<>();
	public DevInfo devInfo;
	public SalesAuthFile salesAuthFile;

	public Insurance(){
	}

	public int getId() {
		return id;
	}

	public Insurance setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Insurance setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Insurance setDescription(String description) {
		this.description = description;
		return this;
	}

	public int getContractPeriod() {
		return contractPeriod;
	}

	public Insurance setContractPeriod(int contractPeriod) {
		this.contractPeriod = contractPeriod;
		return this;
	}

	public int getPaymentPeriod() {
		return paymentPeriod;
	}

	public Insurance setPaymentPeriod(int paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
		return this;
	}

	public InsuranceType getInsuranceType() {
		return insuranceType;
	}

	public Insurance setInsuranceType(InsuranceType insuranceType) {
		this.insuranceType = insuranceType;
		return this;
	}

	public ArrayList<Guarantee> getGuaranteeList() {
		return guaranteeList;
	}

	public Insurance setGuaranteeList(ArrayList<Guarantee> guaranteeList) {
		this.guaranteeList = guaranteeList;
		return this;
	}

	public ArrayList<InsuranceDetail> getInsuranceDetailList() {
		return insuranceDetailList;
	}

	public Insurance setInsuranceDetailList(ArrayList<InsuranceDetail> insuranceDetailList) {
		this.insuranceDetailList = insuranceDetailList;
		return this;
	}

	public DevInfo getDevInfo() {
		return devInfo;
	}

	public Insurance setDevInfo(DevInfo devInfo) {
		this.devInfo = devInfo;
		return this;
	}

	public SalesAuthFile getSalesAuthFile() {
		return salesAuthFile;
	}

	public Insurance setSalesAuthFile(SalesAuthFile salesAuthFile) {
		this.salesAuthFile = salesAuthFile;
		return this;
	}

	public String print() {
		return "보험 정보 {" +
				"보험ID: " + id +
				", 보험유형:" + insuranceType.getName() +
				", 이름: '" + name + '\'' +
				", 설명: '" + description + '\'' +
				", 계약기간: " + contractPeriod +
				", 납입기간: " + paymentPeriod +
				", 보장정보: " + guaranteeList +
				", 개발정보: " + devInfo.print() +
				", 인가파일: " + salesAuthFile.print() +
				'}';
	}
}