package utility;

import domain.accident.Accident;
import domain.accident.accDocFile.AccDocType;
import exception.MyFileException;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.writer.HWPWriter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * packageName :  utility
 * fileName : DocUtil
 * author :  규현
 * date : 2022-05-20
 * description : 파일의 업로드, 다운로드를 해줄 수 있게 하는 클래스
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-20                규현             최초 생성
 */
public class DocUtil extends JFrame {

    private static DocUtil instance;
    private static final String submitPath = "./AccDocFile/submit/";
    private FileInputStream in;
    private FileOutputStream out;
    static {
        instance = new DocUtil();
    }

    public static DocUtil getInstance() {
        return instance;
    }


    public void download(String dir) {
        FileDialog dialog = new FileDialog(this, "파일 다운로드", FileDialog.SAVE);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setVisible(true);

        if(dialog.getDirectory() == null)
            return;
        String saveFilePath = dialog.getDirectory()+dialog.getFile();
        try {
            in = new FileInputStream(dir);
            out = new FileOutputStream(saveFilePath);
            readIOBuffer();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("ERROR :: 파일을 찾을 수 없습니다!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String upload(String dir) {

        try {
            File folder = new File(dir);
            if (!folder.getParentFile().exists()) {
                folder.getParentFile().mkdirs();
            }

            FileDialog dialog = new FileDialog(this, "파일 업로드", FileDialog.LOAD);
            dialog.setModal(true);
            dialog.setVisible(true);

            String originPath = dialog.getDirectory()+dialog.getFile();
            if(dialog.getDirectory() == null)
                return null;
            in = new FileInputStream(originPath);
            out = new FileOutputStream(dir);
            readIOBuffer();
        } catch (FileNotFoundException e) {
//            throw new RuntimeException("ERROR :: 파일을 찾을 수 없습니다!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dir;
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

    public static void isExist(Accident accident, AccDocType accDocType) {
        String directory =  accident.getCustomerId()+"/"+ accident.getId();
        File folder = new File(submitPath+directory+"/"+accDocType.getDesc()+".hwp");
        if (!folder.exists()) {
            System.out.println(accDocType.getDesc() + "파일이 존재하지 않습니다. ");
        }
    }

}
