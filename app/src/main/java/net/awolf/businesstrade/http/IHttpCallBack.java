package net.awolf.businesstrade.http;

public interface IHttpCallBack {
	public void onSuccesse(Object o);
	public void onException(String errorMessage);
	public void onFinish();
}
