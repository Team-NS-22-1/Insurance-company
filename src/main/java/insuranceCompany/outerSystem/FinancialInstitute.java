package insuranceCompany.outerSystem;

import insuranceCompany.application.domain.customer.payment.Payment;

import static insuranceCompany.application.global.utility.ConsoleColors.RESET;
import static insuranceCompany.application.global.utility.ConsoleColors.YELLOW_BOLD;

public interface FinancialInstitute {

    public static void validPaymentInfo(Payment payment) {
        System.out.println(YELLOW_BOLD+"[알림] " + payment.toStringForValid() + " 는 사용가능합니다."+RESET);
    }
}
