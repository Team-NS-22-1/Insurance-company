package insuranceCompany.application.global.exception;

/**
 * packageName :  insuranceCompany.application.global.exception
 * fileName : MyInvalidAccessException
 * author :  규현
 * date : 2022-06-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-02                규현             최초 생성
 */
public class MyInvalidAccessException extends MyException{
    public MyInvalidAccessException() {
    }

    public MyInvalidAccessException(String message) {
        super(message);
    }

    public MyInvalidAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyInvalidAccessException(Throwable cause) {
        super(cause);
    }
}
