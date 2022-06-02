package insuranceCompany.application.domain.payment;

import insuranceCompany.application.global.exception.MyIllegalArgumentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * packageName :  main.domain.payment
 * fileName : PaymentListImpl
 * author :  규현
 * date : 2022-05-12
 * description : Memory 관리하는 결제 수단에 대한 저장소.
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-12                규현             최초 생성
 */
public class PaymentListImpl implements PaymentDao {

    private static Map<Integer, Payment> paymentList = new HashMap<>();
    private static int idSequence;

    @Override
    public void create(Payment payment) {
        payment.setId(++idSequence);
        paymentList.put(payment.getId(), payment);
    }

    @Override
    public Payment read(int id) {
        Payment payment = paymentList.get(id);
        if (payment == null) {
            throw new MyIllegalArgumentException(id + "는 없는 ID 입니다. 다시 확인해주세요.");
        }
        return payment;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        Payment payment = paymentList.remove(id);
        if (payment == null) {
            throw new MyIllegalArgumentException(id + "는 없는 ID 입니다. 다시 확인해주세요.");
        }
        return true;
    }



    @Override
    public List<Payment> findAllByCustomerId(int customerId) {
        List<Payment> collect = paymentList.values()
                .stream()
                .filter(p -> p.getCustomerId() == customerId)
                .collect(Collectors.toList());
        if (collect.isEmpty())
            throw new MyIllegalArgumentException("해당 ID로 조회되는 결제수단이 존재하지 않습니다.");
        return collect;
    }
}
