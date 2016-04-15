package com.apl.traffic.cop.exception;

public class DataException extends Exception{
	
	/**
	 * when "1" will send email to customer
	 */
	private String notifyLevel = "0";
	
	
	public DataException() {
		super();
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataException(String message) {
		super(message);
	}
	
	public DataException(String message,String level)
	{
		super(message);
		this.notifyLevel = level;
	}

	public DataException(Throwable cause) {
		super(cause);
	}
	
	public String getNotifyLevel() {
		return notifyLevel;
	}
	
}
