package net.awolf.businesstrade.model.response;

/**
 * Created by Work on 2017/8/5.
 */

public class ResponseModel<T> {

    public boolean success;
    public int code;
    public T message;

    public boolean getSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public T getMessage() {
        return message;
    }
    public void setMessage(T message) {
        this.message = message;
    }

}
