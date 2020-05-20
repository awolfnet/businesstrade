package net.awolf.businesstrade.method;

/**
 * Created by Work on 2017/8/5.
 */

public interface IMethodCallback {
    public void onSuccesse(Object o);
    public void onServerFailure(String response);
    public void onUnfulfil(String unfulfilMessage);
    public void onException(String errorMessage);
    public void onFinish();
}
