package main.utility;

import main.exception.MyIOException;

import java.io.IOException;
import java.io.Reader;

/**
 * packageName :  main.utility
 * fileName : CustomMyBufferedReader
 * author :  규현
 * date : 2022-05-18
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-18                규현             최초 생성
 */
public class CustomMyBufferedReader extends MyBufferedReader{
    public CustomMyBufferedReader(Reader in) {
        super(in);
    }

    @Override
    public Object verifyRead(String query, Object returnType){
        Object rtVal = null;
        try {
            rtVal = super.verifyRead(query, returnType);
        } catch (IOException e) {
            throw new MyIOException(e);
        }
        return rtVal;
    }

    @Override
    public int verifyMenu(String query, int categorySize){
        int rtVal = 0;
        try {
            rtVal =   super.verifyMenu(query, categorySize);
        } catch (IOException e) {
            throw new MyIOException(e);
        }
        return rtVal;
    }
}
