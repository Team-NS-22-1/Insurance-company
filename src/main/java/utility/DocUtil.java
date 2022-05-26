package utility;

import domain.accident.Accident;
import domain.accident.accDocFile.AccDocType;
import exception.MyFileException;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.writer.HWPWriter;

import javax.swing.*;
import java.io.*;
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

    static {
        instance = new DocUtil();
    }

    public static DocUtil getInstance() {
        return instance;
    }

    //TODO 보상담당자가 제출된 파일들을 다운로드 받을 수 있는 기능도 만들어야함.

    public void download(AccDocType accDocType) {
        String path = "./AccDocFile/Example"; // path
        String fileName = accDocType.getDesc()+"(예시).hwp";

        String filePath = path+"/"+fileName;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String newPath = chooser.getSelectedFile().getAbsolutePath();
            Scanner sc = new Scanner(System.in);
            System.out.println("저장할 파일 이름을 정해주세요");
            String name = sc.next();

            try {
                HWPFile hwpFile = HWPReader.fromFile((filePath));
                if (hwpFile != null) {
                    HWPFile clonedHWPFile = hwpFile.clone(false);
                    String filename2 = name+".hwp";
                    HWPWriter.toFile(clonedHWPFile, newPath + "/" + filename2);
                    System.out.println(filename2 + "이 성공적으로 다운로드 되었습니다.");
                }
            } catch (Exception e) {
                throw new MyFileException(e);
            }
        }
    }

    public static void isExist(Accident accident, AccDocType accDocType) {
        String directory =  accident.getCustomerId()+"/"+ accident.getId();
        File folder = new File(submitPath+directory+"/"+accDocType.getDesc()+".hwp");
        if (!folder.exists()) {
            System.out.println(accDocType.getDesc() + "파일이 존재하지 않습니다. ");
        }
    }

    public String upload(String directory, AccDocType accDocType) {

        File folder = new File(submitPath+directory);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (accDocType == AccDocType.PICTUREOFSITE) {
            return uploadImg(directory);
        }else
            return uploadHWP(directory,accDocType);

    }

    //TODO 확장자가 달라도 같은 파일로 인식하도록 하고싶은데.
    private String uploadImg(String directory) {
        String dir = "";
        try {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                FileInputStream fileStream = null; // 파일 스트림

                fileStream = new FileInputStream(path);// 파일 스트림 생성
                //버퍼 선언
                BufferedInputStream bis = new BufferedInputStream(fileStream);

                String extension = "";
                int i = path.lastIndexOf('.');
                if (i > 0) {
                    extension = path.substring(i);
                }

                byte[] readBuffer = new byte[1024 * 1024];
                File copy = new File(submitPath + directory + "/교통사고현장사진" + extension);
                dir = submitPath + directory + "/교통사고현장사진" + extension;
                BufferedOutputStream bs = null;

                bs = new BufferedOutputStream(new FileOutputStream(copy));
                while (bis.read(readBuffer) != -1) {
                    bs.write(readBuffer); //Byte형으로만 넣을 수 있음
                }
                bs.flush();
            } else {
                dir = "close";
            }
        } catch (IOException e) {
            throw new MyFileException(e);
        }
        return dir;
    }

    private String uploadHWP(String directory,AccDocType accDocType) {
        String dir = "";
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String path = chooser.getSelectedFile().getAbsolutePath();
            try {
                HWPFile hwpFile = HWPReader.fromFile((path));
                if (hwpFile != null) {
                    HWPFile clonedHWPFile = hwpFile.clone(false);
                    String filename2 = accDocType.getDesc() + ".hwp";
                    HWPWriter.toFile(clonedHWPFile, submitPath + directory + "/" + filename2);
                    dir = submitPath + directory + "/" + filename2;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new MyFileException(e);
            }
        } else {
            dir = "close";
        }
        return dir;
    }
}
