package insuranceCompany.application.global.constant;

public class DevelopViewLogicConstants {
    
    public static final String EXIT_SYSTEM = " [exit: 시스템 종료]";
    public static final String PRINT_PREMIUM = "보험료: %d(원)\n";
    public static final String MENU_IS_CANCEL = "<< 정말 취소하시겠습니까? >>\n1. 예 2. 아니오\n";

    // Menu
    public static final String MENU_TITLE_DEV_VIEW_LOGIC = "<< 개발팀 메뉴 >>";
    public static final String[] MENU_ELEMENTS_DEV_VIEW_LOGIC = {"보험 개발", "판매인가 등록"};
    
    // Method showInsuranceEmployee()
    public static final String EMPLOYEE_INSURANCE_LIST = "<< %s 보험 개발 리스트 >>\n";
    public static final String NONE_INSURANCE_LIST = "--------------NONE---------------";
    public static final String INSURANCE_LIST_LINE = "---------------------------------";
    
    // Method showInsuranceByEmployeeAndSelect()
    public static final String ERROR_NONE_INSURANCE_LIST = "ERROR:: 개발한 보험이 없습니다!";
    public static final String MENU_SELECT_INSURANCE_FOR_REGISTER_FILE = "<< 파일을 추가할 보험을 선택하세요. >>";
    public static final String QUERY_INSURANCE_ID = "보험 ID: ";
    public static final String EXCEPTION_NO_RESULT_LIST = "ERROR:: 리스트에 있는 아이디를 입력해주세요.";
    
    // Method menuInsuranceType()
    public static final String MENU_INSURANCE_TYPE_TITLE = "<< 보험 종류 선택 >>";
    public static final String[] MENU_INSURANCE_TYPE_ELEMENTS = {"건강보험", "자동차보험", "화재보험"};
    
    // Method formInputBasicInfo() 
    public static final String TITLE_INPUT_BASIC_INFO = "<< 보험 기본 정보 >>";
    public static final String QUERY_INSURANCE_NAME = "보험 이름: ";
    public static final String QUERY_INSURANCE_DESCRIPTION = "보험 설명: ";
    public static final String QUERY_INSURANCE_CONTRACT_PERIOD = "만기 나이(세): ";
    public static final String QUERY_INSURANCE_PAYMENT_PERIOD = "납입 기간(년): ";
    
    // Method formDtoHealth()
    public static final String TITLE_INPUT_HEALTH_INFO = "<< 건강 보험 정보 >>";
    public static final String QUERY_TARGET_AGE = "대상 나이: ";
    public static final String QUERY_TARGET_SEX = "대상 성별: \n1. 남자\n2. 여자\n";
    public static final String QUERY_RISK_CRITERION = "위험부담 기준(개): ";
    public static final String ERROR_RISK_CRITERION_COUNT = "위험부담 기준은 6개 이하여야 합니다.";
    public static final String MENU_ADD_HEALTH_INFO = "<< 건강 보험 조건을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n";
    
    // Method formDtoCar()
    public static final String TITLE_INPUT_CAR_INFO = "<< 자동차 보험 정보 >>";
    public static final String QUERY_VALUE_CRITERION = "차량가액 기준(원): ";
    public static final String MENU_ADD_CAR_INFO = "<< 자동차 보험 조건을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n";

    // Method formDtoFire()
    public static final String TITLE_INPUT_FIRE_INFO = "<< 화재 보험 정보 >>";
    public static final String QUERY_TARGET_BUILDING_TYPE = "대상 건물 타입: \n1. 상업용\n2. 산업용\n3. 기관용\n4. 거주용\n";
    public static final String QUERY_COLLATERAL_AMOUNT = "담보 금액: ";
    public static final String MENU_ADD_FIRE_INFO = "<< 화재 보험 조건을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n";

    // Method formInputGuaranteeInfo()
    public static final String TITLE_INPUT_GUARANTEE = "<< 보장 상세 내용 >>";
    public static final String QUERY_GUARANTEE_NAME = "보장 이름: ";
    public static final String QUERY_GUARANTEE_DESCRIPTION = "보장 설명: ";
    public static final String QUERY_GUARANTEE_AMOUNT = "보장 금액: ";
    public static final String MENU_ADD_GUARANTEE = "<< 보장을 더 추가하시겠습니까? >>\n1. 예 2. 아니오\n";

    // Method isCalcPremium()
    public static final String MENU_IS_CALCULATE_PREMIUM = "<< 보험료를 산출하시겠습니까? >>\n1.예 2.아니오\n";

    // Method calcStandardPremium()
    public static final String TITLE_CALC_ST_PREMIUM = "<< 기준 보험료 산출 >>";
    public static final String QUERY_DAMAGE_AMOUNT = "발생손해액(단위:만원): ";
    public static final String QUERY_COUNT_CONTRACT = "계약건수(건): ";
    public static final String QUERY_BIZ_EXPENSE = "사업비(단위:만원): ";
    public static final String QUERY_PROFIT_MARGIN = "이익률(1~99%): ";
    public static final String PRINT_ST_PREMIUM = "기준 보험료: %d(원)\n";
    public static final String MENU_IS_DECISION_ST_PREMIUM = "<< 산출된 기준 보험료로 확정하시겠습니까? >>\n1.예 2. 아니오\n";

    // Method formRegisterInsurance()
    public static final String MENU_IS_REGISTER_INSURANCE = "<< 보험을 저장하시겠습니까? >>\n1. 예 2. 아니오\n";
    public static final String PRINT_SUCCESS_REGISTER_INSURANCE = "SUCCESS:: 보험이 저장되었습니다!";

    // Method menuSalesAuthFile()
    public static final String TITLE_REGISTER_SALES_AUTH_FILE = "<< 해당 보험의 추가할 파일을 선택하세요. >>";
    public static final String MENU_SALES_AUTH_FILE_TYPE = "1. 보험상품신고서\n"
                                                        + "2. 선임계리사 검증기초서류\n"
                                                        + "3. 보험요율산출기관 검증확인서\n"
                                                        + "4. 금융감독원 판매인가서\n"
                                                        + "0. 이전 메뉴로 돌아가기\n";
    public static final String PRINT_SUCCESS_UPLOAD_SAF = "SUCCESS:: 파일이 업로드되었습니다!";
    public static final String ERROR_EXIST_FILE_IS_CHANGE_FILE = "<< 이미 파일이 존재합니다! 변경하시겠습니까? >>\n1. 예 2. 아니오\n";
    public static final String REGISTER_ALL_FILE_MODIFY_STATE = "<< 모든 인가파일이 등록되었습니다! 판매상태를 변경해주세요. >>\n1. 허가 2. 불허\n";

}
