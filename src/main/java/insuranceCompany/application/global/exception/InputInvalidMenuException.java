package insuranceCompany.application.global.exception;

/**
 * packageName :  insuranceCompany.application.global.exception
 * fileName : InvalidMenuException
 * author :  규현
 * date : 2022-06-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-02                규현             최초 생성
 */
public class InputInvalidMenuException extends InputException{
    public InputInvalidMenuException() {
        super("ERROR!! : 올바른 메뉴를 입력해주세요.\n");
    }

    public InputInvalidMenuException(String message) {
        super("ERROR!! : 올바른 메뉴를 입력해주세요.\n");
    }

    public InputInvalidMenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputInvalidMenuException(Throwable cause) {
        super(cause);
    }
}
