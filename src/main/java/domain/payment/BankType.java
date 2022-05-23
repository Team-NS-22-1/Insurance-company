package domain.payment;


/**
 * @author SeungHo
 * @version 1.0
 * @created 09-5-2022 오전 2:42:22
 */
public enum BankType {
	KB("******-**-******"),
	NH("(301 or 302 or 312)-****-****-**"),
	KAKAOBANK("****-**-*******"),
	SINHAN("***-***-******"),
	WOORI("****-***-******"),
	IBK("***-******-**-***"),
	HANA("***-******-*****"),
	CITY("***-******-***"),
	SAEMAUL("9***-********-*");

	private String format;
	private BankType(String format){
		this.format = format;
	}

	public String getFormat() {
		return this.format;
	}
}