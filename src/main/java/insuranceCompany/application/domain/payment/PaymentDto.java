package insuranceCompany.application.domain.payment;

import java.time.LocalDate;

/**
 * packageName :  main.domain.payment
 * fileName : PaymentDto
 * author :  규현
 * date : 2022-05-16
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-16                규현             최초 생성
 */
public class PaymentDto {

    private PayType payType;
    private int customerId;

    //cardInfo
    private String cardNo;
    private CardType cardType;
    private String cvcNo;
    private LocalDate expiryDate;

    //accountInfo
    private String accountNo;
    private BankType bankType;

    public PayType getPayType() {
        return payType;
    }

    public PaymentDto setPayType(PayType payType) {
        this.payType = payType;
        return this;
    }

    public int getCustomerId() {
        return customerId;
    }

    public PaymentDto setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getCardNo() {
        return cardNo;
    }

    public PaymentDto setCardNo(String cardNo) {
        this.cardNo = cardNo;
        return this;
    }

    public CardType getCardType() {
        return cardType;
    }

    public PaymentDto setCardType(CardType cardType) {
        this.cardType = cardType;
        return this;
    }

    public String getCvcNo() {
        return cvcNo;
    }

    public PaymentDto setCvcNo(String cvcNo) {
        this.cvcNo = cvcNo;
        return this;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public PaymentDto setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public PaymentDto setAccountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public BankType getBankType() {
        return bankType;
    }

    public PaymentDto setBankType(BankType bankType) {
        this.bankType = bankType;
        return this;
    }
}
