package insuranceCompany.outerSystem;

public interface ElectronicPaymentSystem {


    static void pay(String paymentInfo, int premium) {
        System.out.println(paymentInfo + "를 사용해서  "+premium + "원이 결제되었습니다.");
    }
}
