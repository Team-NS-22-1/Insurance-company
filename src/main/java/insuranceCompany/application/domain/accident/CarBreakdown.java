package insuranceCompany.application.domain.accident;

import java.time.format.DateTimeFormatter;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class CarBreakdown extends Accident {

	private String carNo;
	private String placeAddress;
	private String symptom;

	public CarBreakdown(){

	}

	public String getCarNo() {
		return carNo;
	}

	public CarBreakdown setCarNo(String carNo) {
		this.carNo = carNo;
		return this;
	}

	public String getPlaceAddress() {
		return placeAddress;
	}

	public CarBreakdown setPlaceAddress(String placeAddress) {
		this.placeAddress = placeAddress;
		return this;
	}

	public String getSymptom() {
		return symptom;
	}

	public CarBreakdown setSymptom(String symptom) {
		this.symptom = symptom;
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

		String accidentInfo ="사고번호 : "+id +"\n사고 일시 : " + accidentDate + "\n접수 일시 : " + reportDate
				+"\n사고 장소 : " + placeAddress
				+"\n차량번호 : " + carNo
				+"\n고장 증상 : " + symptom;
		System.out.println(accidentInfo);

	}


}