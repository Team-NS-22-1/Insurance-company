package insuranceCompany.application.domain.insurance;


import java.time.LocalDate;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:38:59
 */
public class DevelopInfo {

	private int id;
	private int insuranceId;
	private int employeeId;
	private LocalDate developDate;
	private SalesAuthorizationState salesAuthorizationState;
	private LocalDate salesStartDate;

	public int getId() {
		return id;
	}

	public DevelopInfo setId(int id) {
		this.id = id;
		return this;
	}

	public int getInsuranceId() {
		return insuranceId;
	}

	public DevelopInfo setInsuranceId(int insuranceId) {
		this.insuranceId = insuranceId;
		return this;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public DevelopInfo setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
		return this;
	}

	public LocalDate getDevelopDate() {
		return developDate;
	}

	public DevelopInfo setDevelopDate(LocalDate developDate) {
		this.developDate = developDate;
		return this;
	}

	public SalesAuthorizationState getSalesAuthorizationState() {
		return salesAuthorizationState;
	}

	public DevelopInfo setSalesAuthorizationState(SalesAuthorizationState salesAuthorizationState) {
		this.salesAuthorizationState = salesAuthorizationState;
		return this;
	}

	public LocalDate getSalesStartDate() {
		return salesStartDate;
	}

	public DevelopInfo setSalesStartDate(LocalDate salesStartDate) {
		this.salesStartDate = salesStartDate;
		return this;
	}

	public String print() {
		String value =
				"{개발직원ID: " + employeeId +
				", 개발일자: " + developDate.getYear() + "년 " +
							developDate.getMonthValue() + "월 "+
							developDate.getDayOfMonth() + "일" +
				", 판매인가상태: " + salesAuthorizationState.name();
		if(salesAuthorizationState == SalesAuthorizationState.PERMISSION) {
			value += ", 판매시작일자: " + salesStartDate.getYear() + "년 " +
									salesStartDate.getMonthValue() + "월 " +
									salesStartDate.getDayOfMonth() + "일";
		}
		value += "}";
		return value;
	}
}