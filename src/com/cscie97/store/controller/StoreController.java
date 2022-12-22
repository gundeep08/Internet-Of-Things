package com.cscie97.store.controller;

import java.nio.file.AccessDeniedException;

import com.cscie97.ledger.Ledger;
import com.cscie97.ledger.LedgerException;
import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.command.processors.AssistCustomer;
import com.cscie97.store.command.processors.CustomerBasket;
import com.cscie97.store.command.processors.CheckAccountBalance;
import com.cscie97.store.command.processors.Checkout;
import com.cscie97.store.command.processors.CustomerSeen;
import com.cscie97.store.command.processors.Emergency;
import com.cscie97.store.command.processors.EnterStore;
import com.cscie97.store.command.processors.FetchProduct;
import com.cscie97.store.command.processors.MissingPerson;
import com.cscie97.store.command.processors.RobotTasks;
import com.cscie97.store.interfaces.Observer;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

public class StoreController implements Observer {
	
	private StoreModelService storeModelService;
	private Ledger ledger;
	private AuthenticationService authenticationService;
	private AuthToken authToken;
	
	public StoreController(StoreModelService storeModelService, Ledger ledger, AuthenticationService authenticationService, AuthToken authToken) {
		this.storeModelService = storeModelService;
		this.ledger =ledger;
		this.authenticationService =authenticationService;
		this.authToken =authToken;
	}

	@Override
	public void update(String event) throws AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		try {
			if(event.startsWith("Emergency")) {
				// Emergency fire in a12
				String location = event.substring(event.indexOf("location")+9).trim();
				String type = event.substring(event.indexOf("type")+4,event.indexOf("location")).trim();
				Emergency emergency = new Emergency(location,type,this.storeModelService, authToken);
				emergency.execute();
			}else if(event.startsWith("Customer Basket")) {
				//Customer c1 adds p1 from <aisle:shelf>.
				String customerId = event.substring(25,event.indexOf("action")).trim();
				String action = event.substring(event.indexOf("action")+7, event.indexOf("product")).trim();
				String productId = event.substring(event.indexOf("product")+8, event.indexOf("location")).trim();
				String location = event.substring(event.indexOf("location")+9).trim();
				CustomerBasket customerbasket = new CustomerBasket(customerId, action, productId, location,  this.storeModelService, authToken);
				customerbasket.execute();
			
			}else if(event.startsWith("Cleaning")) {
				//p1 on floor <store:aisle>
				String productId = event.substring(event.indexOf("product")+8,event.indexOf("location")).trim();
				String location = event.substring(event.indexOf("location")+9).trim();
				RobotTasks cleaning = new RobotTasks(productId,location, this.storeModelService, "cleaning", authToken);
				cleaning.execute();
			
			}else if(event.startsWith("BrokenGlass")) {
				//sound of breaking glass in <aisle>
				String productId = event.substring(event.indexOf("product")+8,event.indexOf("location")).trim();
				String location = event.substring(event.indexOf("location")+9).trim();
				RobotTasks brokenGlass = new RobotTasks(productId,location, this.storeModelService, "brokenGlass", authToken);
				brokenGlass.execute();
			
			}else if(event.startsWith("MissingPerson")) {
				//can you help me find <customer name>
				String customerName = event.substring(event.indexOf("Customer")+9).trim();
			    MissingPerson missingperson = new MissingPerson(customerName, this.storeModelService, authToken);
			    missingperson.execute();
			}else if(event.startsWith("CustomerSeen")) {
				//Customer enter <aisle>
				String location = event.substring(event.indexOf("location")+9,event.indexOf("imagepath")).trim();
				String imagePath = event.substring(event.indexOf("imagepath")+10).trim(); 
				CustomerSeen customerSeen = new CustomerSeen(location,imagePath, this.storeModelService, authToken);
				customerSeen.execute();
			}else if(event.startsWith("FetchProduct")) {
				//<customer> says: Please get me <number> of <product>
				String customerId = event.substring(event.indexOf("customer")+9,event.indexOf("product")).trim();
				int count = Integer.valueOf(event.substring(event.indexOf("count")+5).trim());
				String productId = event.substring(event.indexOf("product")+8, event.indexOf("count")).trim();
				FetchProduct fetchProduct = new FetchProduct(customerId, count, productId, this.storeModelService, authToken);
				fetchProduct.execute();
			
			}else if(event.startsWith("CheckAccountBalance")) {
				//Customer <customer> says "What is the total basket value?"
				String customerId = event.substring(event.indexOf("customer")+8).trim();
				CheckAccountBalance checkAccountBalance = new CheckAccountBalance(customerId, this.storeModelService, this.ledger, authToken);
				checkAccountBalance.execute();
			
			}else if(event.startsWith("AssistCustomer")) {
				//checkout: total weight of products in basket exceeds 10 lbs
				String customerId = event.substring(event.indexOf("customer")+8).trim();
				AssistCustomer assistCustomer = new AssistCustomer(customerId, this.storeModelService, authToken);
				assistCustomer.execute();
			}else if(event.startsWith("Checkout")) {
				//customer <customer> approaches turnstile
				String customerId = event.substring(event.indexOf("customer")+8).trim();
				Checkout checkout = new Checkout(customerId, this.storeModelService, this.ledger, authToken);
				checkout.execute();
			
			}else if(event.startsWith("EnterStore")) {
				//<customer> waiting to enter at the turnstile <turnstile>
				String customerId = event.substring(event.indexOf("customer")+8, event.indexOf("turnstile")).trim();
				String turnstileId = event.substring(event.indexOf("turnstile")+10).trim();
				EnterStore enterStore = new EnterStore(customerId,turnstileId, this.storeModelService, authToken);
				enterStore.execute();
			}
		 }catch (StoreModelServiceException e) {
				e.printStackTrace();
				}
		catch (LedgerException e) {
			e.printStackTrace();
			}
	}
	

}
