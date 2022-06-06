package insuranceCompany.application.global.constant;

import static insuranceCompany.application.global.constant.CommonConstants.ZERO_MESSAGE;
import static insuranceCompany.application.global.utility.ConsoleColors.RED_BOLD;
import static insuranceCompany.application.global.utility.ConsoleColors.RESET;

public class CompensationViewLogicConstants {

    //COMMON
    public static final String COMPENSATION_MENU_HEAD = "<< 보상팀 메뉴 >>";
    public static final String READ_ACCIDENT_LIST = "사고목록조회";
    public static final String INVESTIGATE_DAMAGE = "손해조사";
    public static final String ASSESS_DAMAGE = "손해사정";

    public static final String [] MENU_ELEMENTS_COMP_VIEW_LOGIC = {READ_ACCIDENT_LIST,INVESTIGATE_DAMAGE,ASSESS_DAMAGE};
    public static final String SELECT_ACCIDENT = "<< 접수된 사고를 선택하세요. >> " + ZERO_MESSAGE;
    public static final String ACCIDENT_ID_QUERY = "사고 ID : ";
    public static final String SHOW_CUSTOMER_NAME = "접수자 명";
    public static final String SHOW_CAR_NO = "차량 번호 : ";
    public static final String SHOW_OPPOSING_PHONE = "상대방 차주 연락처 : ";
    public static final String SHOW_PLACE_ADDRESS = "사고 주소 : ";
    public static final String SHOW_INJURY_SITE = "부상 부위 : ";



    //손해조사
    public static final String FINISH_DOWNLOAD = "다운로드 종료";
    public static final String INVESTIGATE_ACCIDENT_QUERY = "사고 조사 보고서를 제출해주세요";
    public static final String LOSS_RESERVE_QUERY = "지급 준비금을 입력해주세요 : ";
    public static final String ERROR_RATE_QUERY = "과실비율을 입력해주세요 (0~100) : ";


    public static final String ASSESS_DAMAGE_QUERY = "손해 사정을 진행하시겠습니까? (Y/N) 입력 : ";
    //손해사정
    public static final String UPLOAD_ASSESS_DAMAGE = "손해사정서를 업로드해주세요.";
    public static String getInputCompensation(long lossReserve) {
        return "지급할 보상금을 입력해주세요. (입력하신 지급 준비금 : " + lossReserve + ") : ";
    }
    public static final String REJECT_ASSESS_DAMAGE = "손해 사정서가 반려되었습니다.";
    public static final String NO_ERROR = "고객 과실이 0이기 때문에 보상금을 지급하지 않습니다.";

    //exception
    public static final String LOSS_RESERVE_EXCEPTION = RED_BOLD+"ERROR!! : 지급 준비금이 입력되지 않은 사고이기 떄문에 손해 사정이 불가능합니다."+RESET;
    public static final String INVESTIGATE_ACCIDENT_EXCEPTION =RED_BOLD+"ERROR!! : 사고 조사 보고서가 업로드되지 않은 사고이기 때문에 손해 사정이 불가능합니다."+RESET;
    public static final String ERROR_RATE_EXCEPTION = RED_BOLD+"ERROR!! : 과실 비율이 입력되지 않은 사고이기 때문에 손해 사정이 불가능합니다."+RESET;

    public static String getDownloadDocExQuery(String format) {
        return format+" 양식을 다운로드 받겠습니까? (Y/N) "+ZERO_MESSAGE+" 입력 : ";
    }

    public static final String INPUT_ACCOUNT = RED_BOLD+"ERROR !! : 계좌 입력을 하지 않았습니다."+RESET;

}

