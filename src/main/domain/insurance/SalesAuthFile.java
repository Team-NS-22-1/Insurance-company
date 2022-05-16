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

	public SalesAuthFile(){
	}

	public String print() {
		return "null";
	}

}