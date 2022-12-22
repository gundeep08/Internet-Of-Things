package com.cscie97.ledger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class Ledger {

	private String name;
	private String description;
	private String seed;
	private Block genesisBlock;
	private Map<Integer,Block>blockMap;
	private static final Logger LOGGER = Logger.getLogger(Ledger.class.getClass().getName());
	
	public Ledger(String name, String description, String seed) {
		LOGGER.info("Initialize Ledger and create Genesis block");
		this.name =name;
		this.description=description;
		this.seed = seed;
		genesisBlock = new Block(1);
		String masterAddress= "master";
		Account masterAccount = new Account(masterAddress);
		masterAccount.setBalance(Integer.MAX_VALUE);
		Map<String, Account> masterAccountMap= new HashMap<>();
		masterAccountMap.put(masterAddress, masterAccount);
		genesisBlock.setAccountBalanceMap(masterAccountMap);
		Map<Integer,Block> newBlockMap = new HashMap<>();
		newBlockMap.put(1, genesisBlock);
		this.setBlockMap(newBlockMap);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public Block getGenesisBlock() {
		return genesisBlock;
	}
	public void setGenesisBlock(Block genesisBlock) {
		this.genesisBlock = genesisBlock;
	}
	public Map<Integer, Block> getBlockMap() {
		return blockMap;
	}
	public void setBlockMap(Map<Integer, Block> blockMap) {
		this.blockMap = blockMap;
	}
	
	public Account createAccount(String address) throws LedgerException{
		LOGGER.info("Create New Account and update AccountBalanceMap of current Block");
		Account account =null;
		if(!this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap().containsKey(address)) {
			account = new Account(address);
			account.setBalance(0);
			Map<String, Account> accountMap= this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap();
			accountMap.put(address, account);
			this.getBlockMap().get(this.getBlockMap().size()).setAccountBalanceMap(accountMap);
		}else {
			throw new LedgerException("Create Account", "Duplicate Address, Account already exists.");
		}
		return account;
	}
	
	public String processTransaction(Transaction transaction) throws LedgerException{
		LOGGER.info("ProcessTransaction and update current Block and if required create a new Block");
		String transactionId=null;
		boolean duplicateTransaction=false;
		outer: for(Map.Entry<Integer, Block> entry: this.getBlockMap().entrySet()) {
			List<Transaction> existingTransactions = entry.getValue().getTransactionList();
			if(existingTransactions!=null && existingTransactions.size()>0) {
				for(Transaction eachTransaction : existingTransactions) {
					if(eachTransaction.getTransactionId().equalsIgnoreCase(transaction.getTransactionId())) {
						duplicateTransaction= true;
						break outer;
					}
				}
			}
		}
		if(!duplicateTransaction) {
			int payerRequiredAmount= transaction.getAmount()+ transaction.getFee();
			if(transaction.getPayerAddress()!=null && transaction.getRecieverAddress()!=null &&
					this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap().containsKey(transaction.getPayerAddress()) && 
					this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap().containsKey(transaction.getRecieverAddress()) && 
					payerRequiredAmount<=this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap().get(transaction.getPayerAddress()).getBalance()) {
				
				transactionId=UUID.randomUUID().toString();
				Account payerAccount= this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap().get(transaction.getPayerAddress());
				Account recieverAccount= this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap().get(transaction.getRecieverAddress());				
				List<Transaction>transactions= this.getBlockMap().get(this.getBlockMap().size()).getTransactionList();
				if(transactions != null && transactions.size()<9) {
					LOGGER.info("ProcessTransaction and update current Block");
					transactions.add(transaction);
					this.getBlockMap().get(this.getBlockMap().size()).setTransactionList(transactions);
					Map<String, Account> updatedAccountBalanceMap = this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap();
					int balance = updatedAccountBalanceMap.get("master").getBalance();
					int masterAccountUpdatedAmount = balance + transaction.getFee();
					updatedAccountBalanceMap.get("master").setBalance(masterAccountUpdatedAmount);
					updatedAccountBalanceMap.get(recieverAccount.getAddress()).setBalance(recieverAccount.getBalance() + transaction.getAmount());
					updatedAccountBalanceMap.get(payerAccount.getAddress()).setBalance(payerAccount.getBalance() - transaction.getAmount() - transaction.getFee());
					this.getBlockMap().get(this.getBlockMap().size()).setAccountBalanceMap(updatedAccountBalanceMap);
				}else if(transactions != null && transactions.size()==9) {
					LOGGER.info("ProcessTransaction and update and complete the current Block and recoumpute the hash and also create new Block");
					transactions.add(transaction);
					this.getBlockMap().get(this.getBlockMap().size()).setTransactionList(transactions);
					Map<String, Account> updatedAccountBalanceMap = this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap();
					int balance = updatedAccountBalanceMap.get("master").getBalance();
					int masterAccountUpdatedAmount = balance + transaction.getFee();
					updatedAccountBalanceMap.get("master").setBalance(masterAccountUpdatedAmount);
					updatedAccountBalanceMap.get(recieverAccount.getAddress()).setBalance(recieverAccount.getBalance() + transaction.getAmount());
					updatedAccountBalanceMap.get(payerAccount.getAddress()).setBalance(payerAccount.getBalance() - transaction.getAmount() - transaction.getFee());
					this.getBlockMap().get(this.getBlockMap().size()).setAccountBalanceMap(updatedAccountBalanceMap);
					this.getBlockMap().get(this.getBlockMap().size()).setHash(calculateHash(seed, this.getBlockMap().get(this.getBlockMap().size())));
					Block newBlock = new Block(blockMap.size()+1);
					newBlock.setHash(calculateHash(seed, newBlock));
					newBlock.setPreviousBlock(this.getBlockMap().get(this.getBlockMap().size()));
					newBlock.setPreviousHash(this.getBlockMap().get(this.getBlockMap().size()).getHash());
					Map<String, Account> existingAccountBalanceMap = this.getBlockMap().get(this.getBlockMap().size()).getAccountBalanceMap();
					HashMap<String, Account> newAccountBalanceMap = new HashMap<>();
					for(Map.Entry<String, Account> entry: existingAccountBalanceMap.entrySet()) {
						Account newAccountInstance = new Account(entry.getValue().getAddress());
						newAccountInstance.setBalance(entry.getValue().getBalance());
						newAccountBalanceMap.put(entry.getKey(), newAccountInstance);
					}
					newBlock.setAccountBalanceMap(newAccountBalanceMap);
					List<Transaction> newTransactionList= new ArrayList<>();
					newBlock.setTransactionList(newTransactionList);
					blockMap.put(blockMap.size()+1, newBlock);
				}else {
					LOGGER.info("ProcessTransaction and create a new Block and add transaction to it");
					Block currentBlock =this.getBlockMap().get(this.getBlockMap().size());
					List<Transaction> newTransactionList= new ArrayList<>();
					newTransactionList.add(transaction);
					currentBlock.setTransactionList(newTransactionList); 
					Map<String, Account> updatedAccountBalanceMap = currentBlock.getAccountBalanceMap();
					int balance = updatedAccountBalanceMap.get("master").getBalance();
					int masterAccountUpdatedAmount = balance + transaction.getFee();
					updatedAccountBalanceMap.get("master").setBalance(masterAccountUpdatedAmount);
					updatedAccountBalanceMap.get(recieverAccount.getAddress()).setBalance(recieverAccount.getBalance() + transaction.getAmount());
					updatedAccountBalanceMap.get(payerAccount.getAddress()).setBalance(payerAccount.getBalance() - transaction.getAmount() - transaction.getFee());
					currentBlock.setAccountBalanceMap(updatedAccountBalanceMap);
				}
			}else{
				throw new LedgerException("Process Transaction", "Insufficient Balance.");
			}
		}else{
			throw new LedgerException("Process Transaction", "Duplicate Transaction, TransactionId already exists.");
		}
		
		return transactionId;	
	}
	
	public int getAccountBalance(String address) throws LedgerException {
		LOGGER.info("Get Account Balance of the given address from the last completed Block's AccountBalanceMap");
		int balance=0;
		boolean accountExists= false;
		Block lastCompletedBlock = null;
		Block latestBlock= blockMap.get(blockMap.size());
		if(latestBlock.getTransactionList()!=null && latestBlock.getTransactionList().size()==10) {
			lastCompletedBlock = latestBlock;
		}else {
			lastCompletedBlock = latestBlock.getPreviousBlock();
		}
		if(lastCompletedBlock !=null) {
			for(Map.Entry<String, Account> entry: lastCompletedBlock.getAccountBalanceMap().entrySet()) {
				if(entry.getKey().equalsIgnoreCase(address)) {
					balance= entry.getValue().getBalance();
					accountExists= true;
					break;
				}
			}
		}
		if(!accountExists) {
			throw new LedgerException("Get Account Balance", "Account with the given address of " + address+ " is not committed Yet.");
		}
		return balance;
	}
	
	public Map<String,Integer> getAccountBalances() throws LedgerException {
		LOGGER.info("Get Account Balance Map from the last completed Block's AccountBalanceMap");
		Map<String,Integer>accountBalanceMap =new HashMap<>();
		Block lastCompletedBlock = null;
		Block latestBlock= blockMap.get(blockMap.size());
		if(latestBlock.getTransactionList()!=null && latestBlock.getTransactionList().size()==10) {
			lastCompletedBlock = latestBlock;
		}else {
			lastCompletedBlock = latestBlock.getPreviousBlock();
		}
		if(lastCompletedBlock !=null && lastCompletedBlock.getTransactionList()!=null && lastCompletedBlock.getTransactionList().size()==10) {
			Map<String,Account> accountMap = lastCompletedBlock.getAccountBalanceMap();
			for(Map.Entry<String,Account> entry: accountMap.entrySet()) {
				accountBalanceMap.put(entry.getKey(), entry.getValue().getBalance());
			}
		}
		return accountBalanceMap;
	}
	
	public Block getBlock(int blockNumber) throws LedgerException {
		LOGGER.info("Get Block Object against the given block number from the block map");
		Block block = null;
		boolean blockExists= false;
		for(Map.Entry<Integer,Block> entry: blockMap.entrySet()) {
			if(entry.getKey()==blockNumber) {
				block= entry.getValue();
				blockExists= true;
				break;
			}
		}
		if(!blockExists) {
			throw new LedgerException("Get Block", "No Block with the given blockId of " + Integer.toString(blockNumber) + " exists.");
		}
		return block;
	}
	
	public Transaction getTransaction(String transactionId) throws LedgerException{
		LOGGER.info("Get Transaction Object against the given transactionId from all the blocks including the current block");
		Transaction mappedTransaction = null;
		boolean transactionExists= false;
		for(Map.Entry<Integer,Block> entry: blockMap.entrySet()) {
			List<Transaction>transactionsList= entry.getValue().getTransactionList();
			if(transactionsList !=null && transactionsList.size()>0 ) {
				for(Transaction transaction:  transactionsList) {
					if(transaction.getTransactionId().equalsIgnoreCase(transactionId)) {
						mappedTransaction= transaction;
						transactionExists= true;
						break;
					}
				}
			}
		}
		
		if(!transactionExists) {
			throw new LedgerException("Get Transaction", "No Transaction with the given transactionId of " + transactionId + " exists.");
		}
		return mappedTransaction;
	}
	
	public void validate() throws LedgerException{
		LOGGER.info("Validate the complete Blockchain,check the order of blocks,Account Balances, and Limit of Transactions in each block");
		int blockchainAmount= 0;
		Boolean isValidBlockHash = false;
		ArrayList<Integer> keys = new ArrayList<Integer>(blockMap.keySet());
		 for(int i=keys.size()-2; i>=1;i--){
			 Block currentBlock= blockMap.get(keys.get(i));
			 Boolean isValidTransactionCount = isBlockTransactionCountValid(currentBlock);
			 if(isValidTransactionCount) {
				 if(i==1) {
					 isValidBlockHash = true; 
				 }else {
					 isValidBlockHash= isBlockPreviousHashValid(currentBlock, blockMap.get(i-1));
				 }
				  
				 if(isValidBlockHash) {
					 blockchainAmount+= getBlockBalance(currentBlock.getAccountBalanceMap());
					 int totalBlockChainAmount= getBlockBalance(blockMap.get(keys.get(0)).getAccountBalanceMap());
					 if(blockchainAmount==totalBlockChainAmount) {
						 break;
					 }else {
						 throw new LedgerException("Validate", "Account Balance not updated correctly and does not matches final block chain amount.");
					 }
				 }else {
					 throw new LedgerException("Validate", "previousHash property of current block does not match the hash of previous block");
				 }
			 }else {
				 throw new LedgerException("Validate", "Invalid transaction count to the completed block.");
			 }
			
	     }
	}
	
	private boolean isBlockTransactionCountValid(Block block) {
		LOGGER.info("Validate Transaction count each Completed Block");
		boolean blockValid= false;
		if(block.getTransactionList().size()==10) {
			blockValid= true;
		}
		return blockValid;
	}
	
	private boolean isBlockPreviousHashValid(Block current, Block previous) {
		LOGGER.info("Validate Block order using hash for the entire blockchain");
		boolean blockValid= false;
		if(current.getPreviousHash().equalsIgnoreCase(previous.getHash())) {
			blockValid= true;
		}
		return blockValid;
		
	}
	
	private int getBlockBalance(Map<String, Account> blockBalance) {
		LOGGER.info("Get the balance amount of a Block");
		int balance= 0;
		for(Map.Entry<String, Account> entry: blockBalance.entrySet()) {
			balance+= entry.getValue().getBalance();
		}
		return balance;
	}
	
	private String calculateHash(String seed, Block currentBlock) {
		LOGGER.info("Calculate Hash using SHA256 using seed and all block properties");
		String accountString ="";
		String transactionString ="";
		if(currentBlock.getAccountBalanceMap() !=null) {
			for(Map.Entry<String, Account> accountmap: currentBlock.getAccountBalanceMap().entrySet()) {
				accountString = accountString + accountmap.getValue().toString();
			}
		}
		if(currentBlock.getTransactionList() !=null) {
			for(Transaction trnasaction: currentBlock.getTransactionList()) {
				transactionString = transactionString + trnasaction.toString();
			}
		}
		String augumentedInput = seed + accountString +transactionString;
		MessageDigest digest;
		String sha3Hex ="";
		try {
			digest = MessageDigest.getInstance("SHA3-256");
			byte[] encodedhash = digest.digest(augumentedInput.getBytes(StandardCharsets.UTF_8));
			sha3Hex = bytesToHex(encodedhash);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sha3Hex;
	}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	public String toString() {
		   String response= "New Ledger Created with name of:  "  + this.getName() + " and description of: " + this.getDescription() + " and seed of: " + this.getSeed();
		   return response;
	   }

}
