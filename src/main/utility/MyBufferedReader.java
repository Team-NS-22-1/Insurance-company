package main.utility;

import main.exception.InputException;
import main.exception.MyCloseSequence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MyBufferedReader extends BufferedReader {
    public MyBufferedReader(Reader in) {
        super(in);
    }

    public Object verifyRead(Object returnType) throws IOException,
                                                    InputException.InputNullDataException,
                                                    InputException.InputInvalidDataException,
            MyCloseSequence {
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

    private void checkBlankOrExit(String value) {
        if(value.equals("") || value == null || value.isBlank())
            throw new InputException.InputNullDataException();
        if(value.equalsIgnoreCase("EXIT"))
            throw new MyCloseSequence();
    }

    public int verifyMenu(int categorySize) throws IOException,
                                                    InputException.InvalidMenuException, MyCloseSequence {
        String value = this.readLine();
        checkBlankOrExit(value);

        int selectedMenu;
        try {
            selectedMenu = Integer.parseInt(value);
            if(selectedMenu > categorySize || selectedMenu < 0)
                throw new InputException.InvalidMenuException();
        }
        catch (NumberFormatException e){
            throw new InputException.InvalidMenuException();
        }
        return selectedMenu;
    }

}
