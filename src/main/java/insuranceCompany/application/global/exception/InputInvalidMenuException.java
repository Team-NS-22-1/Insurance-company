package insuranceCompany.application.global.exception;

public class InputInvalidMenuException extends InputException {
    public InputInvalidMenuException() {
        super("ERROR!! : 올바른 메뉴를 입력해주세요.\n");
    }
}
