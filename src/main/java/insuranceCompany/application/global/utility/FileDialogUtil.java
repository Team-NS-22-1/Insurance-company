package insuranceCompany.application.global.utility;

import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import insuranceCompany.application.global.exception.MyFileNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static insuranceCompany.application.global.constant.CommonConstants.SLASH;
import static insuranceCompany.application.global.constant.DocUtilConstants.*;
import static insuranceCompany.application.global.constant.ExceptionConstants.FILE_NOT_FOUND;
import static insuranceCompany.application.global.constant.ExceptionConstants.getFileNotFoundMessage;

public class FileDialogUtil {

    private static FileInputStream in;
    private static FileOutputStream out;
    private static Frame frame;
    private static FileDialog dialog;
    private static String serverPath = "fileServer/";

    private static final String submitPath = SUBMIT_PATH;

    public static String upload(String dirInsurance) throws IOException {
        frame = new Frame();
        frame.setUndecorated(true); // 서순 중요!!! 제일 위에 배치해야 함.
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.pack();
        dialog = new FileDialog(frame, FILE_UPLOAD_HEAD, FileDialog.LOAD);
        dialog.setVisible(true);
        String fileName = dialog.getFile();
        String originPath = dialog.getDirectory()+fileName;

        String saveDirectory = serverPath + dirInsurance;
        Files.createDirectories(Paths.get(saveDirectory));
        String savePath = saveDirectory + "/" + fileName;

        if(dialog.getDirectory() == null) {
            frame.dispose();
            return null;
        }

        try {
            in = new FileInputStream(originPath);
            out = new FileOutputStream(savePath);
            readIOBuffer();
        } catch (FileNotFoundException e) {
            throw new MyFileNotFoundException();
        }
        return fileName;
    }

    public static String uploadAccidentDocumentFile(String dir) {

        try {
            File folder = new File(dir);
            if (!folder.getParentFile().exists()) {
                folder.getParentFile().mkdirs();
            }


            frame = new JFrame("BorderLayoutDemo");
            frame.setUndecorated(true); // Remove title bar
            frame.pack();
            frame.setVisible(true);

            dialog = new FileDialog(frame, FILE_UPLOAD_HEAD, FileDialog.LOAD);

            dialog.setFile(getExtension(dir));
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setModal(true);


            String originPath = getFullPath(dialog.getDirectory(), dialog.getFile());
            if(dialog.getDirectory() == null)
                return null;
            in = new FileInputStream(originPath);
            out = new FileOutputStream(dir);
            readIOBuffer();
        } catch (FileNotFoundException e) {
            throw new MyFileNotFoundException(FILE_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dir;
    }

    private static String getFullPath(String directory, String fileName) {
        return directory + fileName;
    }

    public static void download(String dir) {
        frame = new JFrame("BorderLayoutDemo");
        frame.setUndecorated(true); // Remove title bar

        frame.pack();
        frame.setVisible(true);

        dialog = new FileDialog(frame,FILE_DOWNLOAD_HEAD,FileDialog.SAVE);

        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if(dialog.getDirectory() == null)
            return;
        String saveFilePath = getFullPath(dialog.getDirectory(), dialog.getFile());
        try {
            in = new FileInputStream(dir);
            out = new FileOutputStream(saveFilePath);
            readIOBuffer();
        } catch (FileNotFoundException e) {
            throw new MyFileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    public static void download(String filePath) throws IOException {
        String originFileName = "보험상품신고서.pdf";
        String originFilePath = "src/보험상품신고서.pdf";

        frame = new JFrame();
        dialog = new FileDialog(frame, "파일 다운로드", FileDialog.SAVE);
        dialog.setFile(originFileName);
        dialog.setModal(true);
        dialog.setVisible(true);

        if(dialog.getDirectory() == null)
            return;

        String saveFilePath = dialog.getDirectory()+dialog.getFile();

        try {
            in = new FileInputStream(originFilePath);
            out = new FileOutputStream(saveFilePath);
            readIOBuffer();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("ERROR :: 파일을 찾을 수 없습니다!");
        }
    }
    */

    public static void isExist(Accident accident, AccDocType accDocType) {
        String directory = getDirectory(accident);
        String extension = HWP_EXTENSION;
        if(accDocType==AccDocType.PICTUREOFSITE)
            extension = JPEG_EXTENSION;
        File folder = new File(submitPath+directory+ SLASH +accDocType.getDesc()+extension);
        try {
            if (!folder.exists()) {
                throw new MyFileNotFoundException(getFileNotFoundMessage(accDocType.getDesc()));
            }
        } catch (MyFileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getDirectory(Accident accident) {
        return accident.getCustomerId() + SLASH + accident.getId();
    }

    public static void deleteDir(Accident accident) {
        File file = new File(submitPath+getDirectory(accident));

        try {
            if (file.exists()) { //파일존재여부확인
                if (file.isDirectory()) { //파일이 디렉토리인지 확인
                    File[] files = file.listFiles();
                    for (File value : files) value.delete();
                }
            } else {
                throw new MyFileNotFoundException(FILE_NOT_FOUND);
            }
        } catch (MyFileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void readIOBuffer() throws IOException {
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
            frame.dispose();
        }
    }
    private static String getExtension(String path) {
        int lastIndexOf = path.lastIndexOf(DOT);
        return ASTERISK+path.substring(lastIndexOf);
    }
}
