package com.cscie97.store.command.processors;



import java.nio.file.AccessDeniedException;
import java.util.List;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.Robot;
import cscie97.store.model.Speaker;
import cscie97.store.model.Store;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;
import cscie97.store.model.Turnstile;

//This is the command processor class invoked by the store controller when the emergency event is requested!!
public class Emergency implements Command {

	private String location;
	private String type;
	private StoreModelService storeModelService;
	private AuthToken authToken;
	
	public Emergency(String location, String type, StoreModelService storeModelService, AuthToken authToken) {
		this.location = location;
		this.type = type;
		this.storeModelService = storeModelService;
		this.authToken =authToken;
	}
	@Override
	//This method invokes actions to openTurstile, robot actions in a synchronous manner.
	public void execute() throws StoreModelServiceException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		this.storeModelService.openTurnstiles("There is a "+ this.type +" emergency at location " + this.location, authToken);
		this.storeModelService.firstRobotAction("Address "+ this.type +" emergency at location " + this.location, authToken);
		this.storeModelService.remainingRobotActions("Assist customers leaving the " + this.storeModelService.getCurrentStore(authToken),authToken);
	}

}
