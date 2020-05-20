package net.awolf.businesstrade.model.response;

/**
 * Created by Work on 2017/8/11.
 */

public class ErrorModel {
    public int ErrorCode;
    public String ErrorMessage;

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
