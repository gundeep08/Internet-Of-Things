package com.cscie97.store.command.processors;

import java.nio.file.AccessDeniedException;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;
import cscie97.store.model.Customer;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

//This is the command processor class invoked by the store controller when the assist customer event is requested!!
public class AssistCustomer implements Command {

	private String customerId;
	private StoreModelService storeModelService;
	private AuthToken authToken;
	
	public AssistCustomer(String customerId, StoreModelService storeModelService,AuthToken authToken) {
		this.customerId =customerId;
		this.storeModelService =storeModelService;
		this.authToken =authToken;
	}
	@Override
	//This method with first get the total size of basket items and if greater than 10, it will ask robot to assist customer to car.
	public void execute() throws StoreModelServiceException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		double basketProductsSize = this.storeModelService.getBasketSize(this.customerId, authToken);
		Customer customer = this.storeModelService.showCustomer(customerId, authToken);
		if(basketProductsSize>10) {
			String task = "Assist customer " +  customer.getFirst_name() + " " + customer.getLast_name() + " to car ";
			this.storeModelService.allRobotsActions(task, authToken);
		}
	}

}
