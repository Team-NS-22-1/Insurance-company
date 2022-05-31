package insuranceCompany.application.login;

import insuranceCompany.application.global.utility.MyBufferedReader;
import insuranceCompany.application.global.exception.InputException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Login {

    private UserListImpl userList = new UserListImpl();

    public Login() throws IOException {
    }

    public Object loginMenu() throws IOException {
        MyBufferedReader br = new MyBufferedReader(new InputStreamReader(System.in));
        boolean forWhile = true;
        String inputUserId = "", inputPw = "";
        Object result = null;
        while(forWhile){
            try {
                System.out.println("<< 로그인 >>");
                inputUserId = (String) br.verifyRead("아이디: ", inputUserId);
                inputPw = (String) br.verifyRead("비밀번호: ", inputPw);
                int loginId = login(inputUserId, inputPw);
                result = (loginId > 0) ? checkUser(loginId) : (
                        (loginId == -1) ? "패스워드 실패" :
                                (loginId == -2) ? "아이디 실패" : "로그인 실패");
                if(result instanceof String)
                    System.out.println((String) result);
                else forWhile = false;
            }
            catch (InputException.InputNullDataException |
                   InputException.InputInvalidDataException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    private int login(String inputId, String inputPw) {
        ArrayList<User> userArrayList = this.userList.getUserListToArrayList();
        for(User user : userArrayList){
            if(user.getUserId().equals(inputId)){
                if(user.getPassword().equals(inputPw))
                    return user.getId(); // 로그인 성공
                else
                    return -1; // 패스워드 실패
            }
            else
                return -2; // 아이디 실패
        }
        return -3; // 로그인 실패
    }

    private Object checkUser(int loginId){
        User user = this.userList.read(loginId);
        if(user.getCustomerId() > 0){
//            return customerListImpl.read(user.getCustomerId());
        }
        else if(user.getEmployeeId() > 0){
//            return employeeListImpl.read(user.getEmployeeId());
        }
        return null;
    }
}
