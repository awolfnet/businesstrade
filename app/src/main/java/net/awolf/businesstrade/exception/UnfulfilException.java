package net.awolf.businesstrade.exception;

/**
 * Created by Work on 2017/8/19.
 */

public class UnfulfilException extends Exception {
    private String UnfulfilMessage;
    public UnfulfilException(String message)
    {
        super(message);
        this.UnfulfilMessage=message;
    }
}
