package domain.contract;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:22
 */
public enum BuildingType {
	COMMERCIAL("상업용"),
	INDUSTRIAL("산업용"),
	INSTITUTIONAL("기관용"),
	RESIDENTIAL("거주용");
	String name;
	BuildingType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public static BuildingType valueOfName(String name) {
		return switch (name) {
			case "상업용" -> COMMERCIAL;
			case "산업용" -> INDUSTRIAL;
			case "기관용" -> INSTITUTIONAL;
			case "거주용" -> RESIDENTIAL;
			default -> null;
		};
	}

}