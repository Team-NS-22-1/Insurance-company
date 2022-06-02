package insuranceCompany.application.global.exception;

/**
 * packageName :  insuranceCompany.application.global.exception
 * fileName : InputNullDataException
 * author :  규현
 * date : 2022-06-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-02                규현             최초 생성
 */
public class InputNullDataException extends InputException {
    public InputNullDataException() {
        super("ERROR!! : 입력창에 값을 입력해주세요.\n");
    }

    public InputNullDataException(String message) {
        super("ERROR!! : 입력창에 값을 입력해주세요.\n");
    }

    public InputNullDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputNullDataException(Throwable cause) {
        super(cause);
    }
}
