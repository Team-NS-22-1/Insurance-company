package insuranceCompany.application.global.exception;

/**
 * packageName :  exception
 * fileName : FileException
 * author :  규현
 * date : 2022-05-20
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-20                규현             최초 생성
 */
public class MyFileNotFoundException extends MyException{
    public MyFileNotFoundException() {
        super("\033[1;31mERROR :: 파일을 찾을 수 없습니다!\n\033[0m");
    }

    public MyFileNotFoundException(String message) {
        super(message);
    }

    public MyFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyFileNotFoundException(Throwable cause) {
        super(cause);
    }
}
