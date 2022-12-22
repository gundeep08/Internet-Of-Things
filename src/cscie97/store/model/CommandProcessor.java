package cscie97.store.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.logging.Logger;
import com.cscie97.ledger.Ledger;
import com.cscie97.ledger.LedgerException;
import com.cscie97.ledger.Transaction;
import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.InvalidAccessTokenException;

public class CommandProcessor {

	private static int lineNumber;
	private static String operationName = "";
	private static StoreModelService storemodel;
	private static AuthenticationService authenticationService;
	private static Store currentStore;
	private static Ledger ledger;
	private AuthToken authToken;
	private static final Logger LOGGER = Logger.getLogger(CommandProcessor.class.getClass().getName());

	@SuppressWarnings("unused")
	public Object processCommand(String command) throws CommandProcessorException, StoreModelServiceException, LedgerException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Object response = null;
		String[] args= command.split(" ");
		if(command.startsWith("create-ledger")) {
			operationName = "create-ledger";
			if(command.contains("description") && command.contains("seed")) {
					String ledgerName= command.substring(13, command.indexOf("description")).trim();
					String description= command.substring(command.indexOf("description")+13, command.indexOf("seed")-2).trim();
					String seed= command.substring(command.indexOf("seed")+6).trim();
					response =  new Ledger(ledgerName,description.replaceAll("\"", ""), seed.substring(0, seed.length()-1));
					ledger=(Ledger)response;
				}else {
					throw new CommandProcessorException(operationName," Missing Description or seed in the create-ledger command ",lineNumber);
				}
			}else if(command.startsWith("create-account")) {
				operationName = "create-account";
				if(args.length==2) {
					response = ledger.createAccount(args[1]);
				}else {
					throw new CommandProcessorException(operationName," Missing accountId in the create-account command ",lineNumber);
				}
			}else if(command.startsWith("process-transaction")) {
				operationName = "process-transaction";
				if(command.contains("amount") && command.contains("fee") && command.contains("note") && command.contains("payer") && command.contains("receiver") && ledger!=null) {
					String transactionId= command.substring(20, command.indexOf("amount")).trim();
					int amount= Integer.parseInt(command.substring(command.indexOf("amount")+6, command.indexOf("fee")).trim());
					int fee= Integer.parseInt(command.substring(command.indexOf("fee")+3, command.indexOf("note")).trim());
					String note= command.substring(command.indexOf("note")+6, command.indexOf("payer")-2).trim();
					String payer= command.substring(command.indexOf("payer")+5, command.indexOf("receiver")).trim();
					String reciever= command.substring(command.indexOf("receiver")+8).trim();
					Transaction transaction = new Transaction(transactionId, amount, fee, note.replaceAll("\"", ""), payer, reciever);
					response= "Transaction processed with the transactionId of: " +ledger.processTransaction(transaction);
				}else {
					throw new CommandProcessorException(operationName," Missing arguments in the process-transaction command or the Ledger Object ",lineNumber);
				}
				
			}else if(command.startsWith("get-account-balances")) {
				operationName = "get-account-balances";
				if(args.length==1  && ledger!=null) {
					response = ledger.getAccountBalances();
				}else {
					throw new CommandProcessorException(operationName," Missing Ledger Object for the get-account-balances ",lineNumber);
				}
				
			}else if(command.startsWith("get-account-balance")) {
				operationName = "get-account-balance";
				if(ledger!=null && args.length==2) {
					response = ledger.getAccountBalance(args[1]);
				}else {
					throw new CommandProcessorException(operationName," Missing accountId in the get-account-balance command or the Ledger Object ",lineNumber);
				}	
			}else if(command.startsWith("get-block")) {
				operationName = "get-block";
				if(args.length==2 || ledger!=null && ledger.getBlockMap().containsKey(Integer.parseInt(args[1]))) {
					response = ledger.getBlock(Integer.parseInt(args[1])).toString();
				}else {
					throw new CommandProcessorException(operationName," Missing BlockId in the get-block command or the Ledger Object ",lineNumber);
				}
			}else if(command.startsWith("get-transaction")) {
				operationName = "get-transaction";
				if(args.length==2 && ledger!=null) {
					Transaction transaction = ledger.getTransaction(args[1]);
					response= transaction.toString();
				}else {
					throw new CommandProcessorException(operationName," Missing TransactionId in the get-transaction command or the Ledger Object ",lineNumber);
				}
			}else if(command.startsWith("validate")) {
				operationName = "validate";
				if(args.length==1 && ledger!=null) {
					ledger.validate();
					response ="Validated Successfully !!!!!";
				}else {
					throw new CommandProcessorException(operationName," Missing Ledger Object ",lineNumber);
				}
			}
			else if(command.startsWith("define store")) {
			operationName = "define store";
			if(command.contains("name") && command.contains("address")) {
					String storeId= command.substring(13, command.indexOf("name")).trim();
					String name= command.substring(command.indexOf("name")+6, command.indexOf("address")-2).trim();
					String address= command.substring(command.indexOf("address")+9, command.length()-1).trim();
					if(storeId !=null && name!=null && address!=null) {
						currentStore = storemodel.createStore(storeId, name, address, authToken);
						response = currentStore;
					}else{
						throw new CommandProcessorException(operationName," Missing StoreId or name or the address of the store to be defined ",lineNumber);
					}
				}else{
					throw new CommandProcessorException(operationName," Missing StoreId or name or the address of the store to be defined ",lineNumber);
				}
			}else if(command.startsWith("show store")) {
				operationName = "show store";
				response = "No Store Found";
				String storeId= command.substring(11).trim();
				if(storeId!=null) {
					response= storemodel.getStoreDetails(storeId,authToken);
				}else {
					throw new CommandProcessorException(operationName,"null storeId in the show store command ",lineNumber);
				}
			}else if(command.startsWith("define aisle")) {
				operationName = "define aisle";
				response = "No Aisle Found";
				if(command.contains("name") && command.contains("description") && command.contains("location")) {
					String storeAisleId= command.substring(13, command.indexOf("name")).trim();
					String storeId=storeAisleId.substring(0, storeAisleId.indexOf(":")).trim();
					String aisleId=storeAisleId.substring(storeAisleId.indexOf(":")+1).trim();
					String aisleName=  command.substring(command.indexOf("name")+5, command.indexOf("description")).trim();
					String aisleDescription=  command.substring(command.indexOf("description")+14, command.indexOf("location")-2);
					String aisleLocation=  command.substring(command.indexOf("location")+9).trim();
					response= storemodel.defineAisle(storeId,aisleId, aisleName, aisleDescription, aisleLocation,authToken);
					
				}else {
					throw new CommandProcessorException(operationName," Missing arguments in the define aisle command or the Ledger Object ",lineNumber);
				}
				
			}else if(command.startsWith("show aisle")) {
				operationName = "show aisle";
				String storeId=command.substring(11, command.indexOf(":")).trim();
				String aisleId=command.substring(command.indexOf(":")+1);
				if(storeId!=null && aisleId!=null ) {
					response = storemodel.showAisle(storeId, aisleId,authToken);
				}else {
					throw new CommandProcessorException(operationName," Missing or incorrect storeId or aisleId",lineNumber);
					}
				}
				else if(command.startsWith("define shelf")) {
				operationName = "define shelf";
				String storeId=command.substring(13, command.indexOf(":")).trim();
				String aisleId=command.substring(command.indexOf(":")+1, command.lastIndexOf(":")).trim();
				String shelfId=command.substring(command.lastIndexOf(":")+1,command.indexOf("name")).trim();
				String shelfName= command.substring(command.indexOf("name")+5, command.indexOf("level")).trim();
				String shelfLevel= command.substring(command.indexOf("level")+6, command.indexOf("description")).trim();
				String shelfDescription =command.substring(command.indexOf("description")+13, command.indexOf("temperature")-2).trim();
				String shelfTemperature = command.substring(command.indexOf("temperature")+12).trim();
				if(storeId !=null && aisleId!=null && shelfId!=null) {
					response = storemodel.defineShelf(storeId, aisleId, shelfId, shelfName, shelfLevel, shelfDescription, shelfTemperature,authToken);
				}else {
					throw new CommandProcessorException(operationName," Missing arguments for creating shelf",lineNumber);
				}	
			}else if(command.startsWith("show shelf")) {
				operationName = "show shelf";
				String storeId=command.substring(11, command.indexOf(":")).trim();
				String aisleId=command.substring(command.indexOf(":")+1, command.lastIndexOf(":"));
				String shelfId=command.substring(command.lastIndexOf(":")+1);
				response = storemodel.showShelf(storeId,aisleId,shelfId,authToken);
				
			}else if(command.startsWith("define product")) {
				operationName = "define product";
				String productId=command.substring(14, command.indexOf("name")).trim();
				String name=command.substring(command.indexOf("name")+5, command.indexOf("description")).trim();
				String description=command.substring(command.indexOf("description")+12, command.lastIndexOf("size")).trim();
				String size=command.substring(command.indexOf("size")+5, command.lastIndexOf("category")).trim();
				String category=command.substring(command.lastIndexOf("category")+9, command.lastIndexOf("unit_price")).trim();
				double unitPrice=Double.parseDouble(command.substring(command.lastIndexOf("unit_price")+11, command.lastIndexOf("temperature")));
				String temperature=command.substring(command.lastIndexOf("temperature")+12).trim();
				if(productId!=null && description!=null && size!=null && category!=null && unitPrice!=0 && temperature!=null) {
					response= storemodel.defineProduct(productId, name, description, size, category, unitPrice, temperature,authToken);
				}else {
					throw new CommandProcessorException(operationName," Missing TransactionId in the get-transaction command or the Ledger Object ",lineNumber);
				}
			}else if(command.startsWith("show product")) {
				operationName = "show product";
				String productId=command.substring(13).trim();
				if(productId!= null) {
					response =storemodel.showProduct(productId,authToken);
				}else {
					throw new CommandProcessorException(operationName," Missing productId  ",lineNumber);
				}
			}else if(command.startsWith("define inventory")) {
				operationName = "define inventory";
				String inventoryId=command.substring(17, command.indexOf("location")).trim();
				String location=command.substring(command.indexOf("location")+9, command.indexOf("capacity")).trim();
				int capacity=Integer.parseInt(command.substring(command.indexOf("capacity")+9, command.indexOf("count")).trim());
				int count=Integer.parseInt(command.substring(command.indexOf("count")+6, command.indexOf("product")).trim());
				String productId = command.substring(command.indexOf("product")+8).trim();
				response = storemodel.defineInventory(inventoryId, location, capacity, count, productId,authToken);
				
			}else if(command.startsWith("show inventory")) {
				operationName = "show inventory";
				String inventoryId=command.substring(15).trim();
				if(inventoryId!= null) {
					response =storemodel.showInventory(inventoryId,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Inventory Exists with the given inventoryId  ",lineNumber);
				}
			}else if(command.startsWith("update inventory")) {
				operationName = "update inventory";
				String inventoryId=command.substring(17, command.indexOf("update_count")).trim();
				int count = Integer.parseInt(command.substring(command.indexOf("update_count")+13).trim());
				if(inventoryId!= null) {
					response= storemodel.updateInventory(inventoryId, count,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Inventory Exists with the given inventoryId  ",lineNumber);
				}
			}else if(command.startsWith("define customer")) {
				operationName = "define customer";
				String customerId=command.substring(16, command.indexOf("first_name")).trim();
				String firstName=command.substring(command.indexOf("first_name")+11, command.indexOf("last_name")).trim();
				String lastName=command.substring(command.indexOf("last_name")+10, command.indexOf("type")).trim();
				String type= command.substring(command.indexOf("type")+5, command.indexOf("email_address")).trim();
				String emailAddress=command.substring(command.indexOf("email_address")+14,command.indexOf("account")).trim();
				String accountAddress=command.substring(command.indexOf("account")+8).trim();
				if(customerId!= null && firstName!= null && lastName!= null && type!= null && emailAddress!= null && accountAddress!= null) {
					response= storemodel.defineCustomer(customerId,firstName, lastName, type, emailAddress, accountAddress,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Inventory Exists with the given inventoryId  ",lineNumber);
				}
			}else if(command.startsWith("show customer")) {
				operationName = "show customer";
				String customerId=command.substring(14).trim();
				if(customerId!= null) {
					response= storemodel.showCustomer(customerId,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Customer Exists with the given customerId  ",lineNumber);
				}
			}else if(command.startsWith("update customer")) {
				operationName = "update customer";
				String customerId=command.substring(16, command.indexOf("location")).trim();
				String locationId = command.substring(command.indexOf("location")).trim();
				if(customerId!= null && locationId !=null) {
					response= storemodel.updateCustomer(customerId,locationId,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Customer Exists with the given customerId  ",lineNumber);
				}
			}else if(command.startsWith("define basket")) {
				operationName = "define basket";
				String basketId=command.substring(14).trim();
				if(basketId!= null) {
					response = storemodel.defineBasket(basketId,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Inventory Exists with the given inventoryId  ",lineNumber);
				}
			}else if(command.startsWith("assign basket")) {
				operationName = "assign basket";
				String basketId=command.substring(14,command.indexOf("customer")).trim();
				String customerId=command.substring(command.indexOf("customer")+9).trim();
				if(basketId!= null && customerId!=null) {
					response =  storemodel.assignBasket(basketId, customerId,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Customer or Basket Exists with the given Id's or the customer has another basket assigned.",lineNumber);
				}
			}else if(command.startsWith("get_customer_basket")) {
				operationName = "get_customer_basket";
				String customerBasketId=command.substring(20).trim();
				if(customerBasketId!= null) {
					response = storemodel.getCustomerBasket(customerBasketId,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Customer or Basket Exists with the given Id's or the customer has another basket assigned.",lineNumber);
				}	
			}else if(command.startsWith("add_basket_item")) {
				operationName = "add_basket_item";
				String basketId=command.substring(16, command.indexOf("product")).trim();
				String productId = command.substring(command.indexOf("product")+8, command.indexOf("item_count")).trim();
				int itemCount = Integer.parseInt(command.substring(command.indexOf("item_count")+11).trim());
				if(basketId !=null && productId!=null) {
					response = storemodel.addBasketItem(basketId,productId, itemCount,authToken);
				}else {
					throw new CommandProcessorException(operationName,"Error while adding items to the basket.",lineNumber);
				}
			}else if(command.startsWith("remove_basket_item")) {
				operationName = "remove_basket_item";
				String basketId=command.substring(19, command.indexOf("product")).trim();
				String productId = command.substring(command.indexOf("product")+8, command.indexOf("item_count")).trim();
				int itemCount = Integer.parseInt(command.substring(command.indexOf("item_count")+11).trim());
				if(basketId!=null && productId!=null) {
					response = storemodel.removeBasketItem(basketId,productId ,itemCount,authToken);
				}else {
					throw new CommandProcessorException(operationName,"Error while adding items to the basket.",lineNumber);
				}
			}else if(command.startsWith("clear_basket")) {
				operationName = "clear_basket";
				String basketId=command.substring(13).trim();
				if(basketId !=null) {
					response = storemodel.clearBasket(basketId,authToken);
				}else {
					throw new CommandProcessorException(operationName,"Invalid BasketId",lineNumber);
				}
			}else if(command.startsWith("Show basket_items")) {
				operationName = "Show basket_items";
				String basketId=command.substring(18).trim();
				if(basketId!= null) {
					response = storemodel.showBasketItems(basketId,authToken);
				}else {
					throw new CommandProcessorException(operationName,"Invalid BasketId.",lineNumber);
				}
			}else if(command.startsWith("define device")) {
				operationName = "define device";
				String deviceId=command.substring(14, command.indexOf("name")).trim();
				String name=command.substring(command.indexOf("name")+5,command.indexOf("type")).trim();
				String type=command.substring(command.indexOf("type")+5,command.indexOf("location")).trim();
				String location=command.substring(command.indexOf("location")+9).trim();
				
				if(deviceId!= null && name !=null && type!=null && location!=null) {
					response = storemodel.defineDevice(type, name,location,deviceId,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Customer or Basket Exists with the given Id's or the customer has another basket assigned.",lineNumber);
				}
			}else if(command.startsWith("show device")) {
				operationName = "show device";
				String deviceId=command.substring(12).trim();
				if(deviceId!= null ) {
					response = storemodel.showDevice(deviceId,authToken);
				}else {
					throw new CommandProcessorException(operationName," No Sensor Type or Appliance Type Exists with the given Device Id.",lineNumber);
				}
			}else if(command.startsWith("create_event")) {
				operationName = "create_event";
				String customerBasketId=command.substring(13, command.lastIndexOf("event")).trim();
				String eventDescription = command.substring(command.lastIndexOf("event")+6).trim();
				if(customerBasketId!= null) {
					response= storemodel.createEvent(customerBasketId,  eventDescription, this.ledger);
				}else {
					throw new CommandProcessorException(operationName," No Customer or Basket Exists with the given Id's or the customer has another basket assigned.",lineNumber);
				}
			}else if(command.startsWith("create command")) {
				operationName = "create command";
				String customerBasketId=command.substring(15, command.indexOf("message")).trim();
				String applianceCommand=command.substring(command.indexOf("message")+8).trim();
				if(customerBasketId!= null) {
					response = storemodel.sendApplianceCommand(customerBasketId,  applianceCommand, this.ledger);
				}else {
					throw new CommandProcessorException(operationName," No Customer or Basket Exists with the given Id's or the customer has another basket assigned.",lineNumber);
				}
			}else if(command.startsWith("event")) {
				operationName = command.substring(6);
				if(command.length()>6) {
					storemodel.invokeUpdate(command.substring(6).trim());
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName," No valid Event provided.",lineNumber);
				}
				
			}else if(command.startsWith("add_user_credential")) {
				operationName = "add_user_credential";
				if(command.length()>19) {
					String user_id = command.substring(19, command.indexOf(",")).trim();
					String credential_type = command.substring(command.indexOf(",")+1, command.lastIndexOf(",")).trim();
					String credential_details = command.substring(command.lastIndexOf(",")+1).trim();
					storemodel.assignUserCredentials(user_id, credential_type, credential_details,authToken);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName," No valid User to attach credentials.",lineNumber);
				}
				
			}else if(command.startsWith("add_role_to_user")) {
				operationName = "add_role_to_user";
				if(command.length()>16) {
					String user_id = command.substring(16, command.indexOf(",")).trim();
					String role = command.substring(command.lastIndexOf(",")+1).trim();
					storemodel.assignUserRoles(user_id, role,authToken);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName," No valid User to attach role.",lineNumber);
				}
			}else if(command.startsWith("create_resource_role")) {
				operationName = "create_resource_role";
				if(command.length()>20) {
					String resource_role_name = command.substring(20, command.indexOf(",")).trim();
					String role = command.substring(command.indexOf(resource_role_name) + resource_role_name.length()+1, command.lastIndexOf(",")).trim();
					String resource = command.substring(command.lastIndexOf(",")+1).trim();
					storemodel.createResourseRole(resource_role_name, role,resource,authToken);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName,"Unable to create a resource role.",lineNumber);
				}
				
				
			}else if(command.startsWith("add_resource_role_to_user")) {
				operationName = "add_resource_role_to_user";
				if(command.length()>25) {
					String user_id = command.substring(25, command.indexOf(",")).trim();
					String resource_role = command.substring(command.lastIndexOf(",")+1).trim();
					storemodel.addResourseRole(user_id, resource_role,authToken);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName,"Unable to create a resource role.",lineNumber);
				}
				
				
			}else if(command.startsWith("add_permission_to_role")) {
				operationName = "add_permission_to_role";
				if(command.length()>22) {
					//add_permission_to_role, admin_role, user_admin
					String role_id = command.substring(23, command.lastIndexOf(",")).trim();
					String permission_id = command.substring(command.lastIndexOf(",")+2).trim();
					storemodel.addPermissionToRole(role_id, permission_id,authToken);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName,"Unable to assign permissions to the given role.",lineNumber);
				}
				
			}else if(command.startsWith("define_role")) {
				operationName = "define_role";
				if(command.length()>12) {
					String role_id = command.substring(13, command.substring(13,command.length()).indexOf(",")+13).trim();
					String role_name = command.substring(command.indexOf(role_id) + role_id.length()+2, command.lastIndexOf(",")-1).trim();
					String role_description = command.substring(command.lastIndexOf(",")+3, command.length()-1).trim();
					storemodel.defineRole(role_id, role_name, role_description,authToken);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName,"Unable to create a new role.",lineNumber);
				}
			}else if(command.startsWith("define_permission")) {
				operationName = "define_permission";
				if(command.length()>18) {
					String permission_id = command.substring(18, command.substring(18,command.length()).indexOf(",")+18).trim();
					String temp = command.substring(command.indexOf(permission_id)+ permission_id.length()+3,command.length());
					String permission_name = temp.substring(0,command.indexOf(",")+1).trim();
					String permission_description = command.substring(command.indexOf(permission_name)+ permission_name.length()+4, command.length()-1).trim();
					storemodel.definePermission(permission_id, permission_name, permission_description,authToken);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName,"Unable to create a new permission.",lineNumber);
				}	
			}else if(command.startsWith("create_user")) {
				operationName = "create_user";
				if(command.length()>12) {
					String user_id = command.substring(command.indexOf(",")+1, command.lastIndexOf(",")).trim();
					String user_name = command.substring(command.lastIndexOf(",")+3, command.length()-1).trim();
					storemodel.createUser(user_id, user_name,authToken);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName,"Unable to create a new user.",lineNumber);
				}	
			}else if(command.startsWith("login")) {
				operationName = "login";
				if(command.length()>5) {
					String user_id = command.substring(5).trim();
					authToken = storemodel.login(user_id);
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName,"Unable to login.",lineNumber);
				}	
			}else if(command.startsWith("check inventory")) {
				operationName = "check inventory";
				if(command.length()==15) {
					storemodel.checkInventory();
					response ="Event of " + operationName + " processed successfully!!!";
				}else {
					throw new CommandProcessorException(operationName,"Unable to check inventory.",lineNumber);
				}	
			}
		System.out.println(response.toString());
		return response.toString();
	}
	
	public void processCommandFile(String filepath)throws CommandProcessorException, StoreModelServiceException, LedgerException, AuthenticationException, InvalidAccessTokenException {
		LOGGER.info("Process the each commands indiviually from the file at the filePath location.");
		  File file = new File(filepath);
		  storemodel = new StoreModelService(getAuthenticationSingleton(),authToken);
		  try {
			  BufferedReader br= new BufferedReader(new FileReader(file));
			  String st;
			  while ((st = br.readLine()) != null) {
				  System.out.println(st);
				  lineNumber++;
				  if(!st.startsWith("#")) {
					  Object output = processCommand(st);
				  }
			 }
			  LOGGER.info("Successfully Completed Executing the sample script !!!!");
		} catch (FileNotFoundException e) {
			throw new CommandProcessorException(operationName,"No File found",lineNumber);
		} catch (IOException e) {
			throw new CommandProcessorException(operationName,"Exception while reading the script ",lineNumber);
		}
	}
	
	// Singleton design pattern to create an instance of AuthenticationService.
	private AuthenticationService getAuthenticationSingleton() {
		if (authenticationService == null) {
			authenticationService = new AuthenticationService();
			authToken = new AuthToken("admin");
	  }
		return authenticationService;
	}

	
	
}
