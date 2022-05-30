package utility;

import domain.accident.InjuryAccident;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName :  utility
 * fileName : DocUtilTest
 * author :  규현
 * date : 2022-05-30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-30                규현             최초 생성
 */
class DocUtilTest {


    @Test
    void deleteDir() {
        InjuryAccident accident = new InjuryAccident();
        accident.setId(1);
        accident.setCustomerId(2);
        DocUtil.deleteDir(accident);
    }
}