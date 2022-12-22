package com.cscie97.store.authentication;

public class InvalidAccessTokenException extends Exception {

	private String reason;
	private String methodName;
	
	public InvalidAccessTokenException(String reason, String methodName) {
		super("InvalidAccessTokenException Exception of "+ reason + " while Executing method of:  " + methodName ); 
		this.reason = reason;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
