package domain.contract;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public enum CarType {
	URBAN("경형"),
	SUBCOMPACT("소형"),
	COMPACT("준중형"),
	MIDSIZE("중형"),
	LARGESIZE("준대형"),
	FULLSIZE("대형"),
	SPORTS("스포츠카");

	String name;
	CarType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

}