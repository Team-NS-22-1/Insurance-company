package domain.insurance;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:25
 */
public enum SalesAuthState {
	PERMISSION("허가"),
	WAIT("대기"),
	DISALLOWANCE("불허");
	String name;
	SalesAuthState(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public static SalesAuthState valueOfName(String name){
		return switch (name) {
			case "허가" -> PERMISSION;
			case "대기" -> WAIT;
			case "불허" -> DISALLOWANCE;
			default -> null;
		};
	}
}