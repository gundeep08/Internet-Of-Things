package com.cscie97.ledger;

public class Account {
	
	private String address;
	private int balance =0;
	
	public Account(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
   public String toString() {
	   String response= "Account Created with Address of "  + this.getAddress()+ " and with the initial Balance of "+ this.getBalance();
	   return response;
   }
}
