package insuranceCompany.application.global.exception;

public class LoginIdFailedException extends MyException {
    public LoginIdFailedException() {
        super("\033[1;31mLOGIN ERROR:: 아이디를 다시 확인해주세요.\033[0m");
    }
}
