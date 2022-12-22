package com.cscie97.ledger;

public class LedgerException extends Exception {
	
	private String reason;
	private String action;
	
	public LedgerException(String action, String reason) {
		super("Ledger Exception while executing " + action + " because of "+ reason); 
		this.action = action;
		this.reason = reason;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	

}
