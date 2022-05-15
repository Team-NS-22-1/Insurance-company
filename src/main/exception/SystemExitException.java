package main.exception;

public class SystemExitException extends MyException {
    public SystemExitException(){
        super("EXIT!! : 시스템을 종료합니다.");
    }
}
