package main.domain.employee;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:25
 */
public enum Position {
	CEO("CEO"),
	DEPTMANAGER("부장"),
	TEAMLEADER("팀장"),
	MEMBER("팀원");
	private String name;
	Position(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}

	public static Position getPositionByName(String name) {
		for(Position position : Position.values()){
			if(position.getName().equals(name))
				return position;
		}
		return null;
	}
}