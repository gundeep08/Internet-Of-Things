package com.cscie97.store.command.processors;

import java.awt.Image;
import java.util.Map;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.Customer;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

public class CustomerSeen implements Command {
	private String aisle;
	private StoreModelService storeModelService;
	private String imagePath;
	private AuthToken authToken;
	
	//This is the command processor class invoked by the store controller when the CustomerSeen event is requested!!
	public CustomerSeen(String aisle, String imagePath, StoreModelService storeModelService, AuthToken authToken) {
		this.aisle =aisle;
		this.storeModelService = storeModelService;
		this.imagePath =imagePath;
		this.authToken =authToken;
	}

	@Override
	//This method invokes set customer location based on the customer image.
	public void execute() throws StoreModelServiceException, AuthenticationException, InvalidAccessTokenException {
		this.storeModelService.setCustomerLocationFromCamera(this.imagePath, this.aisle, authToken);
	}

}
