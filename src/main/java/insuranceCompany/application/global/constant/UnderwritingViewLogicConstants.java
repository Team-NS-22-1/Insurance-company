package insuranceCompany.application.global.constant;

public class UnderwritingViewLogicConstants {

    // Menu
    public static final String UNDERWRITING_MENU = "<<언더라이팅팀메뉴>>";
    public static final String UNDERWRITING_MENU_ELEMENTS = "인수심사";

    // Method selectInsuranceType()
    public static final String INSURANCE_TYPE_MENU = "<<보험 종류 선택>>";
    public static final String[] INSURANCE_TYPE_MENU_ELEMENTS = {"건강보험", "자동차보험", "화재보험"};

    // Method readContracts()
    public static final String READ_CONTRACTS_COLUMN = "계약 ID | 고객 이름 | 인수심사상태";
    public static final String INPUT_CONTRACT_ID = "<<인수심사할 계약 ID를 입력하세요.>>";
    public static final String SHOW_CONTRACT_INFO = "<<계약 정보>>";

    // Method selectUwState()
    public static final String SELECT_UNDERWRITING_STATE = "<<인수심사결과 선택>>";
    public static final String[] SELECT_UNDERWRITING_STATE_MENU = {"승인", "거절", "보류", "계약 목록 조회"};
    public static final String INPUT_UNDERWRITING_REASON_MENU = "<<인수사유를 입력해주세요.>>";
    public static final String INPUT_UNDERWRITING_REASON = "인수사유: ";
    
    // Method confirmUnderWriting()
    public static final String CONFIRM_UNDERWRITING_MENU = "<<인수심사 결과를 반영하시겠습니까?>>";
    public static final String[] CONFIRM_UNDERWRITING_MENU_ELEMENTS = {"예", "아니오"};
    public static final String CONFIRM_UNDERWRITING_MESSAGE = "인수심사 결과가 반영되었습니다.";

}
