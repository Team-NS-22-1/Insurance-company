package insuranceCompany.outerSystem;

import insuranceCompany.application.domain.customer.payment.Payment;

public interface FinancialInstitute {

    public static void validPaymentInfo(Payment payment) {
        System.out.println("[알림] " + payment.toStringForValid() + " 는 인증되었습니다.");
    }
}
