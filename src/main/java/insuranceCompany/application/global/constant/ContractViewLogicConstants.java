package insuranceCompany.application.global.constant;

public class ContractViewLogicConstants {
    // Sales Menu
    public static final String SALES_MENU = "<<영업팀 메뉴>>";
    public static final String SALES_MENU_ELEMENTS = "보험상품설계";

    // Method selectInsurance()
    public static final String CONTRACT_INSURANCE_LIST = "<< 보험상품목록 >>";
    public static final String CONTRACT_INSURANCE_ID = "상품번호";
    public static final String CONTRACT_INSURANCE_NAME = "보험이름";
    public static final String CONTRACT_INSURANCE_TYPE = "보험종류";
    public static final String CONTRACT_INPUT_INSURANCE_ID = "상품번호: ";
    public static final String CONTRACT_INSURANCE_DESCRIPTION = "<< 상품안내 >>\n";
    public static final String CONTRACT_INSURANCE_CONTRACT_PERIOD = "\n계약기간: ";
    public static final String CONTRACT_INSURANCE_PAYMENT_PERIOD = "납입기간: ";
    public static final String CONTRACT_INSURANCE_GUARANTEE = "\n<< 보장내역 >>";
    public static final String CONTRACT_INSURANCE_GUARANTEE_DESCRIPTION = "보장내용";
    public static final String CONTRACT_INSURANCE_GUARANTEE_AMOUNT = "보장금액";
    public static final String CONTRACT_YEAR_PARAMETER = "년 ";

    public static final String CUSTOMER_SELECT_INSURANCE_ID = "가입할 보험상품의 번호를 입력하세요. \t(0: 뒤로가기)";
    public static final String SALES_SELECT_INSURANCE_ID = "설계할 보험상품의 번호를 입력하세요. \t(0 : 뒤로가기)";

    // Method decideSigning()
    public static final String SALES_INSURANCE_DATAIL = "<< 계약조건 >>\n";
    public static final String SALES_TARGET_AGE = "나이";
    public static final String SALES_TARGET_SEX = "성별";
    public static final String SALES_RISK_CRITERTION = "위험도 기준";
    public static final String SALES_BUIILDING_TYPE = "건물종류";
    public static final String SALES_COLLATERAL_AMOUNT_CRITERION = "담보금액기준";
    public static final String SALES_VALUE_CRITERION = "차량가액기준";

    public static final String SALES_PROGRESS_CONTRACT = "보험계약을 진행하시겠습니까?\n1. 계약\n2. 취소\n";
    public static final String CUSTOMER_DICIDE_SIGNING = "해당 보험상품을 가입하시겠습니까?\n1. 가입\n2. 취소\n";

    public static final String SALES__PREMIUM= "보험료";
    public static final String SALES_CANCEL= "계약이 취소되었습니다.";

    // Method planHealthInsurance()
    public static final String SALES_TARGET_SEX_QUERY = "성별 \n1. 남  2. 여\n";
    public static final String SALES_IS_DRINKING_QUERY = "음주 여부를 입력해주세요. \n1. 예  2. 아니요\n";
    public static final String SALES_IS_SMOKING_QUERY = "흡연 여부를 입력해주세요. \n1. 예  2. 아니요\n";
    public static final String SALES_IS_DRIVING_QUERY = "운전 여부를 입력해주세요. \n1. 예  2. 아니요\n";
    public static final String SALES_IS_DANGER_ACTIVITY_QUERY = "위험 취미 활동 여부를 입력해주세요. \n1. 예  2. 아니요\n";
    public static final String SALES_IS_TAKING_DRUG_QUERY = "약물 복용 여부를 입력해주세요. \n1. 예  2. 아니요\n";
    public static final String SALES_IS_HAVING_DISEASE_QUERY = "질병 이력 여부를 입력해주세요. \n1. 예  2. 아니요\n";

    // Method planFireInsurance()
    public static final String SALES_BUILDING_TYPE_QUERY = "건물종류를 선택해주세요.\n1. 상업용\n2. 산업용\n3. 기관용\n4. 거주용\n";
    public static final String SALES_COLLATRAL_AMOUNT_QUERY = "담보금액: (단워: 원): ";

    // Method planCarInsurance()
    public static final String SALES_VALUE_QUERY = "차량가액 (단위: 원): ";

    // Method plan...
    public static final String SALES_TARGET_AGE_QUERY = "고객님의 나이: ";
    public static String premiumInquiry(int premium) {
        return "조회된 귀하의 보험료는 " + premium + "원 입니다.";
    }

