package com.cscie97.store.command.processors;

import java.nio.file.AccessDeniedException;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.Inventory;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

//This is the command processor class invoked by the store controller when the FetchProduct event is requested!!
public class FetchProduct implements Command {
	
	private int count;
	private String productId;
	private String customerId;
	private  StoreModelService storeModelService;
	private AuthToken authToken;
	
	public FetchProduct(String customerId, int count, String productId, StoreModelService storeModelService, AuthToken authToken) {
		this.count=count;
		this.productId=productId;
		this.customerId=customerId;
		this.storeModelService=storeModelService;
		this.authToken =authToken;
	}

	@Override
	//This method first gets the inventory of the product and invoke the robot actions.
	public void execute() throws StoreModelServiceException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		Inventory inventory = this.storeModelService.getProductInventory(productId,authToken);
		String robotCommand = "Fetch " + this.count +  " of" + this.storeModelService.showProduct(this.productId,authToken).getName()+  " from " + inventory.getAisleId() +  " and " + inventory.getShelfId() + "and bring to customer" + this.storeModelService.showCustomer(this.customerId,authToken).getFirst_name()+ " " + this.storeModelService.showCustomer(this.customerId,authToken).getLast_name() + " in aisle " +  this.storeModelService.showCustomer(this.customerId, authToken).getLocation();
		System.out.println(robotCommand);
		this.storeModelService.allRobotsActions(robotCommand,authToken);
	}

}
