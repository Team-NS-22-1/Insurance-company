package insuranceCompany.application.global.exception;

public class InputException extends MyException {
    public InputException() {
    }
    public InputException(String message) {
        super(message);
    }
    public InputException(String message, Throwable cause) {
        super(message, cause);
    }
    public InputException(Throwable cause) {
        super(cause);
    }
}
