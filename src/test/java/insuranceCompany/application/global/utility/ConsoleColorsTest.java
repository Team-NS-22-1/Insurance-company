package insuranceCompany.application.global.utility;

import insuranceCompany.application.global.exception.MyInvalidAccessException;
import org.junit.jupiter.api.Test;

import static insuranceCompany.application.global.constant.ExceptionConstants.INPUT_DATA_ON_LIST;
import static org.junit.jupiter.api.Assertions.*;

class ConsoleColorsTest {

    @Test
    void colorException() {
        String error = "\033[47m\033[1;31mERROR!! : 리스트에 있는 아이디를 입력해주세요.";
        try {
            throw new MyInvalidAccessException(error);
        } catch (MyInvalidAccessException e) {
            System.out.println(e.getMessage());
        }
    }

}