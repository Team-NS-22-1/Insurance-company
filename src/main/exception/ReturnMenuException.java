package main.exception;

public class ReturnMenuException extends MyException {
    public ReturnMenuException() {
        super("이전 메뉴로 돌아갑니다.\n");
    }
}
