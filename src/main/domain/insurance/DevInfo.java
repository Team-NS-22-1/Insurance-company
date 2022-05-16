package main.domain.insurance;


import java.time.LocalDate;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:59
 */
public class DevInfo {

	private int employeeId;
	private LocalDate devDate;
	private SalesAuthState salesAuthState;
	private LocalDate salesStartDate;

	public DevInfo(){
	}

	public String print() {
		String value =
				"{개발직원ID: " + employeeId +
				", 개발일자: " + devDate.getYear() + "년 " +
							devDate.getMonthValue() + "월 "+
							devDate.getDayOfMonth() + "일" +
				", 판매인가상태: " +salesAuthState.getName();
		if(salesAuthState == SalesAuthState.PERMISSION) {
			value += ", 판매시작일자: " + salesStartDate.getYear() + "년 " +
									salesStartDate.getMonthValue() + "월 " +
									salesStartDate.getDayOfMonth() + "일";
		}
		return value;
	}
	public int getEmployeeId() {
		return employeeId;
	}

	public DevInfo setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
		return this;
	}

	public LocalDate getDevDate() {
		return devDate;
	}

	public DevInfo setDevDate(LocalDate devDate) {
		this.devDate = devDate;
		return this;
	}

	public SalesAuthState getSalesAuthState() {
		return salesAuthState;
	}

	public DevInfo setSalesAuthState(SalesAuthState salesAuthState) {
		this.salesAuthState = salesAuthState;
		return this;
	}

	public LocalDate getSalesStartDate() {
		return salesStartDate;
	}

	public DevInfo setSalesStartDate(LocalDate salesStartDate) {
		this.salesStartDate = salesStartDate;
		return this;
	}
}