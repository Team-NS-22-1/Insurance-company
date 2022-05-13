package main.domain.customer;


import main.domain.accident.Accident;
import main.domain.contract.Contract;

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
	private ArrayList<Payment> paymentList;
	private String phone;
	private String ssn;
	public Accident m_Accident;
	public Contract m_Contract;
	public Payment m_Payment;

	
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

	public void pay(){

	}

	public void readContract(){

	}

	public void readPayment(){

	}

	public void registerPayment(){

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
				", 인수심사여부: " + isContract +
				", 직업: '" + job + '\'' +
				", 이름: '" + name + '\'' +
				", 결제수단: " + paymentList +
				", 전화번호: '" + phone + '\'' +
				", 주민등록번호: '" + ssn + '\'' +
				'}';
	}

}