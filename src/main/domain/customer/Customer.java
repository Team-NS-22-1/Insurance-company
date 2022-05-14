package main.domain.customer;


import main.domain.accident.Accident;
import main.domain.contract.Contract;
import main.domain.payment.Payment;

import java.util.ArrayList;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class Customer {

	private String address;
	private String email;
	private int id;
	private boolean isContract;
	private String job;
	private String name;
	private ArrayList<Payment> paymentList = new ArrayList<>();
	private String phone;
	private String ssn;
	public Accident m_Accident;
	public Contract m_Contract;
	public Payment m_Payment;
	// Ȯ��
	private String housePrice;
	private String homeOwner;

	public ArrayList<Payment> getPaymentList() {
		return paymentList;
	}

	public Customer setPaymentList(ArrayList<Payment> paymentList) {
		this.paymentList = paymentList;
		return this;
	}

	public String getHousePrice() {
		return housePrice;
	}

	public void setHousePrice(String housePrice) {
		this.housePrice = housePrice;
	}

	public String getHomeOwner() {
		return homeOwner;
	}

	public void setHomeOwner(String homeOwner) {
		this.homeOwner = homeOwner;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isContract() {
		return isContract;
	}

	public void setContract(boolean isContract) {
		this.isContract = isContract;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public Accident getM_Accident() {
		return m_Accident;
	}

	public void setM_Accident(Accident m_Accident) {
		this.m_Accident = m_Accident;
	}

	public Contract getM_Contract() {
		return m_Contract;
	}

	public void setM_Contract(Contract m_Contract) {
		this.m_Contract = m_Contract;
	}

	public Payment getM_Payment() {
		return m_Payment;
	}

	public void setM_Payment(Payment m_Payment) {
		this.m_Payment = m_Payment;
	}


	public Customer(){

	}

	public void changeCompEmp(){

	}

	public void claimCompensation(){

	}

	public void pay(Contract contract){
		Payment payment = contract.getPayment();
		if(payment != null)
			System.out.println(contract.getPremium() + "원이 결제되었습니다.");
	}

	public void readContract(){

	}

	public void readPayment(){

	}

	public void addPayment(Payment payment){
		this.paymentList.add(payment);
		payment.setCustomerId(this.id);
	}

	public void registerPayment(Contract contract, Payment payment) {
		contract.setPayment(payment);
	}

	public void reportAccident(){

	}

	public void signUp(){

	}

	public void terminate(){

	}

}