package insuranceCompany.application.global.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  insuranceCompany.application.global.exception
 * fileName : InputInvalidDataExceptionTest
 * author :  규현
 * date : 2022-06-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-02                규현             최초 생성
 */
class InputInvalidDataExceptionTest {
    
    @Test
    void ExceptionCatch() {

        try {
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            throw new MyIllegalArgumentException(e);
        } catch (MyIllegalArgumentException e) {
            System.out.println("성공");
        }
        
    }

}