package main.exception;

/**
 * packageName :  main.exception
 * fileName : MyInadequateFormatException
 * author :  규현
 * date : 2022-05-14
 * description : 적절하지 않은 형식을 입력했을 때, 발생하는 예외 클래스
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-14                규현             최초 생성
 */
public class MyInadequateFormatException extends MyException {
    public MyInadequateFormatException() {
    }

    public MyInadequateFormatException(String message) {
        super(message);
    }

    public MyInadequateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyInadequateFormatException(Throwable cause) {
        super(cause);
    }
}
