package net.awolf.businesstrade.exception;

public class ModelException extends Exception {
	private String ErrorMessage;
	public ModelException(String msg)
	{
		super(msg);
		this.ErrorMessage=msg;
	}
}
