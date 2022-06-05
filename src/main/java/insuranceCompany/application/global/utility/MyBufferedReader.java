package insuranceCompany.application.global.utility;

import insuranceCompany.application.global.exception.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static insuranceCompany.application.global.utility.CustomerInfoFormatUtil.*;

public class MyBufferedReader extends BufferedReader {
    public MyBufferedReader(Reader in) {
        super(in);
    }

    private void checkBlankOrExit(String value) {
        if(value.equals("") || value == null || value.isBlank())
            throw new InputNullDataException();
        if(value.equalsIgnoreCase("EXIT"))
            throw new MyCloseSequence();
    }

    private void checkSpecificFormat(String value) {

    }

    public Object verifyRead(String query, Object returnType) {
        while(true){
            System.out.print(query);
            try {
                String value = this.readLine();
                checkBlankOrExit(value);
                try {
                    if(returnType instanceof String)
                        return value;
                    else if(returnType instanceof Integer){
                        int intValue = Integer.parseInt(value);
                        return intValue;
                    }
                    else if(returnType instanceof Long){
                        long longValue = Long.parseLong(value);
                        return longValue;
                    }
                    else if(returnType instanceof Double){
                        double doubleValue = Double.parseDouble(value);
                        return doubleValue;
                    }
                    else
                        throw new InputInvalidDataException();
                }
                catch (NumberFormatException e) {
                    throw new InputInvalidDataException();
                }
            }
            catch (InputException e){
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                throw new MyIOException();
            }
        }
    }

    public Object verifySpecificRead(String query, Object returnType, String verifyType) {
        while(true){
            System.out.print(query);
            try {
                String value = this.readLine();
                checkBlankOrExit(value);
                boolean check = false;
                switch (verifyType) {
                    case "name" -> check = isName(value);
                    case "ssn" -> check = isSsn(value);
                    case "phone" -> check = isPhone(value);
                    case "email" -> check = isEmail(value);
                    case "carNo" -> check = isCarNo(value);
                }
                if (check == false) {
                    throw new InputInvalidDataException();
                }
                try {
                    if(returnType instanceof String)
                        return value;
                    else if(returnType instanceof Integer){
                        int intValue = Integer.parseInt(value);
                        return intValue;
                    }
                    else if(returnType instanceof Long){
                        long longValue = Long.parseLong(value);
                        return longValue;
                    }
                    else if(returnType instanceof Double){
                        double doubleValue = Double.parseDouble(value);
                        return doubleValue;
                    }
                    else
                        throw new InputInvalidDataException();
                }
                catch (NumberFormatException e) {
                    throw new InputInvalidDataException();
                }
            }
            catch (InputException e){
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                throw new MyIOException();
            }
        }
    }

    // verifyMenu()는 '0'을 입력 가능하여 return Menu가 가능토록 구성
    public int verifyMenu(String query, int categorySize)  {
        while(true){
            System.out.print(query);
            try {
                String value = this.readLine();
                checkBlankOrExit(value);
                try {
                    int selectedMenu;
                    selectedMenu = Integer.parseInt(value);
                    if (selectedMenu > categorySize || selectedMenu < 0)
                        throw new InputInvalidMenuException();
                    return selectedMenu;
                } catch (NumberFormatException e) {
                    throw new InputInvalidMenuException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                throw new MyIOException();
            }
        }
    }

    // verifyCategory()는 정보 입력 중 범주형 질문에 대한 검증 로직으로 '0'이 입력 불가하도록 구성
    public int verifyCategory(String query, int categorySize) {
        while(true){
            System.out.print(query);
            try {
                String value = this.readLine();
                checkBlankOrExit(value);
                try {
                    int selectedCategory;
                    selectedCategory = Integer.parseInt(value);
                    if (selectedCategory > categorySize || selectedCategory < 1)
                        throw new InputInvalidDataException();
                    return selectedCategory;
                } catch (NumberFormatException e) {
                    throw new InputInvalidDataException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                throw new MyIOException();
            }
        }
    }
}
