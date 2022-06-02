package insuranceCompany.application.global.utility;

import insuranceCompany.application.global.exception.InputException;
import insuranceCompany.application.global.exception.MyCloseSequence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MyBufferedReader extends BufferedReader {
    public MyBufferedReader(Reader in) {
        super(in);
    }

    private void checkBlankOrExit(String value) {
        if(value.equals("") || value == null || value.isBlank())
            throw new InputException.InputNullDataException();
        if(value.equalsIgnoreCase("EXIT"))
            throw new MyCloseSequence();
    }

    public Object verifyRead(String query, Object returnType) throws IOException {
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
                        throw new InputException.InputInvalidDataException();
                }
                catch (NumberFormatException e) {
                    throw new InputException.InputInvalidDataException();
                }
            }
            catch (InputException e){
                System.out.println(e.getMessage());
            }
        }
    }

    // verifyMenu()는 '0'을 입력 가능하여 return Menu가 가능토록 구성
    public int verifyMenu(String query, int categorySize) throws IOException {
        while(true){
            System.out.print(query);
            try {
                String value = this.readLine();
                checkBlankOrExit(value);
                try {
                    int selectedMenu;
                    selectedMenu = Integer.parseInt(value);
                    if (selectedMenu > categorySize || selectedMenu < 0)
                        throw new InputException.InputInvalidMenuException();
                    return selectedMenu;
                } catch (NumberFormatException e) {
                    throw new InputException.InputInvalidMenuException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                throw new InputException(e);
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
                        throw new InputException.InputInvalidDataException();
                    return selectedCategory;
                } catch (NumberFormatException e) {
                    throw new InputException.InputInvalidDataException();
                }
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                throw new InputException(e);
            }
        }
    }
}
