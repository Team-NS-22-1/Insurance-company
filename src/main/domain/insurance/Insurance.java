package main.domain.insurance;


import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오후 4:39:01
 */
public class Insurance {

	public int id;
	public String name;
	public String description;
	public int premium;
	public int contractPeriod;
	public int paymentPeriod;
	public ArrayList<Guarantee> guaranteeList = new ArrayList<>();
	public DevInfo devInfo;
	public SalesAuthFile salesAuthFile;
	public InsuranceType insuranceType;

	public Insurance(){
	}

	public String toString() {
		String value = this.id+"'"+this.name+"'"+this.description
				+"'"+this.premium+"'"+this.paymentPeriod+"'"+this.contractPeriod;
		return value;
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

	public int getPremium() {
		return premium;
	}

	public Insurance setPremium(int premium) {
		this.premium = premium;
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

	public ArrayList<Guarantee> getGuarantee() {
		return guaranteeList;
	}

	public Insurance setGuarantee(ArrayList<Guarantee> guaranteeList) {
		guaranteeList.stream()
				.forEach(guarantee -> this.guaranteeList.add(guarantee));
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

	public InsuranceType getInsuranceType() { return insuranceType; }

	public Insurance setInsuranceType(InsuranceType insuranceType) {
		this.insuranceType = insuranceType;
		return this;
	}

}