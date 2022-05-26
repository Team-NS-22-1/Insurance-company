package domain.customer;


import application.viewlogic.dto.accidentDto.AccidentReportDto;
import domain.accident.*;
import domain.accident.accDocFile.AccDocFile;

import domain.complain.Complain;

import domain.contract.Contract;
import domain.payment.*;
import utility.DocUtil;

import java.util.ArrayList;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public class Customer {

	private int id;
	private String name;
	private String ssn;
	private String address;
	private String phone;
	private String email;
	private String job;
	private ArrayList<Payment> paymentList = new ArrayList<>();
	private ArrayList<Complain> complainList = new ArrayList<>();

	public ArrayList<Complain> getComplainList() {
		return complainList;
	}

	public Customer setComplainList(ArrayList<Complain> complainList) {
		this.complainList = complainList;
		return this;
	}

	public ArrayList<Payment> getPaymentList() {
		return paymentList;
	}

	public Customer setPaymentList(ArrayList<Payment> paymentList) {
		this.paymentList = paymentList;
		return this;
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

	public Customer(){

	}

	public Complain changeCompEmp(String reason){
		Complain complain = Complain.builder().reason(reason)
				.customerId(this.id).build();

		this.complainList.add(complain);
		return complain;
	}

	// 파일을 선택해서 저장하고, 파일 주소를 리턴하는 식으로 해야할듯?
	public AccDocFile claimCompensation(Accident accident,AccDocFile accDocFile){
		DocUtil docUtil = DocUtil.getInstance();
		String path = this.id+"/"+ accident.getId();
		String directory = docUtil.upload(path, accDocFile.getType());
		if (directory.equals("close")) {
			return null;
		}
		accDocFile.setFileAddress(directory);
		accident.getAccDocFileList().put(accDocFile.getType(), accDocFile);
		return accDocFile;
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

	public Payment createPayment(PaymentDto paymentDto) {
		PayType payType = paymentDto.getPayType();
		Payment payment;
		if (payType.equals(PayType.CARD)) {
			payment = new Card();
			((Card)payment).setCardNo(paymentDto.getCardNo())
					.setCvcNo(paymentDto.getCvcNo())
					.setCardType(paymentDto.getCardType())
					.setExpiryDate(paymentDto.getExpiryDate());

		} else {
			payment = new Account();
			((Account)payment).setAccountNo(paymentDto.getAccountNo())
					.setBankType(paymentDto.getBankType());
		}
		payment.setPaytype(payType)
				.setCustomerId(paymentDto.getCustomerId());
		return payment;

	}

	public void addPayment(Payment payment){
		this.paymentList.add(payment);
		payment.setCustomerId(this.id);
	}

	public void registerPayment(Contract contract, Payment payment) {
		contract.setPayment(payment);
	}

	public Accident reportAccident(AccidentReportDto accidentReportDto){
		AccidentType accidentType = accidentReportDto.getAccidentType();

		Accident accident = null;
		if (accidentType == AccidentType.INJURYACCIDENT) {
			accident = new InjuryAccident();
			((InjuryAccident) accident).setInjurySite(accidentReportDto.getInjurySite());
		} else if(accidentType == AccidentType.CARACCIDENT) {
			accident = new CarAccident();
			((CarAccident)accident).setCarNo(accidentReportDto.getCarNo())
					.setPlaceAddress(accidentReportDto.getPlaceAddress())
					.setOpposingDriverPhone(accidentReportDto.getOpposingDriverPhone())
					.setRequestOnSite(accidentReportDto.isRequestOnSite());
		} else if (accidentType == AccidentType.CARBREAKDOWN) {
			accident = new CarBreakdown();
			((CarBreakdown) accident).setCarNo(accidentReportDto.getCarNo())
					.setPlaceAddress(accidentReportDto.getPlaceAddress())
					.setSymptom(accidentReportDto.getSymptom());
		} else {
			accident = new FireAccident();
			((FireAccident)accident).setPlaceAddress(accidentReportDto.getPlaceAddress());
		}
		return accident.setAccidentType(accidentType)
				.setDateOfAccident(accidentReportDto.getDateOfAccident())
				.setDateOfReport(accidentReportDto.getDateOfReport())
				.setCustomerId(this.id);
	}

	public void signUp() {

	}
	public void terminate(){
	}

	@Override
	public String toString() {
		return "고객정보 {" +
				"고객 ID: " + id +
				", 이름: '" + name + '\'' +
				", 주소: '" + address + '\'' +
				", 이메일: '" + email + '\'' +
				", 전화번호: '" + phone + '\'' +
				", 직업: '" + job + '\'' +
				", 주민등록번호: '" + ssn + '\'' +
				", 결제수단: " + paymentList +
				'}';
	}
}