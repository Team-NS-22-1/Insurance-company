package utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PremiumInfoFinder {

    public static int customerAgeFinder(String ssn) {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        String today = date.format(new Date());

        int thisYear = Integer.parseInt(today.substring(0, 4));
        int thisMonth = Integer.parseInt(today.substring(4, 6));
        int thisDay = Integer.parseInt(today.substring(6, 8));
        int sex = Integer.parseInt(ssn.substring(7, 8));

        int birthYear = Integer.parseInt(ssn.substring(0, 2));
        int birthMonth = Integer.parseInt(ssn.substring(2, 4));
        int birthDay = Integer.parseInt(ssn.substring(4, 6));

        if (sex == 1 || sex == 2) {
            birthYear += 1900;
        } else {
            birthYear += 2000;
        }
        int age = thisYear - birthYear;
        if (birthMonth > thisMonth)
            age--;
        else if (birthMonth == thisMonth) {
            if (birthDay > thisDay)
                age--;
        }
        return age;
    }

    public static boolean customerSexFinder(String ssn) {
        boolean sex;
        int num = Integer.parseInt(ssn.substring(7, 8));
        if(num == 1 || num == 3)
            sex = true;
        else
            sex = false;
        return sex;
    }
}
