package insuranceCompany.application;

import insuranceCompany.application.global.exception.MyCloseSequence;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.viewlogic.LoginViewLogic;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ApplicationTest {

    private LoginViewLogic loginViewLogic;

    public ApplicationTest() {
        loginViewLogic = new LoginViewLogic();
    }

    public void run() {
        while(true) {
            Scanner sc = new Scanner(System.in);
            try {
                while (true){
                    loginViewLogic.showMenu();
                    String command = sc.nextLine();
                    if (command.equalsIgnoreCase("EXIT")) {
                        throw new MyCloseSequence();
                    }
                    loginViewLogic.work(command);
                }
            }
            catch (ArrayIndexOutOfBoundsException | InputMismatchException | MyIllegalArgumentException | NullPointerException e) {
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
