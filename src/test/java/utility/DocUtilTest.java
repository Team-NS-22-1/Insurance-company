package utility;

import insuranceCompany.application.domain.accident.InjuryAccident;
import insuranceCompany.application.global.utility.DocUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

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
        Assertions.assertEquals(1,accident.getId());
    }

    @Test
    void findExtension() {
        String path = "안녕하세요.hwp";
        int i = path.lastIndexOf(".");
        String sub = path.substring(i);
        System.out.println(sub);
    }
    
    @Test
    void findExtensionWithMulitpleDot() {
        String path = "안.녕.하.세.요.hwp";
        int i = path.lastIndexOf(".");
        String sub = path.substring(i);
        System.out.println(sub);
    }

    @Test
    void 확장자필터적용하기() {
        FileDialog f = new FileDialog(new Frame(), "테스트");
        f.setFile("*.hwp");
        f.setVisible(true);

        String originPath = f.getDirectory()+f.getFile();
        System.out.println(originPath);



    }

    
    @Test
    void 실행테스트() {
        System.out.println("안녕하세요");
        System.out.println("test");

        tester t = new tester();
        t.setName("hello");
        t.setAge(5);
        System.out.println(t);
    }

    @Data
    static class tester{
        private String name;
        private int age;
    }
}