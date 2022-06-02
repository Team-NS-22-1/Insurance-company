package insuranceCompany.application;

import insuranceCompany.application.global.exception.MyCloseSequence;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.viewlogic.*;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import static insuranceCompany.application.global.utility.MessageUtil.createMenu;


/**
 * packageName :  main.application
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
        map.put(UserType.DEV, new DevelopViewLogic());
        map.put(UserType.UW, new UnderwritingViewLogic());
        map.put(UserType.COMP, new CompensationViewLogic());

    }

    public void run() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            try {
                createMenu("<<유저 타입>>", "보험가입희망자", "고객", "영업팀", "언더라이팅팀", "개발팀", "보상팀");
                System.out.println("0. 종료하기");
                int userType = Integer.parseInt(sc.nextLine());
                UserType[] values = UserType.values();

                if (userType == 0) {
                    throw new MyCloseSequence();
                }
                UserType type = values[userType - 1];
                while (true) {
                    ViewLogic viewLogic = map.get(type);
                    viewLogic.showMenu();
                    String command = sc.nextLine();
                    if (command.equals("0"))
                        break;
                    if (command.equalsIgnoreCase("EXIT")) {
                        throw new MyCloseSequence();
                    }
                    viewLogic.work(command);
                }
            }
            catch (ArrayIndexOutOfBoundsException | InputMismatchException | MyIllegalArgumentException e) {
                System.out.println("정확한 값을 입력해주세요.");
            }
            catch (MyCloseSequence e) {
                System.out.println(e.getMessage());
                System.exit(0);
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 메뉴를 입력해주세요");
            }
        }
    }
}
