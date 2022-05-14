package main.utility;

import java.util.regex.Pattern;

/**
 * packageName :  main.domain.utility
 * fileName : PaymentFormatUtil
 * author :  규현
 * date : 2022-05-14
 * description : 고객이 카드, 계좌 정보 추가 시 입력하는 형식을 검증하는 클래스
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-14                규현             최초 생성
 */
public class PaymentFormatUtil {

    public static boolean isCardNo(String str) {
        return Pattern.matches("^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}$",str);
    }

    public static boolean isCVC(String str) {
        return Pattern.matches("^[0-9]{3}$", str);
    }
    public static boolean isMonth(int m) {
        return m>0 && m<13;
    }
    public static boolean isYear(String str) {
        return Pattern.matches("^20[2-3][0-9]$", str);
    }



    public static boolean isNH(String str) {
        return Pattern.matches("^(312|301|302)-[0-9]{4}-[0-9]{4}-\\d{2}$", str);
    }
    public static boolean isKB(String str) {
        return Pattern.matches("^[0-9]{6}-[0-9]{2}-[0-9]{6}$",str);
    }
    public static boolean isWoori(String str) {
        return Pattern.matches("^\\d{4}-\\d{3}-\\d{6}$",str);
    }
    public static boolean isHana(String str) {
        return Pattern.matches("^\\d{3}-\\d{6}-\\d{5}$",str);
    }
    public static boolean isIBK(String str) {
        return Pattern.matches("^\\d{3}-\\d{6}-\\d{2}-\\d{3}$",str);
    }
    public static boolean isSaemaul(String str) {
        return Pattern.matches("^9\\d{3}-\\d{8}-\\d$",str);
    }
    public static boolean isKakaoBank(String str) {
        return Pattern.matches("^\\d{4}-\\d{2}-\\d{7}$",str);
    }
    public static boolean isSinhan(String str) {
        return Pattern.matches("^\\d{3}-\\d{3}-\\d{6}$",str);
    }
    public static boolean isCity(String str) {
        return Pattern.matches("^\\d{3}-\\d{6}-\\d{3}$",str);
    }
}
