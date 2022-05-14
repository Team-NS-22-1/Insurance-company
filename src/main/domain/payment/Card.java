package main.domain.payment;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:23
 */
public class Card extends Payment {

	private String cardNo;
	private CardType cardType;
	private String cvcNo;
	private LocalDate expiryDate;

	public Card(){

	}

	public String getCardNo() {
		return cardNo;
	}

	public Card setCardNo(String cardNo) {
		this.cardNo = cardNo;
		return this;
	}

	public CardType getCardType() {
		return cardType;
	}

	public Card setCardType(CardType cardType) {
		this.cardType = cardType;
		return this;
	}

	public String getCvcNo() {
		return cvcNo;
	}

	public Card setCvcNo(String cvcNo) {
		this.cvcNo = cvcNo;
		return this;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public Card setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
		return this;
	}

	public void add(){

	}

	public void delete(){

	}

	public void pay(){

	}

	@Override
	public String toString() {
		String exDate = expiryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyyy"));
		return "ID : " + id + " 종류 : 카드 " + " 카드사 : " + cardType.name() + " 카드 번호 : " + cardNo + " 만료일 : " + exDate + " CVC : " + cvcNo;


//		return "Card{" +
//				"cardNo='" + cardNo + '\'' +
//				", cardType=" + cardType +
//				", cvcNo='" + cvcNo + '\'' +
//				", expiryDate=" + expiryDate +
//				", id=" + id +
//				", paytype=" + paytype +
//				", customerId=" + customerId +
//				'}';
	}
}