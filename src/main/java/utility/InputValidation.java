package utility;

import exception.InputException;
import exception.MyInadequateFormatException;

import java.util.Scanner;

import static main.utility.CustomerInfoFormatUtil.*;

public class InputValidation {
    private Scanner sc;

    public String validateNameFormat(String name) {
        if(name.isBlank())
            throw new InputException.InputNullDataException();
        if(!isName(name))
            throw new InputException.InputInvalidDataException();
        return name;
    }

    public String validateSsnFormat(String ssn) {
        if(ssn.isBlank())
            throw new InputException.InputNullDataException();
        if(!isSsn(ssn))
            throw new InputException.InputInvalidDataException();
        return ssn;
    }

    public String validatePhoneFormat(String phone) {
        if(phone.isBlank())
            throw new InputException.InputNullDataException();
        if(!isPhone(phone))
            throw new MyInadequateFormatException();
        return phone;
    }

    public String validateEmailFormat(String email) {
        if(email.isBlank())
            throw new InputException.InputNullDataException();
        if(!isEmail(email))
            throw new MyInadequateFormatException();
        return email;
    }

    public String validateCarNoFormat(String carNo) {
        if(carNo.isBlank())
            throw new InputException.InputNullDataException();
        if(!isEmail(carNo))
            throw new MyInadequateFormatException();
        return carNo;
    }

    public int validateIntFormat(String question) {
        while (true) {
            try {
                System.out.println(question);
                String temp = sc.nextLine();
                if (temp.isBlank()){
                    throw new InputException.InputNullDataException();
                }
                return Integer.parseInt(temp);
            } catch (InputException.InputNullDataException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("ERROR!! : 유효하지 않은 값을 입력하였습니다.\n");
            }
        }
    }

    public boolean validateBooleanFormat(String question) {
        while (true) {
            try {
                System.out.println(question);
                String input = sc.nextLine();
                boolean temp = false;
                switch (input) {
                    case "1":
                        temp = true;
                        break;
                    case "2":
                    case "":
                        break;
                    default:
                        throw new InputException.InputInvalidDataException();
                }
                return temp;
            } catch (InputException.InputInvalidDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String validateStringFormat(String question){
        while (true){
            try{
                System.out.println(question);
                String temp = sc.nextLine();
                if(temp.isBlank())
                    throw new InputException.InputNullDataException();
                return temp;
            } catch (InputException.InputNullDataException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
