package insuranceCompany.outerSystem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface ElectronicPaymentSystem {


    static void pay(String paymentInfo, int premium) {
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
        System.out.println("[결제 완료] ("+format+") "+paymentInfo+premium + "원이 결제되었습니다.");
    }
}
