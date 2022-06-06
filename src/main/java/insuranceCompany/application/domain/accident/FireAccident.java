package insuranceCompany.application.domain.accident;


import java.time.format.DateTimeFormatter;

import static insuranceCompany.application.global.constant.CommonConstants.LIST_LINE;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class FireAccident extends Accident {

	private String placeAddress;

	public FireAccident(){

	}

	public String getPlaceAddress() {
		return placeAddress;
	}

	public FireAccident setPlaceAddress(String placeAddress) {
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

		String accidentInfo ="사고번호 : "+id +"\n사고 일시 : " + accidentDate + "\n접수 일시 : " + reportDate
				+"\n사고 장소 : " + placeAddress;
		System.out.println(accidentInfo);
		System.out.println(LIST_LINE);
	}


}