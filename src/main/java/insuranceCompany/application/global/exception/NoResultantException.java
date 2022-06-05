package insuranceCompany.application.global.exception;

/**
 * packageName :  insuranceCompany.application.global.exception
 * fileName : NoResultantException
 * author :  규현
 * date : 2022-06-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-02                규현             최초 생성
 */
public class NoResultantException extends InputException {

    public NoResultantException() {
        super("\033[1;31mERROR!! : 조회 결과가 없습니다.\n");
    }

    public NoResultantException(String message) {
       super(message);
    }

    public NoResultantException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResultantException(Throwable cause) {
        super(cause);
    }
}
