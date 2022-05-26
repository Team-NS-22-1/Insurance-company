package domain.insurance;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:02
 */
public class SalesAuthFile {

	private int id;

	/**
	 * 보험상품신고서
	 */
	private String prodDeclaration;
	/**
	 * 보험요율산출기관 검증확인서
	 */
	private String isoVerification;

	/**
	 * 선임계리사 검증기초서류
	 */
	private String srActuaryVerification;
	/**
	 * 금융감독원 인가허가파일
	 */
	private String fSSOfficialDoc;

	private int insuranceId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProdDeclaration() {
		return prodDeclaration;
	}

	public void setProdDeclaration(String prodDeclaration) {
		this.prodDeclaration = prodDeclaration;
	}

	public String getIsoVerification() {
		return isoVerification;
	}

	public void setIsoVerification(String isoVerification) {
		this.isoVerification = isoVerification;
	}

	public String getSrActuaryVerification() {
		return srActuaryVerification;
	}

	public void setSrActuaryVerification(String srActuaryVerification) {
		this.srActuaryVerification = srActuaryVerification;
	}

	public String getfSSOfficialDoc() {
		return fSSOfficialDoc;
	}

	public void setfSSOfficialDoc(String fSSOfficialDoc) {
		this.fSSOfficialDoc = fSSOfficialDoc;
	}

	public int getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(int insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String print() {
		String print = "";
		if(prodDeclaration != null) print += "보험상품신고서 ";
		if(isoVerification != null) print += "보험요율산출기관 검증확인서 ";
		if(srActuaryVerification != null) print += "선임계리사 검증기초서류 ";
		if(fSSOfficialDoc != null) print += "금융감독원 인가허가파일 ";
		return print;
	}

}