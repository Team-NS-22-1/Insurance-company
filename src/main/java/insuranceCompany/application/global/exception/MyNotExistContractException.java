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
public class MyNotExistContractException extends MyException{
    public MyNotExistContractException() { super("\033[1;31m계약 정보가 존재하지 않습니다.\033[0m");}

    public MyNotExistContractException(String message) {
        super(message);
    }

    public MyNotExistContractException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyNotExistContractException(Throwable cause) {
        super(cause);
    }
}
