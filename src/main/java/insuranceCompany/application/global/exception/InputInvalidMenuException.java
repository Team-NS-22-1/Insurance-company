package insuranceCompany.application.global.exception;

import static insuranceCompany.application.global.constant.ExceptionConstants.INPUTINVALIDMENUEXCEPTION;

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
        super(INPUTINVALIDMENUEXCEPTION);
    }

    public InputInvalidMenuException(String message) {
        super(INPUTINVALIDMENUEXCEPTION);
    }

    public InputInvalidMenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputInvalidMenuException(Throwable cause) {
        super(cause);
    }
}
