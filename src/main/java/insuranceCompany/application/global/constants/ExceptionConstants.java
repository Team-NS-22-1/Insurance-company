package insuranceCompany.application.global.constants;

/**
 * packageName :  insuranceCompany.application.global.constants
 * fileName : ExceptionConstants
 * author :  규현
 * date : 2022-06-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-02                규현             최초 생성
 */
public class ExceptionConstants {

    //이미 값이 있는 예외클래스들
    public static final String INPUTINVALIDMENUEXCEPTION = "ERROR!! : 올바른 메뉴를 입력해주세요.\n";

    public static final String CARBREAKDOWNEXCEPTION ="ERROR!! : 자동차 고장은 보상금 청구가 되지 않습니다.";
    public static final String INPUTDATEONLIST = "ERROR!! : 리스트에 있는 아이디를 입력해주세요.";
    public static final String NOINSURANCEABOUNTACCIDENT="ERROR!! : 해당 사고를 접수하기 위한 보험에 가입되어있지 않습니다. 다시 확인해주세요.";
    public static final String INPUTWRONGFORMAT = "ERROR!! : 정확한 형식의 값을 입력해주세요.";


}
