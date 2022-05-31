package insuranceCompany.application.global.exception;

/**
 * packageName :  main.exception
 * fileName : MyIllegalArgumentException
 * author :  규현
 * date : 2022-05-12
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-12                규현             최초 생성
 */
public class MyIllegalArgumentException extends MyException{

        public MyIllegalArgumentException() {

        }

        public MyIllegalArgumentException(String message) {
            super(message);
        }

        public MyIllegalArgumentException(String message, Throwable cause) {
            super(message, cause);
        }

        public MyIllegalArgumentException(Throwable cause) {
            super(cause);
        }

}
