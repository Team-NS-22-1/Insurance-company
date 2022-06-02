package insuranceCompany.application.global.exception;

/**
 * packageName :  insuranceCompany.application.global.exception
 * fileName : InputInvalidDataException
 * author :  규현
 * date : 2022-06-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-02                규현             최초 생성
 */
public class InputInvalidDataException extends InputException{
    public InputInvalidDataException() {
        super("ERROR!! : 유효하지 않은 값을 입력하였습니다.\n");
    }

    public InputInvalidDataException(String message) {
        super(message);
    }

    public InputInvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputInvalidDataException(Throwable cause) {
        super("ERROR!! : 유효하지 않은 값을 입력하였습니다.\n",cause);
    }
}