    // Method inputCustomerInfo()
    public static final String SALES_IS_CONTRACTED_CUSTOMER = "등록된 회원입니까?\n1. 예\n2. 아니요\n";
    public static final String SALES_INPUT_CUSTOMER_ID = "고객 ID를 입력해주세요.\n고객 ID: ";
    public static final String SALES_INPUT_CUSTOMER_INFO = "<< 고객님의 개인정보를 입력해주세요. >>";
    public static final String SALES_CUSTOMER_NAME_QUERY = "이름: ";
    public static final String SALES_SSN_QUERY = "주민번호 (______-*******): ";
    public static final String SALES_PHONE_QUERY = "연락처 (0__-____-____): ";
    public static final String SALES_ADDRESS_QUERY = "주소: ";
    public static final String SALES_EMAIL_QUERY = "이메일 (_____@_____.___): ";
    public static final String SALES_JOB_QUERY = "직업: ";

    // Method inputHealthInfo()
    public static final String SALES_INPUT_HEALTH_INFO = "<< 고객님의 건강정보를 입력해주세요. >>";
    public static final String SALES_HEIGHT_QUERY = "키 (단위: cm): ";
    public static final String SALES_WEGHIT_QUERY = "몸무게 (단위: kg): ";
    public static final String SALES_DISEASE_DETAIL_QUERY = "질병에 대한 상세 내용를 입력해주세요.\n";

    // Method inputFireInfo()
    public static final String SALES_BUILDING_AREA_QUERY = "건물면적 (단위: m^2): ";
    public static final String SALES_IS_SELF_OWNED_QUERY = "자가 여부를 입력해주세요. \n1. 예  2. 아니요\n";
    public static final String SALES_IS_ACTUAL_RESIDENCE_QUERY = "실거주 여부를 입력해주세요. \n1. 예  2. 아니요\n";

    // Method inputCarInfo()
    public static final String SALES_CAR_NO_QUERY = "차량번호: ";
    public static final String SALES_CAR_TYPE_QUERY = "차종을 선택해주세요.\n1. 경형\n2. 소형\n3. 준중형\n4. 중형\n5. 준대형\n6. 대형\n7. 스포츠카\n";
    public static final String SALES_MADEL_NAME_QUERY = "모델 이름: ";
    public static final String SALES_MODEL_YEAR_QUERY = "차량 연식 (단위: 년): ";

    // Method concludeContract()
    public static final String SALES_CONCLUDE_CONTRACT = "보험계약을 체결하시겠습니까?\n1. 계약체결\n2. 취소\n";
    public static final String SALES_CONCLUDE = "계약을 체결하였습니다.";

    public static final String CUSTOMER_SGIN_CONTRACT = "보험가입을 신청하시겠습니까?\n1. 가입\n2. 취소\n";
    public static final String CUSTOMER_SIGN = "가입이 완료되었습니다.";
    public static final String CUSTOMER_CANCEL = "가입이 취소되었습니다.";

    public static final String CONTRACT_USER_ID_QUERY = "아이디: ";
    public static final String CONTRACT_USER_PASSWORD_QUERY = "비밀번호: ";

    // Table format
    public static final String CONTRACT_INSURANCES_CATEGORY_FORMAT = "%-4s\t|\t%-10s\t|\t%-5s\n";
    public static final String CONTRACT_INSURANCES_VALUE_FORMAT = "%-4s\t|\t%-10s\t|\t%-5s\n";
    public static final String CONTRACT_GUARANTEES_CATEGORY_FORMAT = "%-12s\t|\t%-20s\t\t|\t%-11s\n";
    public static final String CONTRACT_GUARANTEES_VALUE_FORMAT = "%-10s\t|\t%-20s\t|\t%-5d원\n";
    public static final String CONTRACT_HEALTH_DETAIL_CATEGORY_FORMAT = "%-10s\t|\t%-3s\t|\t%-6s\t|\t%-8s\n";
    public static final String CONTRACT_HEALTH_DETAIL_VALUE_FORMAT = "%-10s\t|\t%-3s\t|\t%-6s\t\t|\t%-5d원\n";
    public static final String CONTRACT_FIRE_DETAIL_CATEGORY_FORMAT = "%-2s\t|\t%-10s\t|\t%-7s\n";
    public static final String CONTRACT_FIRE_DETAIL_VALUE_FORMAT = "%-3s\t|\t%-10s\t|\t%-7d원\n";
    public static final String CONTRACT_CAR_DETAIL_CATEGORY_FORMAT = "%-8s\t|\t%-20s\t|\t%-5s\n";
    public static final String CONTRACT_CAR_DETAIL_VALUE_FORMAT = "%-8s\t|\t%-20s\t|\t%-5d원\n";
    public static final String CONTRACT_SHORT_DIVISION = "__________________________________________";
    public static final String CONTRACT_LONG_DIVISION = "___________________________________________________________________";
}
