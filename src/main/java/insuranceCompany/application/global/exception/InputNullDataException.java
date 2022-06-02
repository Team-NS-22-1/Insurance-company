package insuranceCompany.application.global.exception;

public class InputNullDataException extends InputException {
    public InputNullDataException() {
        super("ERROR!! : 입력창에 값을 입력해주세요.\n");
    }
}
