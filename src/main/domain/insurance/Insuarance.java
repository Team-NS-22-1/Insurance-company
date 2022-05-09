package main.domain.insurance;


import main.domain.contract.Contract;
import main.domain.customer.Customer;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public abstract class Insuarance {

	private int contractPeriod;
	public int getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(int contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DevInfo getDevInfo() {
		return devInfo;
	}

	public void setDevInfo(DevInfo devInfo) {
		this.devInfo = devInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String description;
	private DevInfo devInfo;
	private int id;
	//private enum insuranceType;
	private String name;
	private int paymentPeriod;
	private int premium;
	private SalesAuthFile salesAuthFile;
	public Contract m_Contract;
	public SalesAuthFile m_SalesAuthFile;
	public Guarantee m_Guarantee;
	public DevInfo m_DevInfo;


	public Insuarance(){

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPaymentPeriod() {
		return paymentPeriod;
	}

	public void setPaymentPeriod(int paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}

	public int getPremium() {
		return premium;
	}

	public void setPremium(int premium) {
		this.premium = premium;
	}

	public SalesAuthFile getSalesAuthFile() {
		return salesAuthFile;
	}

	public void setSalesAuthFile(SalesAuthFile salesAuthFile) {
		this.salesAuthFile = salesAuthFile;
	}

	public Contract getM_Contract() {
		return m_Contract;
	}

	public void setM_Contract(Contract m_Contract) {
		this.m_Contract = m_Contract;
	}

	public SalesAuthFile getM_SalesAuthFile() {
		return m_SalesAuthFile;
	}

	public void setM_SalesAuthFile(SalesAuthFile m_SalesAuthFile) {
		this.m_SalesAuthFile = m_SalesAuthFile;
	}

	public Guarantee getM_Guarantee() {
		return m_Guarantee;
	}

	public void setM_Guarantee(Guarantee m_Guarantee) {
		this.m_Guarantee = m_Guarantee;
	}

	public DevInfo getM_DevInfo() {
		return m_DevInfo;
	}

	public void setM_DevInfo(DevInfo m_DevInfo) {
		this.m_DevInfo = m_DevInfo;
	}
	
	public void register() {
		
	}
	
	abstract public float calcuationRatio(Customer customer);

}