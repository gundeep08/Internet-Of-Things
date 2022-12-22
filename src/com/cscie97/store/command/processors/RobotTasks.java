package com.cscie97.store.command.processors;

import java.nio.file.AccessDeniedException;
import java.util.List;

import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.interfaces.Command;

import cscie97.store.model.Robot;
import cscie97.store.model.Store;
import cscie97.store.model.StoreModelService;
import cscie97.store.model.StoreModelServiceException;

//This is the command processor class invoked by the store controller when the RobotTasks event is requested!!
public class RobotTasks implements Command{
	
	private String productId;
	private String location;
	private String type;
	private StoreModelService storeModelService;
	private AuthToken authToken;

	public RobotTasks( String productId, String location, StoreModelService storeModelService, String type, AuthToken authToken) {
		this.productId = productId;
		this.location =location;
		this.storeModelService =storeModelService;
		this.type = type;
		this.authToken =authToken;
	}
	
	public RobotTasks(String location, StoreModelService storeModelService, String type) {
		this.location =location;
		this.storeModelService =storeModelService;
		this.type = type;
	}
	@Override
	//This method first fetches all robots of a given store, and assign appropriate tasks.
	public void execute() throws StoreModelServiceException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException  {
		// TODO Auto-generated method stub
		List<Robot> robots = this.storeModelService.getCurrentStore(authToken).getRobots();
		if(this.type.equalsIgnoreCase("cleaning")) {
			this.storeModelService.firstRobotAction("clean up" + this.productId + " in " + this.location, authToken);
		}else if(this.type.equalsIgnoreCase("BrokenGlass")) {
			this.storeModelService.firstRobotAction("clean up broken glass in"  + this.location,authToken);
		}		
		
	}

}
