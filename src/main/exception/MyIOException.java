package main.exception;

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
