package main.domain.insurance;


/**
 * @author ����
 * @version 1.0
 * @created 09-5-2022 ���� 4:39:02
 */
public class SalesAuthFile {

	/**
	 * 금융감독원 인가허가파일
	 */
	private String dirFSSOfficialDoc;
	/**
	 * 보험요율산출기관 검증확인서
	 */
	private String dirISOVerification;
	/**
	 * 보험상품신고서
	 */
	private String dirProdDeclaration;
	/**
	 * 선임계리사 검증기초서류
	 */
	private String dirSrActuaryVerification;

	public SalesAuthFile() {}

	public String getDirFSSOfficialDoc() {
		return dirFSSOfficialDoc;
	}

	public SalesAuthFile setDirFSSOfficialDoc(String dirFSSOfficialDoc) {
		this.dirFSSOfficialDoc = dirFSSOfficialDoc;
		return this;
	}

	public String getDirISOVerification() {
		return dirISOVerification;
	}

	public SalesAuthFile setDirISOVerification(String dirISOVerification) {
		this.dirISOVerification = dirISOVerification;
		return this;
	}

	public String getDirProdDeclaration() {
		return dirProdDeclaration;
	}

	public SalesAuthFile setDirProdDeclaration(String dirProdDeclaration) {
		this.dirProdDeclaration = dirProdDeclaration;
		return this;
	}

	public String getDirSrActuaryVerification() {
		return dirSrActuaryVerification;
	}

	public SalesAuthFile setDirSrActuaryVerification(String dirSrActuaryVerification) {
		this.dirSrActuaryVerification = dirSrActuaryVerification;
		return this;
	}

	public String print() {
		return "null";
	}

}