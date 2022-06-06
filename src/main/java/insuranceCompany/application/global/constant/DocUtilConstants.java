package insuranceCompany.application.global.constant;

public class DocUtilConstants {
    private DocUtilConstants() {}

    public static final String ASTERISK = "*";
    public static final String DOT = ".";
    public static final String SUBMIT_PATH = "./AccDocFile/submit/";
    public static String getSubmitPath(int customerId, int accidentId, String format) {
        return   SUBMIT_PATH+customerId+"/"+ accidentId+"/"+ format;
    }
    public static final String JPG_EXTENSION = ".jpg";
    public static final String HWP_EXTENSION = ".hwp";

    public static final String FILE_DOWNLOAD_HEAD = "파일 다운로드";
    public static final String FILE_UPLOAD_HEAD = "파일 업로드";
}
