package com.cscie97.store.command.processors;

import java.nio.file.AccessDeniedException;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

//This is the command processor class invoked by the store controller when the MissingPerson event is requested!!
public class MissingPerson implements Command{
	
	private String customerName;
	private StoreModelService storeModelService;
	private AuthToken authToken;
	
	public MissingPerson(String customerName, StoreModelService storeModelService, AuthToken authToken) {
		this.customerName=customerName;
		this.storeModelService =storeModelService;
		this.authToken =authToken;
	}

	@Override
	//This method first gets the customer location and then invokes speaker message action to inform.
	public void execute() throws StoreModelServiceException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		String customerLocation = this.storeModelService.getCustomerLocation(this.customerName,authToken);
		String message = this.customerName + " is in aisle" +  customerLocation;
		System.out.println(message);
		this.storeModelService.speakerTask(message,authToken);
	}

}
