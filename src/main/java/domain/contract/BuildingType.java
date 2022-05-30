package domain.contract;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:22
 */
public enum BuildingType {
	COMMERCIAL("상업형"),
	INDUSTRIAL("공업형"),
	INSTITUTIONAL("기관형"),
	RESIDENTIAL("주거형");

	String name;
	BuildingType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}