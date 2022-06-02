package insuranceCompany.application;

import insuranceCompany.application.global.exception.*;
import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.viewlogic.LoginViewLogic;

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
                    String query = loginViewLogic.showMenu();
                    String command = String.valueOf(br.verifyMenu(query, 2));
                    if(command.equals("0")) throw new InputInvalidMenuException();
                    loginViewLogic.work(command);
                }
            }
            catch (InputException e) {
                System.out.println(e.getMessage());
            }
            catch (ArrayIndexOutOfBoundsException | InputMismatchException |
                    MyIllegalArgumentException | NullPointerException e) {
                System.out.println("정확한 값을 입력해주세요.");
            }
            catch (MyCloseSequence e) {
                System.out.println(e.getMessage());
                System.exit(0);
            } catch (NumberFormatException e) {
                System.out.println("형식에 맞는 메뉴를 입력해주세요");
            } catch (MyIOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }
    }
}
