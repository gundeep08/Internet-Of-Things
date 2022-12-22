package com.cscie97.store.command.processors;

import java.nio.file.AccessDeniedException;

import com.cscie97.ledger.Ledger;
import com.cscie97.ledger.LedgerException;
import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.Customer;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

//This is the command processor class invoked by the store controller when the check customer account balance event is requested!!
public class CheckAccountBalance implements Command{
	
	private String customerId;
	private StoreModelService storeModelService;
	private Ledger ledger;
	private AuthToken authToken;
	
	public CheckAccountBalance(String customerId, StoreModelService storeModelService, Ledger ledger,AuthToken authToken) {
		this.customerId=customerId;
		this.storeModelService =storeModelService;
		this.ledger = ledger;
		this.authToken =authToken;
	}

	@Override
	//This method compares the basket total value and the customer account balance and notifies using speaker method accordingly.
	public void execute() throws StoreModelServiceException, LedgerException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		double basketPrice = this.storeModelService.getBasketPrice(this.customerId, authToken);
		Customer customer = this.storeModelService.showCustomer(customerId,authToken);
		String speakerMessage = "";
		int accountBalance = this.storeModelService.getCustomerAccountBalance(customer,this.ledger, authToken);
		if(accountBalance>basketPrice) {
			speakerMessage = "Total value of basket items " + basketPrice + " is less than your account balance of " + accountBalance;
		}else if(accountBalance>basketPrice) {
			speakerMessage = "Total value of basket items " + basketPrice + " is more than your account balance of " + accountBalance;
		}else {
			speakerMessage = "Total value of basket items " + basketPrice + " is equal to your account balance of " + accountBalance;
		}
		System.out.println(speakerMessage);
		this.storeModelService.speakerTask(speakerMessage, authToken);
		}

}
