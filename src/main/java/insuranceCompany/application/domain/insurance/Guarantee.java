package insuranceCompany.application.domain.insurance;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:00
 */
public class Guarantee {

	private int id;
	private int insuranceId;
	private String name;
	private String description;
	private Long guaranteeAmount;

	public Guarantee() {
	}

	public int getId() {
		return id;
	}

	public Guarantee setId(int id) {
		this.id = id;
		return this;
	}

	public int getInsuranceId() {
		return insuranceId;
	}

	public Guarantee setInsuranceId(int insuranceId) {
		this.insuranceId = insuranceId;
		return this;
	}

	public String getName() {
		return name;
	}

	public Guarantee setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Guarantee setDescription(String description) {
		this.description = description;
		return this;
	}

	public Long getGuaranteeAmount() {
		return guaranteeAmount;
	}

	public Guarantee setGuaranteeAmount(Long guaranteeAmount) {
		this.guaranteeAmount = guaranteeAmount;
		return this;
	}

	public String print() {
		return "{" +
				"보장ID: " + id +
				", 보장명: '" + name +
				"', 보장설명: '" + description +
				"', 보장금액: " + guaranteeAmount +
				"}";
	}

	@Override
	public String toString() {
		return "{" +
				"이름 : " + name +
				", 설명 : " + description +
				", 보장금액: " + guaranteeAmount +
				"}";
	}

}