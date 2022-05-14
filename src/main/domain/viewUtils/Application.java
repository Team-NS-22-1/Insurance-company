package main.domain.viewUtils;

import main.domain.viewUtils.viewlogic.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import static main.domain.utility.MessageUtil.createMenu;

/**
 * packageName :  main.domain.viewUtils
 * fileName : Application
 * author :  규현
 * date : 2022-05-10
 * description : 어플리케이션 시작 클래스, 해당 클래스에서 유저 타입을 입력하는 것으로
 *               각자 개발한 ViewLogic을 수행하여 기능을 작동한다.
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-10                규현             최초 생성
 */
public class Application {
    private Map<UserType, ViewLogic> map = new HashMap<>();

    public Application() {
        map.put(UserType.GUEST,new GuestViewLogic());
        map.put(UserType.CUSTOMER, new CustomerViewLogic());
        map.put(UserType.SALES, new SalesViewLogic());
        map.put(UserType.DEV, new DevViewLogic());
        map.put(UserType.UW, new UWViewLogic());
        map.put(UserType.COMP, new CompVIewLogic());
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        createMenu("유저 타입","보험가입희망자","고객","영업팀","언더라이팅팀","개발팀","보상팀","종료하기");
        int userType = sc.nextInt();
        UserType[] values = UserType.values();

        UserType type = values[userType-1];
        if(type == UserType.OUT)
            System.exit(0);
        ViewLogic viewLogic = map.get(type);
        viewLogic.showMenu();
        String command = sc.next();
        viewLogic.work(command);
    }
}
