package com.cscie97.ledger;

public class Transaction {
	
	private String transactionId;
	private int amount;
	private int fee;
	private String note;
	private String payerAddress;
	private String recieverAddress;

	
	public Transaction(String transactionId, int amount, int fee, String note, String payerId,String receiverId) {
		this.transactionId=transactionId;
		this.amount =amount;
		this.fee=fee;
		this.note=note;
		this.payerAddress=payerId;
		this.recieverAddress= receiverId;
		
	}
	
	public String getPayerAddress() {
		return payerAddress;
	}

	public void setPayerAddress(String payerAddress) {
		this.payerAddress = payerAddress;
	}

	public String getRecieverAddress() {
		return recieverAddress;
	}

	public void setRecieverAddress(String recieverAddress) {
		this.recieverAddress = recieverAddress;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public String toString() {
		   String response= "Transaction with transactionId of "  + this.getTransactionId() + "  amount of " + this.amount + " fee of " +this.fee + " with note of "+ this.note;
		   return response;
	   }
	
	

}
