package main.exception;

/**
 * packageName :  main.exception
 * fileName : MyCloseSequence
 * author :  규현
 * date : 2022-05-15
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-15                규현             최초 생성
 */
public class MyCloseSequence extends MyException{
    public MyCloseSequence() {
    }

    public MyCloseSequence(String message) {
        super(message);
    }

    public MyCloseSequence(String message, Throwable cause) {
        super(message, cause);
    }

    public MyCloseSequence(Throwable cause) {
        super(cause);
    }
}
