package insuranceCompany.application.domain.accident;


import java.time.format.DateTimeFormatter;

import static insuranceCompany.application.global.constant.CommonConstants.LIST_LINE;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class CarAccident extends Accident {


	private String carNo;
	private int errorRate;
	private boolean isRequestOnSite; // 현장 출동 요청
	private String opposingDriverPhone;
	private String placeAddress;

	public CarAccident(){

	}

	public String getCarNo() {
		return carNo;
	}

	public CarAccident setCarNo(String carNo) {
		this.carNo = carNo;
		return this;
	}

	public int getErrorRate() {
		return errorRate;
	}

	public CarAccident setErrorRate(int errorRate) {
		this.errorRate = errorRate;
		return this;
	}

	public boolean isRequestOnSite() {
		return isRequestOnSite;
	}

	public CarAccident setRequestOnSite(boolean requestOnSite) {
		isRequestOnSite = requestOnSite;
		return this;
	}

	public String getOpposingDriverPhone() {
		return opposingDriverPhone;
	}

	public CarAccident setOpposingDriverPhone(String opposingDriverPhone) {
		this.opposingDriverPhone = opposingDriverPhone;
		return this;
	}

	public String getPlaceAddress() {
		return placeAddress;
	}

	public CarAccident setPlaceAddress(String placeAddress) {
		this.placeAddress = placeAddress;
		return this;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public void printForCustomer() {
		String accidentDate = this.dateOfAccident.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
		String reportDate = this.dateOfReport.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
		String request = "거절";
		if(isRequestOnSite)
			request = "요청";
		


		String accidentInfo ="사고번호 : "+id +"\n사고 일시 : " + accidentDate + "\n접수 일시 : " + reportDate
				+"\n사고 장소 : " + placeAddress
				+"\n차량번호 : " + carNo +"\n상대방 연락처 : " + opposingDriverPhone
				+"\n현장 출동 여부 : " + request;
		System.out.println(accidentInfo);
		System.out.println(LIST_LINE);
	}


}