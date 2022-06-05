package insuranceCompany.application.global.exception;

/**
 * packageName :  main.exception
 * fileName : MyIOException
 * author :  규현
 * date : 2022-05-18
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-18                규현             최초 생성
 */
public class MyIOException extends MyException{
    public MyIOException() {
        super("\033[1;31mERROR:: IO 시스템에 장애가 발생하였습니다!\n프로그램을 종료합니다...\033[0m");
    }

    public MyIOException(String message) {
        super(message);
    }

    public MyIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyIOException(Throwable cause) {
        super(cause);
    }
}
