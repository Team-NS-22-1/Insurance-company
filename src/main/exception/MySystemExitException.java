package main.exception;

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
public class MySystemExitException extends MyException{

        public MySystemExitException() {

        }

        public MySystemExitException(String message) {
            super(message);
        }

        public MySystemExitException(String message, Throwable cause) {
            super(message, cause);
        }

        public MySystemExitException(Throwable cause) {
            super(cause);
        }

}
