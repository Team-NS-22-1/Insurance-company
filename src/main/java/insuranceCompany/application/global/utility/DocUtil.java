package insuranceCompany.application.global.utility;

import insuranceCompany.application.domain.accident.Accident;
import insuranceCompany.application.domain.accident.accDocFile.AccDocType;
import insuranceCompany.application.global.exception.MyFileException;

import javax.swing.*;
import java.awt.*;
import java.io.*;

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
            dialog.setFile(getExtension(dir));
            dialog.setModal(true);
            dialog.setVisible(true);

            String originPath = dialog.getDirectory()+dialog.getFile();
            if(dialog.getDirectory() == null)
                return null;
            in = new FileInputStream(originPath);
            out = new FileOutputStream(dir);
            readIOBuffer();
        } catch (FileNotFoundException e) {
            throw new MyFileException("ERROR :: 파일을 찾을 수 없습니다!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dir;
    }

    private String getExtension(String path) {
        int lastIndexOf = path.lastIndexOf(".");
        return "*"+path.substring(lastIndexOf);
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
    public static void deleteDir(Accident accident) {
        File file = new File("./AccDocFile/submit/"+accident.getCustomerId()+"/"+accident.getId());

        if( file.exists() ){ //파일존재여부확인

            if(file.isDirectory()){ //파일이 디렉토리인지 확인

                File[] files = file.listFiles();

                for( int i=0; i<files.length; i++){
                    if( files[i].delete() ){
                        System.out.println(files[i].getName()+" 삭제성공");
                    }else{
                        System.out.println(files[i].getName()+" 삭제실패");
                    }
                }

            }
            if(file.delete()){
                System.out.println("디렉토리 삭제 성공");
            }else{
                System.out.println("디렉토리 삭제 실패");
            }

        }else{
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

}
