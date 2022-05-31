package login;

import dao.UserDao;
import domain.customer.Customer;
import exception.InputException;
import utility.MyBufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

public class Login {

    public Login() throws IOException {
    }

    public int menuLogin() throws IOException {
        MyBufferedReader br = new MyBufferedReader(new InputStreamReader(System.in));
        String id = "", password = "";
        int roleId = -1;
        loopLogin: while(true){
            try {
                System.out.println("<< 로그인 >>");
                id = (String) br.verifyRead("아이디: ", id);
                if(id.equals("0")) break loopLogin;        // 로그인 취소
                loopPw: while (true) {
                    password = (String) br.verifyRead("비밀번호: ", password);
                    if(password.equals("0")) break loopLogin;        // 로그인 취소
                    int login = login(id, password);
                    if(login < 0) {
                        switch (login) {
                            case -1 -> System.out.println("LOGIN ERROR:: 패스워드를 다시 입력하세요!");
                            default -> {
                                System.out.println("LOGIN ERROR:: 아이디를 다시 입력하세요!");
                                break loopPw;
                            }
                        }
                    }
                    else{
                        roleId = login;
                        break loopLogin;
                    }
                }
            }
            catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
        return roleId;
    }

    private int login(String id, String password) {
        return new UserDao().login(id, password);
    }

    public Customer loginCustomer() throws IOException {
//        int roleId = this.menuLogin();
//        return new CustomerDao().read(roleId);
        return null;
    }


}
