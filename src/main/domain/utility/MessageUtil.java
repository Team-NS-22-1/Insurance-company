package main.domain.utility;

import main.domain.viewUtils.UserType;

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
            sb.append(i + 1).append(".").append(elements[i]).append("\n");
        }
        System.out.println(sb.toString());
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
