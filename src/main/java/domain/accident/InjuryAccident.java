package domain.accident;


import java.time.format.DateTimeFormatter;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class InjuryAccident extends Accident {

	private String injurySite; // 부상 부위

	public InjuryAccident(){

	}

	public String getInjurySite() {
		return injurySite;
	}

	public InjuryAccident setInjurySite(String injurySite) {
		this.injurySite = injurySite;
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
				+"\n부상 부위 : " + injurySite;

		System.out.println(accidentInfo);
	}


}