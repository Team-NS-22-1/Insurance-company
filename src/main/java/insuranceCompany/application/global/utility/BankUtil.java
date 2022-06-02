package insuranceCompany.application.global.utility;

import insuranceCompany.application.domain.payment.BankType;
import insuranceCompany.application.global.exception.MyInadequateFormatException;

import static insuranceCompany.application.global.utility.FormatUtil.*;

/**
 * packageName :  utility
 * fileName : BankTuil
 * author :  규현
 * date : 2022-05-26
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-26                규현             최초 생성
 */
public class BankUtil {

    public static BankType selectBankType(MyBufferedReader br) {
        BankType[] values = BankType.values();
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            query.append(i + 1).append(" ").append(values[i]).append("\n");
        }
        query.append("0. 취소하기").append("\n").append("은행 번호 : ").append("\n");
        String key = "";
        key = (String) br.verifyRead(query.toString(),key);

        if(key.equals("0"))
            return null;

        return values[Integer.parseInt(key)-1];
    }

    public static String checkAccountFormat(BankType bankType, String accountNo) {
        boolean result = false;
        switch (bankType) {
            case KB :
                result = isKB(accountNo);
                break;
            case NH:
                result = isNH(accountNo);
                break;
            case KAKAOBANK:
                result = isKakaoBank(accountNo);
                break;
            case SINHAN:
                result = isSinhan(accountNo);
                break;
            case WOORI:
                result = isWoori(accountNo);
                break;
            case IBK:
                result = isIBK(accountNo);
                break;
            case HANA:
                result = isHana(accountNo);
                break;
            case CITY:
                result = isCity(accountNo);
                break;
            case SAEMAUL:
                result = isSaemaul(accountNo);
                break;
        }
        if (!result)
            throw new MyInadequateFormatException("정확한 형식의 값을 입력해주세요");

        return accountNo;

    }
}
