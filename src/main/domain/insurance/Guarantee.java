package main.domain.insurance;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:00
 */
public class Guarantee {

	private int insurranceId;
	private String name;
	private String description;

	public Guarantee(String name, String description){
		this.name = name;
		this.description = description;
	}

	public String getName(){
		return this.name;
	}

	public String getDescription(){
		return this.description;
	}
	@Override
	public String toString() {
		return "이름 : " + name + " 설명 : " + description;
	}
}