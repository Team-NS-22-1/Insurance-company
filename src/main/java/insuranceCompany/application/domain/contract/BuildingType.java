package insuranceCompany.application.domain.contract;


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
}