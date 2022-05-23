package domain.payment;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:22
 */
public class Account extends Payment {

	private String accountNo;
	private BankType bankType;

	public Account(){

	}

	public String getAccountNo() {
		return accountNo;
	}

	public Account setAccountNo(String accountNo) {
		this.accountNo = accountNo;
		return this;
	}

	public BankType getBankType() {
		return bankType;
	}

	public Account setBankType(BankType bankType) {
		this.bankType = bankType;
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
		return "ID : " + id + " 종류 : 계좌 " + " 은행 : " + bankType.name() + " 계좌 번호 : " + accountNo;

//		return "Account{" +
//				"accountNo='" + accountNo + '\'' +
//				", bankType=" + bankType +
//				", id=" + id +
//				", paytype=" + paytype +
//				", customerId=" + customerId +
//				'}';
	}
}