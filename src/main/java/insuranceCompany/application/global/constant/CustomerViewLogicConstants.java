package insuranceCompany.application.global.constant;

/**
 * packageName :  insuranceCompany.application.global.constants
 * fileName : CustomerViewLogicConstants
 * author :  규현
 * date : 2022-06-02
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-06-02                규현             최초 생성
 */
public class CustomerViewLogicConstants {


    //Main Menu
    public static final String CUSTOMER_MENU = "<< 고객메뉴 >>";
    public static final String SIGN_IN_INSURANCE = "보험가입";
    public static final String PAY_PREMIUM = "보험료납입";
    public static final String REPORT_ACCIDENT = "사고접수";
    public static final String CLAIM_COMPENSATION = "보상금청구";

    // ABOUT PAYMENT COMMON
    public static final String PAY_MENU = "<< 결제 메뉴 >>";
    public static final String DO_PAY = "결제하기";
    public static final String SET_PAYMENT = "결제수단 설정하기";
    public static final String ADD_ACCOUNT_MENU_HEAD = "결제수단 추가하기";
    public static final String NO_PAYMENT_ON_CONTRACT = "[알림] 해당 계약에 대해 결제 수단 정보가 없습니다. 설정해주세요.";
    public static final String NO_PAYMENT_ON_CUSTOMER = "[알림] 현재 계정에 등록된 결제 수단이 존재하지 않습니다.";
    public static final String SUCCESS_REGISTER_PAYMENT = "결제 수단이 추가되었습니다.";
    public static final String CANCEL_REGISTER_PAYMENT = "결제 수단 등록을 취소하셨습니다.";
    public static final String CONTRACT_ID_LABEL = "[ID] : ";
    public static final String CONTRACT_NAME_LABEL = " 이름 : ";
    public static final String CONTRACT_PREMIUM_LABEL =" 보험료 : ";
    public static final String PAYMENT_LIST_MENU = "<< 결제수단 목록 >>";


    // ABOUT CARD
    public static final String REGISTER_CARD = "카드 추가하기";
    public static final String SELECT_CARD_TYPE = "카드사 선택";
    public static final String CARD_NO_EX = "카드 번호 (예시 : ****-****-****-****) {4자리 숫자와 - 입력}";
    public static final String SELECT_CARD_TYPE_NO = "카드사 번호 : ";
    public static final String CVC_EX = "CVC (예시 : *** {3자리 숫자}) ";
    public static final String EXPIRY_DATE = "만료일";
    public static final String MONTH = "월 : ";
    public static final String YEAR_EX = "년 (예시 : ****) {4개 숫자 입력 && 202* ~ 203* 까지의 값 입력} : ";
    public static final String REGISTER_CARD_INFO = "카드 정보를 등록하시겠습니까? (Y/N)";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DAY_EX = "01/";


    // ABOUT ACCOUNT
    public static final String REGISTER_ACCOUNT = "계좌 추가하기";
    public static final String SELECT_BANK = "은행사 선택하기";
    public static String showAccountNoEX(String format) {
        return "계좌 번호 입력하기 : (예시 -> " + format + ")";
    }
    public static final String REGISTER_ACCOUNT_INFO = "계좌 정보를 등록하시겠습니까? (Y/N)";


    public static final String CONTRACT_LIST = "<< 가입된 계약 목록 >>";

    // report accident
    public static final String ACCIDENT_MENU = "<< 사고 종류 선택 >>";
    public static final String CAR_ACCIDENT = "자동차 사고";
    public static final String CAR_BREAKDOWN = "자동차 고장";
    public static final String INJURY_ACCIDENT = "상해 사고";
    public static final String FIRE_ACCIDENT = "화재 사고";
    public static final String DAY = "일 : ";
    public static final String HOUR = "시 : ";
    public static final String MINUTE = "분 : ";
    public static final String ADDRESS = "사고 장소 : ";
    public static final String CAR_NO_EX = "차 번호 (ex : __-**_-**** (_ : 한글, * : 숫자)) : ";
    public static final String OPOSSING_PHONE = "상대방 연락처 : ";
    public static final String REQUEST_ON_SITE = "현장 출동 요청을 하시겠습니까? (Y/N) : ";
    public static final String SYMPTOM = "고장 증상 : ";
    public static final String INJURY_SITE = "부상 부위 : ";
    public static final String REPORT_ACCIDENT_INFO = "<< 사고 접수 정보 >> (exit: 시스템 종료)";
    public static final String INPUT_ACCIDENT_DATE = "사고 일시를 입력해주세요";
    public static final String INPUT_ACCIDENT_ID = "사고 ID 입력 : ";
    public static final String DATE_FORMAT_HOUR_MINUTE = "yyyy/MM/dd HH:mm";

    // 보상금 접수
    public static String getSubmitDocQuery(String format) {
        return format+"를 제출하시겠습니까?(Y/N) 입력 : ";
    }
    public static String getSubmitDocCancel(String format) {
        return format+"의 제출을 취소하셨습니다.";
    }
    public static String getSubmitDoc(String format) {
        return format+"를 제출해주세요.";
    }
    public static String getDownloadDocExQuery(String format) {
        return format+" 양식을 다운로드 받겠습니까>?(Y/N) 입력 : ";
    }
    public static String getExDirectory(String format) {
        return "./AccDocFile/Example/" + format+"(예시).hwp";
    }
    public static final String CHANGE_COMP_QUERY = "보상처리담당자를 변경하실 수 있습니다. 하시겠습니까? (Y/N) 입력 : ";
    public static final String INPUT_COMPLAIN = "변경 사유를 입력해주세요 : ";
    public static final String SUCCESS_CHANGE_COMP_EMPLOYEE = "보상처리담당자 변경이 완료되었습니다.";
    public static final String FAIL_TO_FINISH_CLAIM_COMPENSATION = "추후에 미제출한 정보들을 제출해주세요.";

}
