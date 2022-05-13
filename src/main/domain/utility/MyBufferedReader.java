package main.domain.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MyBufferedReader extends BufferedReader {
    public MyBufferedReader(Reader in, int sz) {
        super(in, sz);
    }
    public MyBufferedReader(Reader in) {
        super(in);
    }

    public Object verifyRead(Object returnType) throws IOException {
        String value = this.readLine();
        if(value.equals("") || value == null)
            throw new InputNullDataException();

        if(returnType instanceof String) {
            return value;
        }
        else if(returnType instanceof Integer){
            int intValue;
            try {
                intValue = Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                throw new InputInvalidDataException();
            }
            return intValue;
        }
        else if(returnType instanceof Long){
            long longValue;
            try {
                longValue = Long.parseLong(value);
            }
            catch (NumberFormatException e) {
                throw new InputInvalidDataException();
            }
            return longValue;
        }
        else
            throw new InputInvalidDataException();
    }

    public int verifyMenu(int categorySize) throws IOException {
        String value = this.readLine();
        int selectedMenu;
        try {
            selectedMenu = Integer.parseInt(value);
        }
        catch (NumberFormatException e){
            throw new InvalidMenuException();
        }
        if(selectedMenu > categorySize || selectedMenu < 1)
            throw new InvalidMenuException();

        return selectedMenu;
    }

}

class InputNullDataException extends RuntimeException {
    public InputNullDataException() {
        super("ERROR!! : 입력창에 값을 입력해주세요.");
    }
}

class InputInvalidDataException extends RuntimeException {
    public InputInvalidDataException() {
        super("ERROR!! : 유효하지 않은 값을 입력하였습니다.");
    }
}

class InvalidMenuException extends RuntimeException {
    public InvalidMenuException() {
        super("ERROR!! : 올바른 메뉴를 입력해주세요.");
    }
}