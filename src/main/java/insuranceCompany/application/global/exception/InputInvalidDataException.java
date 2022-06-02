package insuranceCompany.application.global.exception;

public class InputInvalidDataException extends InputException {
        public InputInvalidDataException() {
            super("ERROR!! : 유효하지 않은 값을 입력하였습니다.\n");
        }
}
