package exception;

public class InputException extends MyException {

    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputException(Throwable cause) {
        super(cause);
    }

    public static class InputNullDataException extends InputException {
        public InputNullDataException() {
            super("ERROR!! : 입력창에 값을 입력해주세요.\n");
        }
    }

    public static class InputInvalidDataException extends InputException {
        public InputInvalidDataException() {
            super("ERROR!! : 유효하지 않은 값을 입력하였습니다.\n");
        }
    }

    public static class InvalidMenuException extends InputException {
        public InvalidMenuException() {
            super("ERROR!! : 올바른 메뉴를 입력해주세요.\n");
        }
    }

    public static class NoResultantException extends InputException {
        public NoResultantException() {
            super("ERROR!! : 조회 결과가 없습니다.\n");
        }
    }

}
