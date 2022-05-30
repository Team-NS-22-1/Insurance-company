package utility;

import exception.InputException;

import java.util.Scanner;

import static utility.CustomerInfoFormatUtil.*;

public class InputValidation {
    private Scanner sc;


    public InputValidation(){
        sc = new Scanner(System.in);
    }

    public String validateStringFormat(String question){
        while (true){
            try{
                System.out.println(question);
                String temp = sc.nextLine();
                if(temp.isBlank())
                    throw new InputException.InputNullDataException();
                return temp;
            } catch (InputException e){
                System.out.println(e.getMessage());
            }
        }
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
            } catch (InputException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("ERROR!! : 유효하지 않은 값을 입력하였습니다.\n");
            }
        }
    }

    public Long validateLongFormat(String question) {
        while (true) {
            try {
                System.out.println(question);
                String temp = sc.nextLine();
                if (temp.isBlank()){
                    throw new InputException.InputNullDataException();
                }
                return Long.parseLong(temp);
            } catch (InputException e) {
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
                boolean temp;
                switch (input) {
                    case "1":
                        temp = true;
                        break;
                    case "2":
                        temp = false;
                        break;
                    case "":
                        throw new InputException.InputNullDataException();
                    default:
                        throw new InputException.InputInvalidDataException();
                }
                return temp;
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String validateDistinctFormat(String question, int choice) {
        while(true) {
            try {
                System.out.println(question);
                String temp = sc.nextLine();
                if(temp.isBlank())
                    throw new InputException.InputNullDataException();
                switch (choice) {
                    case 1:
                        validateNameFormat(temp);
                        break;
                    case 2:
                        validateSsnFormat(temp);
                        break;
                    case 3:
                        validatePhoneFormat(temp);
                        break;
                    case 4:
                        validateEmailFormat(temp);
                        break;
                    case 5:
                        validateCarNoFormat(temp);
                        break;
                }
                return temp;
            } catch (InputException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void validateNameFormat(String name) {
        if(!isName(name))
            throw new InputException.InputInvalidDataException();
    }

    public void validateSsnFormat(String ssn) {
        if(!isSsn(ssn))
            throw new InputException.InputInvalidDataException();
    }

    public void validatePhoneFormat(String phone) {
        if(!isPhone(phone))
            throw new InputException.InputInvalidDataException();
    }

    public void validateEmailFormat(String email) {
        if(!isEmail(email))
            throw new InputException.InputInvalidDataException();
    }

    public void validateCarNoFormat(String carNo) {
        if(!isCarNo(carNo))
            throw new InputException.InputInvalidDataException();
    }
}
