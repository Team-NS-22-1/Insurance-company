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
public class InvalidMenuException extends InputException{
    public InvalidMenuException() {
        super("ERROR!! : 올바른 메뉴를 입력해주세요.\n");
    }

    public InvalidMenuException(String message) {
        super("ERROR!! : 올바른 메뉴를 입력해주세요.\n");
    }

    public InvalidMenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMenuException(Throwable cause) {
        super(cause);
    }
}
