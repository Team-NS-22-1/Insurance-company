package insuranceCompany.application.domain.employee;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:24
 */
public enum Department {
	DEV("개발팀"),
	UW("언더라이팅"),
	COMP("보상팀"),
	SALES("영업팀");
	private String name;
	Department(String name){
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
}