package insuranceCompany.application.global.exception;

public class LoginIdFailedException extends MyException {
    public LoginIdFailedException() {
        super("LOGIN ERROR:: 아이디를 다시 확인해주세요.");
    }
}
