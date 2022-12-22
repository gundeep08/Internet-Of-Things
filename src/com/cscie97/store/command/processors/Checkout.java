package com.cscie97.store.command.processors;

import java.nio.file.AccessDeniedException;

import com.cscie97.ledger.Ledger;
import com.cscie97.ledger.LedgerException;
import com.cscie97.ledger.Transaction;
import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.Customer;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

//This is the command processor class invoked by the store controller when the checkout event is requested!!
public class Checkout implements Command {

	private String customerId;
	private StoreModelService storeModelService;
	private Ledger ledger;
	private AuthToken authToken;
	
	public  Checkout(String customerId, StoreModelService storeModelService, Ledger ledger, AuthToken authToken) {
		this.customerId =customerId;
		this.storeModelService =storeModelService;
		this.ledger =ledger;
		this.authToken =authToken;
	}
	
	@Override
	// This method initiates a transactions, processes it against the ledger and then allows customer to exit store through turnstile with a speaker message of goodbye.
	public void execute() throws StoreModelServiceException, LedgerException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		double basketPrice = this.storeModelService.getBasketPrice(this.customerId, authToken);
		Customer customer = this.storeModelService.showCustomer(customerId, authToken);
		 Transaction transaction = new Transaction("50", (int)basketPrice, 10, "checkout",  customer.getFirst_name() + " "+ customer.getLast_name(), "master");
		 transaction.setPayerAddress(customer.getAccount_address());
		 transaction.setRecieverAddress("master");
		 String transactionId= ledger.processTransaction(transaction);
		 ledger.validate();
		 String message = "goodbye  "+ customer.getFirst_name() + " "+ customer.getLast_name() + " ,thanks for shopping at " + this.storeModelService.getCurrentStore(authToken).getStore_name() + "!";
		System.out.println(message);
		 this.storeModelService.openTurnstiles(message, authToken);
		this.storeModelService.speakerTask(message, authToken);
	}

}
