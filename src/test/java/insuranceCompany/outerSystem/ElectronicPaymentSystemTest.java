package insuranceCompany.outerSystem;

import insuranceCompany.application.dao.customer.PaymentDao;
import insuranceCompany.application.dao.customer.PaymentDaoImpl;
import insuranceCompany.application.domain.payment.Payment;
import org.junit.jupiter.api.Test;

class ElectronicPaymentSystemTest {

    @Test
    void interfaceTest() {
        PaymentDao dao = new PaymentDaoImpl();
        Payment read = dao.read(2);
        ElectronicPaymentSystem.pay(read.toStringForPay(),10000);
    }

    
    
    static interface test{
        
        static void print(){
            System.out.println("안녕하세요");
        }
    }
}