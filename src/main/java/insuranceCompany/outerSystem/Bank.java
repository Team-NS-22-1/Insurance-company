package insuranceCompany.outerSystem;

import insuranceCompany.application.domain.payment.Account;

/**
 * packageName :  outerSystem
 * fileName : Bank
 * author :  규현
 * date : 2022-05-25
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-25                규현             최초 생성
 */
public interface Bank {

    public static void sendCompensation(Account account,long compensation) {
        System.out.println("["+account.getBankType() + "]"+account.getAccountNo() + "로 "+compensation + "원이 지급되었습니다.");
    }
}
