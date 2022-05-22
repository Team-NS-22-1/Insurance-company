package main.utility;

import main.domain.insurance.Insurance;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDialogUtil {

    private static FileInputStream in;
    private static FileOutputStream out;
    private static JFrame frame;
    private static FileDialog dialog;
    private static String serverPath = "fileServer/";

    public static String upload(String dirInsurance) throws IOException {
        frame = new JFrame();
        dialog = new FileDialog(frame, "파일 업로드", FileDialog.LOAD);
        dialog.setModal(true);
        dialog.setVisible(true);
        String originPath = dialog.getDirectory()+dialog.getFile();

        String saveDirectory = serverPath + dirInsurance;
        Files.createDirectories(Paths.get(saveDirectory));
        String savePath = saveDirectory + "/" + dialog.getFile();

        try {
            in = new FileInputStream(originPath);
            out = new FileOutputStream(savePath);
            readIOBuffer();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("ERROR :: 파일을 찾을 수 없습니다!");
        }
        return savePath;
    }

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
}
