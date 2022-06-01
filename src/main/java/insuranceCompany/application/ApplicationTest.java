package insuranceCompany.application;

import insuranceCompany.application.global.exception.MyCloseSequence;
import insuranceCompany.application.global.exception.MyIllegalArgumentException;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.viewlogic.LoginViewLogic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;

public class ApplicationTest {

    private LoginViewLogic loginViewLogic;

    public ApplicationTest() {
        loginViewLogic = new LoginViewLogic();
    }

    public void run() {
        MyBufferedReader br = new MyBufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                while (true){
                    loginViewLogic.showMenu();
                    String command = String.valueOf(br.verifyMenu("", 2));
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
            } catch (IOException e) {
                System.out.println("ERROR:: IO 시스템에 장애가 발생하였습니다!\n프로그램을 종료합니다...");
                System.exit(0);
            }
        }
    }

}
