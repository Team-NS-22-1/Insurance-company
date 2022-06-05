package insuranceCompany.application.domain.customer.payment;


/**
 * @author 규현
 * @version 1.0
 * @created 09-5-2022 오전 2:42:25
 */
public abstract class Payment {

	protected int id;
	protected PayType paytype;
	protected int customerId;

	public int getId() {
		return id;
	}

	public Payment setId(int id) {
		this.id = id;
		return this;
	}

	public PayType getPaytype() {
		return paytype;
	}

	public Payment setPaytype(PayType paytype) {
		this.paytype = paytype;
		return this;
	}


	public int getCustomerId() {
		return customerId;
	}

	public Payment setCustomerId(int customerId) {
		this.customerId = customerId;
		return this;
	}

	@Override
	public abstract String toString();

	public abstract String toStringForPay();

	public abstract String toStringForValid();
}