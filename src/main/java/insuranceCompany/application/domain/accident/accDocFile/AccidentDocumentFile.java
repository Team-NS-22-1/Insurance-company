package insuranceCompany.application.domain.accident.accDocFile;


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:22
 */
public class AccidentDocumentFile {

	private int accidentId;
	private String fileAddress;
	private int id;
	private AccDocType type;
	private LocalDateTime lastModifedDate;

	public AccidentDocumentFile(){

	}

	public int getAccidentId() {
		return accidentId;
	}

	public AccidentDocumentFile setAccidentId(int accidentId) {
		this.accidentId = accidentId;
		return this;
	}

	public String getFileAddress() {
		return fileAddress;
	}

	public AccidentDocumentFile setFileAddress(String fileAddress) {
		this.fileAddress = fileAddress;
		return this;
	}

	public int getId() {
		return id;
	}

	public AccidentDocumentFile setId(int id) {
		this.id = id;
		return this;
	}

	public AccDocType getType() {
		return type;
	}

	public AccidentDocumentFile setType(AccDocType type) {
		this.type = type;
		return this;
	}

	public LocalDateTime getLastModifedDate() {
		return lastModifedDate;
	}

	public AccidentDocumentFile setLastModifedDate(LocalDateTime lastModifedDate) {
		this.lastModifedDate = lastModifedDate;
		return this;
	}

	@Override
	public String toString() {
		return "AccDocFile{" +
				"accidentId=" + accidentId +
				", fileAddress='" + fileAddress + '\'' +
				", id=" + id +
				", type=" + type +
				", lastModifedDate=" + lastModifedDate +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AccidentDocumentFile that = (AccidentDocumentFile) o;
		return Objects.equals(getFileAddress(), that.getFileAddress()) && getType() == that.getType();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getFileAddress(), getType());
	}
}