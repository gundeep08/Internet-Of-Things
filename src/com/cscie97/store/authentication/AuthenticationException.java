package com.cscie97.store.authentication;

public class AuthenticationException  extends Exception{
	
	private String reason;
	private String methodName;
	
	public AuthenticationException(String reason, String methodName) {
		super("Authentication Exception of "+ reason + " while Executing method of:  " + methodName ); 
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
