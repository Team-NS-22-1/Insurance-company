package insuranceCompany.application.global.exception;

public class LoginPwFailedException extends MyException {
    public LoginPwFailedException() {
        super("LOGIN ERROR:: 패스워드를 다시 확인해주세요.");
    }
}