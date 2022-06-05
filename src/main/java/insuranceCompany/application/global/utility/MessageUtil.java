package insuranceCompany.application.global.utility;

import insuranceCompany.application.global.constant.CommonConstants;
import insuranceCompany.application.login.UserType;

import static insuranceCompany.application.global.constant.CommonConstants.*;

/**
 * packageName :  main.domain.utility
 * fileName : MessageUtil
 * author :  규현
 * date : 2022-05-10
 * description : menu와 목록을 넣으면 메세지를 뽑아주는 유틸리티 클래스
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class MessageUtil {
    public static void createMenu(String menuName, String ... elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuName).append("\n");
        for (int i = 0; i < elements.length; i++) {
            sb.append(i + 1).append(".").append(" ").append(elements[i]).append("\n");
        }
        System.out.print(sb);
    }

    public static void createMenuAndExit(String menuName, String ... elements) {
        createMenu(menuName, elements);
        System.out.println(ZERO_MESSAGE);
        System.out.println(EXIT_MESSAGE);
    }

    public static String createMenuAndExitQuery(String menuName, String ... elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuName).append("\n");
        for (int i = 0; i < elements.length; i++) {
            sb.append(i + 1).append(".").append(" ").append(elements[i]).append("\n");
        }
        sb.append("0.").append(" ").append("취소하기").append("\n")
                .append("exit.").append(" ").append("종료하기\n").append("입력 : ");
        return sb.toString();
    }

    public static void createMenuOnlyExit(String menuName, String ... elements) {
        createMenu(menuName, elements);
        System.out.println(EXIT_MESSAGE);
    }

    public static String createMenuOnlyExitQuery(String menuName, String ... elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuName).append("\n");
        for (int i = 0; i < elements.length; i++) {
            sb.append(i + 1).append(".").append(" ").append(elements[i]).append("\n");
        }
        sb.append("exit.").append(" ").append("종료하기\n");
        return sb.toString();
    }

    public static String createMenuOnlyExitQueryAndInput(String menuName, String ... elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuName).append("\n");
        for (int i = 0; i < elements.length; i++) {
            sb.append(i + 1).append(".").append(" ").append(elements[i]).append("\n");
        }
        sb.append("exit.").append(" ").append("종료하기\n").append("입력 : ");
        return sb.toString();
    }

    public static void createMenuAndClose(String menuName, String ... elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuName).append("\n");
        for (int i = 0; i < elements.length; i++) {
            sb.append(i + 1).append(".").append(" ").append(elements[i]).append("\n");
        }
        sb.append("0.").append(" ").append("취소하기").append("\n")
                        .append("exit.").append(" ").append("종료하기\n");
        System.out.print(sb.toString());
    }

    public static String createMenuAndLogout(String menuName, String ... elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuName).append("\n");
        for (int i = 0; i < elements.length; i++) {
            sb.append(i + 1).append(".").append(" ").append(elements[i]).append("\n");
        }
        sb.append("0.").append(" ").append("로그아웃").append("\n")
                .append("exit.").append(" ").append("종료하기\n");
        return sb.toString();
    }
    public static String createMenuAndLogoutAndInput(String menuName, String ... elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuName).append("\n");
        for (int i = 0; i < elements.length; i++) {
            sb.append(i + 1).append(".").append(" ").append(elements[i]).append("\n");
        }
        sb.append("0.").append(" ").append("로그아웃").append("\n")
                .append("exit.").append(" ").append("종료하기\n").append("입력 : ");
        return sb.toString();
    }


    public static void createMenuEX(String menuName, UserType userType) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuName).append("\n");
        UserType[] values = UserType.values();
        for (int i = 0; i < values.length; i++) {
            sb.append(i + 1).append(".").append(values[i].name()).append("\n");
        }
        System.out.println(sb.toString());
    }
}
