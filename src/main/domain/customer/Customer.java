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


	public String getAddress() {
		return address;
	}

	public Customer setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Customer setEmail(String email) {
		this.email = email;
		return this;
	}

	public int getId() {
		return id;
	}

	public Customer setId(int id) {
		this.id = id;
		return this;
	}
<<<<<<< HEAD
=======

	public boolean isContract() {
		return isContract;
	}

	public Customer setContract(boolean isContract) {
		this.isContract = isContract;
		return this;
	}

>>>>>>> mincheol
	public String getJob() {
		return job;
	}

	public Customer setJob(String job) {
		this.job = job;
		return this;
	}

	public String getName() {
		return name;
	}

	public Customer setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public Customer setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getSsn() {
		return ssn;
	}

	public Customer setSsn(String ssn) {
		this.ssn = ssn;
		return this;
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

	@Override
	public String toString() {
		return "고객정보 {" +
				"주소: '" + address + '\'' +
				", 이메일: '" + email + '\'' +
				", 고객ID: " + id +
				", 직업: '" + job + '\'' +
				", 이름: '" + name + '\'' +
				", 결제수단: " + paymentList +
				", 전화번호: '" + phone + '\'' +
				", 주민등록번호: '" + ssn + '\'' +
				'}';
	}
}