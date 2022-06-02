package insuranceCompany.application.global.constants;

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
    // COMMON
    public static final String CANCEL = "취소하셨습니다.";
    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String ZEROMESSAGE = "0. 취소하기";
    public static final String ZERO = "0";
    public static final String EXITMESSAGE = "exit : 종료하기";
    public static final String EXIT = "EXIT";

    //Main Menu
    public static final String CUSTOMERMENU = "<<고객메뉴>>";
    public static final String SIGNININSURANCE = "보험가입";
    public static final String PAYPREMIUM = "보험료납입";
    public static final String REPORTACCIDENT = "사고접수";
    public static final String CLAIMCOMPENSATION = "보상금청구";

    // ABOUT PAYMENT COMMON
    public static final String PAYHEAD = "<<결제 메뉴>>";
    public static final String DOPAY = "결제하기";
    public static final String SETPAMENT = "결제수단 설정하기";
    public static final String ADDACCOUNTMENUHEAD = "결제수단추가하기";
    public static final String NOPAYMENTONCONTRACT = "해당 계약에 대해 결제 수단 정보가 없습니다. 설정해주세요.";
    public static final String NOPAYMENTONCUSTOMER = "등록된 결제 수단이 없습니다. 먼저 결제수단을 새로 추가해주세요";
    public static final String SUCEESSREGISTERPAYMENT = "결제 수단이 추가되었습니다.";
    public static final String CANCELREGISTERPAYMENT = "결제 수단 등록을 취소하셨습니다.";


    // ABOUT CARD
    public static final String REGISTERCARD = "카드 추가하기";
    public static final String SELECTCARDTYPE = "카드사 선택";
    public static final String CARDNOEX = "카드 번호 : (예시 : ****-****-****-****) {4자리 숫자와 - 입력}";
    public static final String SELECTCARDTYPENO = "카드사 번호 : ";
    public static final String CVCEX = "CVC : (예시 : *** {3자리 숫자})";
    public static final String EXPIRYDATE = "만료일";
    public static final String MONTH = "월 : ";
    public static final String YEAREX = "년 : (예시 : ****) {4개 숫자 입력 && 202* ~ 203* 까지의 값 입력}";
    public static final String REGISTERCARDINFO = "카드 정보를 등록하시겠습니까? (Y/N)";
    public static final String DATEFORMATE = "dd/MM/yyyy";


    // ABOUT ACCOUNT
    public static final String REGISTERACCOUNT = "계좌 추가하기";
    public static final String SELECTBANK = "은행사 선택하기";
    public static String showAccountNoEX(String format) {
        return "계좌 번호 입력하기 : (예시 -> " + format + ")";
    }
    public static final String REGISTERACCOUNTINFO = "계좌 정보를 등록하시겠습니까? (Y/N)";


    public static final String CONTRACTLIST = "<<가입된 계약 목록>>";

    // report accident
    public static final String ACCIDENTMENU = "<< 사고 종류 선택 >>";
    public static final String CARACCIDENT = "자동차 사고";
    public static final String CARBREAKDOWN = "자동차 고장";
    public static final String INJURYACCIDENT = "상해 사고";
    public static final String FIREACCIDENT = "화재 사고";
    public static final String DAY = "일 : ";
    public static final String HOUR = "시 : ";
    public static final String MINUTE = "분 : ";
    public static final String ADDRESS = "사고 장소 : ";
    public static final String CARNOEX = "차 번호 (ex : __-**_-**** (_ : 한글, * : 숫자)) : ";
    public static final String OPOSSINGPHONE = "상대방 연락처 : ";
    public static final String REQUESTONSITE = "현장 출동 요청을 하시겠습니까? (Y/N) : ";
    public static final String SYMPTOM = "고장 증상 : ";
    public static final String INJURYSITE = "부상 부위 : ";
    public static final String REPORTACCIDENTINFO = "<< 사고 접수 정보 >> (exit: 시스템 종료)";
    public static final String INPUTACCIDENTDATE = "사고 일시를 입력해주세요";
    public static final String INPUTACCIDENTID = "사고 ID 입력 : ";

    // 보상금 접수
    public static String getSubmitDocQuery(String format) {
        return format+"를 제출하시겠습니까?(Y/N)";
    }
    public static String getSubmitDocCancel(String format) {
        return format+"의 제출을 취소하셨습니다.";
    }
    public static String getSubmitDoc(String format) {
        return format+"를 제출해주세요.";
    }
    public static String getDownloadDocExQuery(String format) {
        return format+" 양식을 다운로드 받겠습니까>?(Y/N)";
    }
    public static String getExDirectory(String format) {
        return "./AccDocFile/Example/" + format+"(예시).hwp";
    }
    public static final String CHANGECOMPQUERY = "보상처리담당자를 변경하실 수 있습니다. 하시겠습니까?(Y/N)";
    public static final String INPUTCOMPLAIN = "변경 사유를 입력해주세요 : ";
    public static final String SUCESSCHANGECOMPEMPLOYEE = "보상처리담당자 변경이 완료되었습니다.";

}
