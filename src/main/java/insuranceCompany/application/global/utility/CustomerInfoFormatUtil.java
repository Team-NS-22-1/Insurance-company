package insuranceCompany.application.global.utility;

import java.util.regex.Pattern;

public class CustomerInfoFormatUtil {

    public static boolean isName(String name){
        return Pattern.matches("^[가-힣]+$", name);
    }

    public static boolean isSsn(String ssn){
        return Pattern.matches("^[0-9]{2}[0-1][0-9][0-3][0-9]-[1-4][0-9]{6}$", ssn);
    }

    public static boolean isEmail(String email){
        return Pattern.matches("^[a-zA-Z0-9]+@[a-z]+.[a-z]+$", email);
    }

    public static boolean isPhone(String phone){
        return Pattern.matches("^0[0-9]{2}-[0-9]{3,4}-[0-9]{4}$", phone);
    }

    public static boolean isCarNo(String carNo){
        return Pattern.matches("^[0-9]{2,3}[가-힣][\\s]{0,1}[0-9]{4}$", carNo);
    }
}
