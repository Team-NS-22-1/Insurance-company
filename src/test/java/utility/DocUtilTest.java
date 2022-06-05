package utility;

import insuranceCompany.application.domain.accident.InjuryAccident;
import insuranceCompany.application.global.utility.FileDialogUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.*;

import static insuranceCompany.application.global.constant.DocUtilConstants.FILE_UPLOAD_HEAD;

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

    private FileInputStream in;
    private FileOutputStream out;
    private static Frame frame;
    private static FileDialog dialog;
    @Test
    void frameTest() {
        frame = new Frame("BorderLayoutDemo");
        frame.setUndecorated(true); // Remove title bar
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setVisible(true);
        for (int i = 0; i < 10; i++) {
            FileDialog fileDialog = new FileDialog(frame,"파일테스트");
            fileDialog.setVisible(true);
        }
    }

    @Test
    void fuck() {
        frame = new Frame();
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        dialog = new FileDialog(frame, FILE_UPLOAD_HEAD, FileDialog.LOAD);
        dialog.setVisible(true);
    }
    @Test
    void fileName() {

            FileDialog dialog = new FileDialog(new Frame(), "파일 업로드", FileDialog.LOAD);


            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setModal(true);


            String originPath = dialog.getDirectory()+dialog.getFile();
//            if(dialog.getDirectory() == null)
            System.out.println(originPath);




    }
    private void readIOBuffer() throws IOException {
        try {
            int read, bytebuffer=0;
            byte[] buffer = new byte[8192];
            while((read=in.read(buffer)) != -1){
                out.write(buffer, 0 ,read);
                bytebuffer += read;
                if(bytebuffer > 1024*1024){
                    bytebuffer = 0;
                    out.flush();
                }
            }
        } finally {
            out.close();
            in.close();

        }
    }

    @Test
    void deleteDir() {
        InjuryAccident accident = new InjuryAccident();
        accident.setId(1);
        accident.setCustomerId(2);
        FileDialogUtil.deleteDir(accident);
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