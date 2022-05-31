package domain.payment;

import dao.CrudInterface;

import java.util.List;

/**
 * packageName :  main.domain.payment
 * fileName : PaymentList
 * author :  규현
 * date : 2022-05-12
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-12                규현             최초 생성
 */
public interface PaymentList extends CrudInterface<Payment> {

    List<Payment> findAllByCustomerId(int customerId);
}
