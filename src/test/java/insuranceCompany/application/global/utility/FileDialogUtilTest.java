package insuranceCompany.application.global.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileDialogUtilTest {

    @Test
    void 파일이름추출하기() {
        String filename = "accdocfile/example/교통사고 사실확인원(예시).hwp";
        int i = filename.lastIndexOf("/");
        String substring = filename.substring(i+1);
        System.out.println(substring);
    }

}