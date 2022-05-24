package exception;

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
public class MyFileException extends MyException{
    public MyFileException() {
    }

    public MyFileException(String message) {
        super(message);
    }

    public MyFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyFileException(Throwable cause) {
        super(cause);
    }
}
