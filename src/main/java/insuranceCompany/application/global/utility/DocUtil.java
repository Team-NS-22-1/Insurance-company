package insuranceCompany.application.global.utility;

import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import insuranceCompany.application.global.exception.MyFileNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import static insuranceCompany.application.global.constant.CommonConstants.SLASH;
import static insuranceCompany.application.global.constant.DocUtilConstants.*;
import static insuranceCompany.application.global.constant.ExceptionConstants.FILE_NOT_FOUND;
import static insuranceCompany.application.global.constant.ExceptionConstants.getFileNotFoundMessage;

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
    private static final String submitPath = SUBMIT_PATH;
    private FileInputStream in;
    private FileOutputStream out;
    static {
        instance = new DocUtil();

    }

    public static DocUtil getInstance() {
        return instance;
    }


    public static Window getFrontWindow() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
    }

    private static FileDialog getFileDialog(String title, int type) {
        Window w = getFrontWindow();
        if (w instanceof Frame frame) {
            return new FileDialog(frame, title, type);
        } else {
            return new FileDialog((Dialog)w, title, type);
        }
    }
    public void download(String dir) {

        FileDialog dialog = getFileDialog(FILE_DOWNLOAD_HEAD,FileDialog.SAVE);

        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if(dialog.getDirectory() == null)
            return;
        String saveFilePath = dialog.getDirectory()+dialog.getFile();
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

    public String upload(String dir) {

        try {
            File folder = new File(dir);
            if (!folder.getParentFile().exists()) {
                folder.getParentFile().mkdirs();
            }

//            FileDialog dialog = new FileDialog(this, "파일 업로드", FileDialog.LOAD);
            FileDialog dialog = getFileDialog(FILE_UPLOAD_HEAD,FileDialog.LOAD);

            dialog.setFile(getExtension(dir));
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setModal(true);


            String originPath = dialog.getDirectory()+dialog.getFile();
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

    private String getExtension(String path) {
        int lastIndexOf = path.lastIndexOf(DOT);
        return ASTERISK+path.substring(lastIndexOf);
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
        String directory =  accident.getCustomerId()+SLASH+ accident.getId();
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
    public static void deleteDir(Accident accident) {
        File file = new File(submitPath+accident.getCustomerId()+SLASH+accident.getId());

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

}
