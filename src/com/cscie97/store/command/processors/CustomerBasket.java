package com.cscie97.store.command.processors;

import java.nio.file.AccessDeniedException;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.Inventory;
import cscie97.store.model.Store;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

//This is the command processor class invoked by the store controller when the custoemrBasket event is requested!!
public class CustomerBasket implements Command{
	
	private String customerId;
	private String action;
	private String productId;
	private String location;
	private StoreModelService storeModelService;
	private AuthToken authToken;
	
	public CustomerBasket(String customerId, String action, String productId, String location, StoreModelService storeModelService, AuthToken authToken) {
		this.customerId =customerId;
		this.action =action;
		this.productId =productId;
		this.location =location;
		this.storeModelService =storeModelService;
		this.authToken =authToken;
	}

	@Override
	//This method gets the inventory of the product and customer basket details and on any customer action it updates basket item list and the product inventory count.
	public void execute() throws StoreModelServiceException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		String basketId = this.storeModelService.getCustomerBasket(this.customerId, authToken);
		Inventory inventory = this.storeModelService.getProductInventory(this.productId, authToken);
		int itemcount = inventory.getCount();
		if(this.action.equalsIgnoreCase("Add")) {
			this.storeModelService.addBasketItem(basketId, this.productId, 1, authToken);
			inventory.setCount(itemcount-1);
		}else if(this.action.equalsIgnoreCase("Remove")) {
			this.storeModelService.removeBasketItem(basketId, this.productId, 1, authToken);
			inventory.setCount(itemcount+1);
		}
		
	}

}
