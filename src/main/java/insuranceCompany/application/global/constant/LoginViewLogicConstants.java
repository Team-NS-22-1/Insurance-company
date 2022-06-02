package insuranceCompany.application.global.constant;

public class LoginViewLogicConstants {

    // Menu
    public static final String MENU_TITLE_LOGIN_VIEW_LOGIC = "<< INSURANCE-COMPANY >>";
    public static final String[] MENU_ELEMENTS_LOGIN_VIEW_LOGIC = {"고객", "직원"};

    // Method menuCustomerLogin()
    public static final String MENU_TITLE_LOGIN_CUSTOMER = "<< 고객 >>";
    public static final String[] MENU_ELEMENTS_LOGIN_CUSTOMER = {"회원", "비회원"};
    public static final String MSG_WELCOME_CUSTOMER = "어서오세요! %s 고객님.\n\n";

    // Method menuEmployeeLogin()
    public static final String MSG_WELCOME_EMPLOYEE = "LOGIN:: 부서[%s] 직책[%s] %s\n\n";

    public static final String SUCCESS_LOGOUT = "SUCCESS:: 로그아웃되었습니다!\n";

}
