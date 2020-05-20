package net.awolf.businesstrade.exception;

/**
 * Created by Work on 2017/8/5.
 */

public class ServerException extends Exception{
    private int ErrorCode;
    private String ErrorMessage;
    public ServerException(int code, String msg)
    {
        super(msg);
        this.ErrorCode=code;
        this.ErrorMessage=msg;

    }
}