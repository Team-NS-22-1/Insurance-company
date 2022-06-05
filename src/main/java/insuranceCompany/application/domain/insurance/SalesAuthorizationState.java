package insuranceCompany.application.domain.insurance;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:25
 */
public enum SalesAuthorizationState {
	PERMISSION("허가"),
	WAIT("대기"),
	DISALLOWANCE("불허");
	String name;
	SalesAuthorizationState(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
}