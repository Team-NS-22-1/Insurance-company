package main.domain.employee;


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
	public static Department getDepartmentByName(String name) {
		for(Department department : Department.values()){
			if(department.getName().equals(name))
				return department;
		}
		return null;
	}
}