package domain.insurance;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public enum InsuranceType {
	HEALTH("건강보험"),
	FIRE("화재보험"),
	CAR("자동차보험");

	String name;
	InsuranceType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public static InsuranceType valueOfName(String name) {
		return switch (name) {
			case "건강보험" -> HEALTH;
			case "화재보험" -> FIRE;
			case "자동차보험" -> CAR;
			default -> null;
		};
	}
}