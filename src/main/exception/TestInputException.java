package main.exception;

public class TestInputException extends RuntimeException{

    public TestInputException(String message) {
        super(message);
    }

    public static class InputNullDataException extends TestInputException {
        public InputNullDataException() {
            super("ERROR!! : 입력창에 값을 입력해주세요.\n");
        }
    }

    public static class InputInvalidDataException extends TestInputException {
        public InputInvalidDataException() {
            super("ERROR!! : 유효하지 않은 값을 입력하였습니다.\n");
        }
    }

    public static class InvalidMenuException extends TestInputException {
        public InvalidMenuException() {
            super("ERROR!! : 올바른 메뉴를 입력해주세요.\n");
        }
    }

}
