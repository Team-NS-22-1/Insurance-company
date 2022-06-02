package insuranceCompany.application.global.exception;

public class NoResultantException extends InputException {
    public NoResultantException() {
        super("ERROR!! : 조회 결과가 없습니다.\n");
    }

}
