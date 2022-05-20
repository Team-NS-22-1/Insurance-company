package domain.accident.accDocFile;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:22
 */
public class AccDocFile {

	private int accidentId;
	private String fileAddress;
	private int id;
	private String type;

	public AccDocFile(){

	}

	public int getAccidentId() {
		return accidentId;
	}

	public AccDocFile setAccidentId(int accidentId) {
		this.accidentId = accidentId;
		return this;
	}

	public String getFileAddress() {
		return fileAddress;
	}

	public AccDocFile setFileAddress(String fileAddress) {
		this.fileAddress = fileAddress;
		return this;
	}

	public int getId() {
		return id;
	}

	public AccDocFile setId(int id) {
		this.id = id;
		return this;
	}

	public String getType() {
		return type;
	}

	public AccDocFile setType(String type) {
		this.type = type;
		return this;
	}
}