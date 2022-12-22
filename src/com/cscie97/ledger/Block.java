package com.cscie97.ledger;

import java.util.List;
import java.util.Map;

public class Block {
	
	private String previousHash;
	private String hash;
	private List<Transaction> transactionList;
	private Map<String,Account> accountBalanceMap;
	private Block previousBlock;
	private int blockNumber;
	
	public Block(int blockNumber) {
		this.blockNumber=blockNumber;
	}
	
	public int getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
	}
	public String getPreviousHash() {
		return previousHash;
	}
	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public List<Transaction> getTransactionList() {
		return transactionList;
	}
	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}
	public Block getPreviousBlock() {
		return previousBlock;
	}
	public void setPreviousBlock(Block previousBlock) {
		this.previousBlock = previousBlock;
	}
	public Map<String, Account> getAccountBalanceMap() {
		return accountBalanceMap;
	}

	public void setAccountBalanceMap(Map<String, Account> accountBalanceMap) {
		this.accountBalanceMap = accountBalanceMap;
	}
	
	public String toString() {
		   String response= "Block with blockNumber of "  + this.getBlockNumber();
		   return response;
	   }
	
}
