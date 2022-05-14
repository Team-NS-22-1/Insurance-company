package main.domain.utility;

import main.exception.TestInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MyBufferedReader extends BufferedReader {
    public MyBufferedReader(Reader in) {
        super(in);
    }

    public Object verifyRead(Object returnType) throws IOException,
                                                    TestInputException.InputNullDataException,
                                                    TestInputException.InputInvalidDataException {
        String value = this.readLine();
        if(value.equals("") || value == null || value.isBlank())
            throw new TestInputException.InputNullDataException();

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
                throw new TestInputException.InputInvalidDataException();
        }
        catch (NumberFormatException e) {
            throw new TestInputException.InputInvalidDataException();
        }
    }

    public int verifyMenu(int categorySize) throws IOException, TestInputException.InvalidMenuException {
        String value = this.readLine();
        if(value.equals("") || value == null || value.isBlank())
            throw new TestInputException.InvalidMenuException();

        int selectedMenu;
        try {
            selectedMenu = Integer.parseInt(value);
        }
        catch (NumberFormatException e){
            throw new TestInputException.InvalidMenuException();
        }
        if(selectedMenu > categorySize || selectedMenu < 1)
            throw new TestInputException.InvalidMenuException();

        return selectedMenu;
    }

}
