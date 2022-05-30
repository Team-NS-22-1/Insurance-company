package domain.insurance;

import java.time.LocalDateTime;

/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:02
 */
public class SalesAuthFile {

	private int insuranceId;

	/**
	 * 보험상품신고서
	 */
	private String prodDeclaration;
	private LocalDateTime modifiedProd;

	/**
	 * 보험요율산출기관 검증확인서
	 */
	private String isoVerification;
	private LocalDateTime modifiedIso;

	/**
	 * 선임계리사 검증기초서류
	 */
	private String srActuaryVerification;
	private LocalDateTime modifiedSrActuary;

	/**
	 * 금융감독원 인가허가파일
	 */
	private String fssOfficialDoc;
	private LocalDateTime modifiedFss;

	public int getInsuranceId() {
		return insuranceId;
	}

	public SalesAuthFile setInsuranceId(int insuranceId) {
		this.insuranceId = insuranceId;
		return this;
	}

	public String getProdDeclaration() {
		return prodDeclaration;
	}

	public SalesAuthFile setProdDeclaration(String prodDeclaration) {
		this.prodDeclaration = prodDeclaration;
		return this;
	}

	public LocalDateTime getModifiedProd() {
		return modifiedProd;
	}

	public SalesAuthFile setModifiedProd(LocalDateTime modifiedProd) {
		this.modifiedProd = modifiedProd;
		return this;
	}

	public String getIsoVerification() {
		return isoVerification;
	}

	public SalesAuthFile setIsoVerification(String isoVerification) {
		this.isoVerification = isoVerification;
		return this;
	}

	public LocalDateTime getModifiedIso() {
		return modifiedIso;
	}

	public SalesAuthFile setModifiedIso(LocalDateTime modifiedIso) {
		this.modifiedIso = modifiedIso;
		return this;
	}

	public String getSrActuaryVerification() {
		return srActuaryVerification;
	}

	public SalesAuthFile setSrActuaryVerification(String srActuaryVerification) {
		this.srActuaryVerification = srActuaryVerification;
		return this;
	}

	public LocalDateTime getModifiedSrActuary() {
		return modifiedSrActuary;
	}

	public SalesAuthFile setModifiedSrActuary(LocalDateTime modifiedSrActuary) {
		this.modifiedSrActuary = modifiedSrActuary;
		return this;
	}

	public String getFssOfficialDoc() {
		return fssOfficialDoc;
	}

	public SalesAuthFile setFssOfficialDoc(String fSSOfficialDoc) {
		this.fssOfficialDoc = fSSOfficialDoc;
		return this;
	}

	public LocalDateTime getModifiedFss() {
		return modifiedFss;
	}

	public SalesAuthFile setModifiedFss(LocalDateTime modifiedFss) {
		this.modifiedFss = modifiedFss;
		return this;
	}

	public String print() {
		String print = "{보험ID: " + insuranceId + ", [";
		if(prodDeclaration != null) print += "보험상품신고서(" + modifiedProd + ") ";
		if(isoVerification != null) print += "보험요율산출기관 검증확인서(" + modifiedIso + ") ";
		if(srActuaryVerification != null) print += "선임계리사 검증기초서류(" + modifiedSrActuary + ") ";
		if(fssOfficialDoc != null) print += "금융감독원 인가허가파일(" + modifiedFss + ")";
		print += "]";
		return print;
	}

}