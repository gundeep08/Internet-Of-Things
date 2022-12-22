package com.cscie97.store.command.processors;

import java.nio.file.AccessDeniedException;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.Customer;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

//This is the command processor class invoked by the store controller when the Enter Store event is requested!!
public class EnterStore implements Command{
	
	private String customerId;
	private String turnstileId;
	private StoreModelService storeModelService;
	private AuthToken authToken;
	
	public EnterStore(String customerId, String turnstileId, StoreModelService storeModelService,AuthToken authToken) {
		this.customerId =customerId;
		this.turnstileId =turnstileId;
		this.storeModelService =storeModelService;
		this.authToken =authToken;
	}

	@Override
	//This method first get a available basket and assign to the customer and then invokes actions of open turnstiles and speaker messages.
	public void execute() throws StoreModelServiceException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		Customer customer = this.storeModelService.showCustomer(this.customerId, authToken);
		String basketId = this.storeModelService.getAvailablebasket(authToken);
		this.storeModelService.assignBasket(basketId, customer.getCustomer_id(), authToken);
		String message = "Hello "+ customer.getFirst_name() + " "+ customer.getLast_name() + " ,welcome to " + this.storeModelService.getCurrentStore(authToken).getStore_name() + "!";
		System.out.println(message);
		this.storeModelService.openTurnstiles(message, authToken);
		this.storeModelService.speakerTask(message, authToken);
	}
	
}
