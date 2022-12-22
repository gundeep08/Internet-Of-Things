package cscie97.store.model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.cscie97.store.interfaces.Observer;
import com.cscie97.ledger.Ledger;
import com.cscie97.ledger.LedgerException;
import com.cscie97.store.authentication.AuthToken;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.Credential;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.authentication.Permission;
import com.cscie97.store.authentication.Resource;
import com.cscie97.store.authentication.ResourceRole;
import com.cscie97.store.authentication.Role;
import com.cscie97.store.authentication.User;
import com.cscie97.store.authentication.Visitable;
import com.cscie97.store.authentication.Visitor;
import com.cscie97.store.controller.StoreController;
import com.cscie97.store.interfaces.Observable;

@SuppressWarnings("unused")
public class StoreModelService implements Observable {
	private static final Logger LOGGER = Logger.getLogger(StoreModelService.class.getClass().getName());
	private Map<String, Store> storeCollection = new HashMap<>();
	private Map<String, Product> productCollection = new HashMap<>();
	private Map<String, Inventory> inventoryCollection = new HashMap<>();
	private Map<String, String> producyInventoryMapping = new HashMap<>();
	private Map<String, Customer> customerCollection = new HashMap<>();
	private Map<String, Basket> basketCollection = new HashMap<>();
	private Map<String, String> customerBasketMapping = new HashMap<>();
	private Map<String, Sensors> sensorsMapping = new HashMap<>();
	private Map<String, Appliances> appliancesMapping = new HashMap<>();
	private Map<String, Robot> robotMapping = new HashMap<>();
	private Map<String, Speaker> speakerMapping = new HashMap<>();
	private Map<String, Turnstile> turnstileMapping = new HashMap<>();
	private Map<String, String> deviceLastCommand= new HashMap<>();
	private Map<String, String> deviceLastEvent= new HashMap<>();
	private List<Observer> observersList = new ArrayList<>();
	private Map<String, User> userCollection = new HashMap<>();
	private static AuthenticationService authenticationService = null;
	private String event;
	private Store currentStore;
	private AuthToken authToken;
	
	
	public StoreModelService(AuthenticationService authenticationService, AuthToken authToken) {
		this.authenticationService =authenticationService;
		this.authToken =authToken;
	}
	//This method is responsible for creating a new store
	public Store createStore(String storeId, String name, String address, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		if(authenticationService.checkAccess("admin",authToken)) {
			if(!storeCollection.containsKey(storeId)) {
				currentStore =  new Store(storeId,name,address);
				storeCollection.put(storeId, currentStore);
			}else {
				throw new StoreModelServiceException(" Store exists with the same StoreId ", "createStore");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to createStore ", "createStore");
		}
		
		return currentStore;
	}
	
	//This method is responsible for returning a store based on storeId.
	public Store getStoreDetails(String storeId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Store store = null;
		if(authenticationService.checkAccess("admin", authToken)) {
			if(storeCollection.containsKey(storeId)) {
				 store = storeCollection.get(storeId);
			}else {
				throw new StoreModelServiceException("No store exists with the given storeId in the show store command ", "getStoreDetails");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to getStoreDetails ", "getStoreDetails");
		}
	return store;
	}
	
	//This method is responsible for defining an aisle within a particular store
	public Aisle defineAisle(String storeId, String aisleId, String aisleName, String aisleDescription, String aisleLocation, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Aisle aisle = null;
		if(authenticationService.checkAccess("admin", authToken)) {
			aisle = new Aisle(storeId, aisleId, aisleName, aisleDescription, aisleLocation );
			Store store= storeCollection.get(storeId);
			List<Aisle> storeAisles = null;
			if(store.getStoreAisles() ==null) {
				storeAisles = new ArrayList<>();
			}else {
				storeAisles = store.getStoreAisles();
			}
			storeAisles.add(aisle);
			store.setStoreAisles(storeAisles);
			storeCollection.put(storeId, store);
		}else {
			throw new AuthenticationException("User does not have permissions to defineAisle ", "defineAisle");
		}
		return aisle;
	}
	
	//This method is responsible for returning a Aisle with a given aisleId.
	public Aisle showAisle(String storeId , String aisleId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Aisle finalAisle = null;
		if(authenticationService.checkAccess("admin", authToken)) {
			Store store = storeCollection.get(storeId);
			if(store !=null && store.getStoreAisles() !=null && store.getStoreAisles().size()>0) {
				for(Aisle aisle:store.getStoreAisles()) {
					if(aisle.getAisle_number().equalsIgnoreCase(aisleId)) {
						finalAisle= aisle;
					}
				}
			}else {
				throw new StoreModelServiceException(" Missing or incorrect AisleId", "showAisle");
				}
		}else {
			throw new AuthenticationException("User does not have permissions to showAisle ", "showAisle");
		}
		
		return finalAisle;
	}
	//This method is used for defining a shelf in an given aisle based on aisleId
	public Shelf defineShelf(String storeId, String aisleId, String shelfId, String shelfName, String shelfLevel, String shelfDescription, String shelfTemperature, AuthToken authToken)throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Shelf finalShelf  = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			finalShelf  = new Shelf(storeId, aisleId, shelfId, shelfName, shelfLevel, shelfDescription, shelfTemperature);
			Store store= storeCollection.get(storeId);
			Aisle shelfAisle =null;
			List<Aisle> storeAisles = store.getStoreAisles();
			for(Aisle aisle:storeAisles) {
				if(aisle.getAisle_number().equalsIgnoreCase(aisleId)) {
					shelfAisle= aisle;
					break;
				}
			}
			List<Shelf> shelfs = shelfAisle.getShelfs();
			if(shelfs == null) {
				shelfs = new ArrayList<>();
			}
			shelfs.add(finalShelf);
			shelfAisle.setShelfs(shelfs);
		}else {
			throw new AuthenticationException("User does not have permissions to defineShelf ", "defineShelf");
		}
		return finalShelf;
	}
	
	//This method is responsible for returning a Shelf with a given shelfId.
	public Shelf showShelf(String storeId, String aisleId, String shelfId, AuthToken authToken)throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Aisle shelfAisle =null;
		Shelf currentShelf = null;
		Shelf finalShelf = null;
		if(authenticationService.checkAccess("admin", authToken)) {
			Store store = storeCollection.get(storeId);
			if(store !=null && aisleId!=null && store.getStoreAisles() !=null && store.getStoreAisles().size()>0) {
				for(Aisle aisle:store.getStoreAisles()) {
					if(aisle.getAisle_number().equalsIgnoreCase(aisleId)) {
						shelfAisle= aisle;
						break;
					}
				}
				if(shelfAisle !=null) {
					for(Shelf shelf : shelfAisle.getShelfs()) {
						if(shelf.getShelf_id().equalsIgnoreCase(shelfId)) {
							currentShelf = shelf;
							break;
						}
					}
				if(currentShelf!=null) {
					finalShelf = currentShelf;
				}else {
					throw new StoreModelServiceException(" Missing Shelf with the given ShelfId","showShelf");
				}
				}else {
					throw new StoreModelServiceException(" Missing Aisle with the given AisleId","showShelf");
				}
			}else {
				throw new StoreModelServiceException(" Missing Store or Aisle or Shelf Details","showShelf");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to showShelf ", "showShelf");
		}
		
		return finalShelf;
	}
	
	//This method is used to define a new Product and assign various product attributes.
	public Product defineProduct(String productId, String name, String description, String size, String category, double unitPrice, String temperature, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Product product = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			if(!productCollection.containsKey(productId)) {
				product= new Product(productId, name, description, size, category, unitPrice, temperature);
				productCollection.put(productId,product);
			}else {
				throw new StoreModelServiceException(" Product already exists with a given ProductId ", "defineProduct");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to defineProduct ", "defineProduct");
		}
		
		return product;
		
	}
	
	//This method is to return a product based on productId
	public Product showProduct(String productId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Product product = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			if(productCollection.containsKey(productId)) {
				product =productCollection.get(productId);
			}else {
				 throw new StoreModelServiceException(" No Product Exists with the given productId  ","showProduct");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to showProduct ", "showProduct");
		}
		return product;
	}
	
	//This Method is used to define an inventory object of a particular product and the location of product.
	public Inventory defineInventory(String inventoryId, String location, int capacity, int count,String productId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Inventory inventory = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			Product product= productCollection.get(productId);
			if(inventoryId!= null && location!= null && product!= null && productCollection.containsKey(product.getProduct_id())) {
				inventory = new Inventory(inventoryId, location, capacity, count, product);
				inventoryCollection.put(inventoryId, inventory);
				producyInventoryMapping.put(productId, inventoryId);
			}else {
				throw new StoreModelServiceException("Arguments Missing for defining Inventory ","defineInventory");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to defineInventory ", "defineInventory");
		}
		return inventory;
	}
	
	//This Method is used to return the inventory Object based on the inventoryId
	public Inventory showInventory(String inventoryId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Inventory inventory = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			if(inventoryCollection.containsKey(inventoryId)) {
				inventory =inventoryCollection.get(inventoryId);
			}else {
				throw new StoreModelServiceException(" No Inventory Exists with the given inventoryId  ","showInventory");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to showInventory ", "showInventory");
		}
		
		return inventory;
	}
	
	//This Method is used to update the inventory with the product count of that inventoryId
	public String updateInventory(String inventoryId, int count, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		String response ="";
		if(authenticationService.checkAccess("admin",authToken)) {
			if(inventoryCollection.containsKey(inventoryId)) {
				Inventory inventory =inventoryCollection.get(inventoryId);
				inventory.setCount(count);
				response= "Inventory Updated Successfully!!!";
			}else {
				throw new StoreModelServiceException(" No Inventory Exists with the given inventoryId  ","updateInventory");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to updateInventory ", "updateInventory");
		}
		
		return response;
	}
	
	
	//This Method is used to define a new customer and assign these properties to the new customer
	public Customer defineCustomer(String customerId,String firstName, String lastName, String type, String emailAddress, String accountAddress, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Customer customer= null;
		if(authenticationService.checkAccess("admin", authToken)) {
			customer= new Customer(customerId,firstName, lastName, type, emailAddress, accountAddress);
			customerCollection.put(customerId,customer);
			User user = new User(customerId, firstName + lastName);
			String voicePassword = "--voice:"+firstName +lastName +"--";
			String facePassword = "--face:"+firstName +lastName +"--";
			Credential credentialFace = new Credential("face", facePassword);
			Credential credentialVoice = new Credential("voice", voicePassword);
			Set<Credential> credentials = new HashSet<>();
			credentials.add(credentialFace);
			credentials.add(credentialVoice);
			user.setCredentials(credentials);
			userCollection.put(customerId, user);
			//public ResourceRole(String resource_role_name, Role role, Resource resource) 
			String resource_role_name = "";
			//ResourceRole resourceRole = new ResourceRole();
		}else {
			throw new AuthenticationException("User does not have permissions to defineCustomer ", "defineCustomer");
		}
		
		return customer;
	}
	
	//This Method is used to return the customer object if exists for the given customerId
	public Customer showCustomer(String customerId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Customer customer= null;
		if(authenticationService.checkAccess("admin",authToken)) {
			if(customerId!= null && customerCollection.containsKey(customerId)) {
				customer =customerCollection.get(customerId);
			}else {
				throw new StoreModelServiceException(" No Customer Exists with the given customerId  ","showCustomer");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to showCustomer ", "showCustomer");
		}
		
		return customer;
	}
	
	//This Method is used to update customer with the new location of the customer.
	public String updateCustomer(String customerId, String locationId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		String response ="Customer location unable to update";
		if(authenticationService.checkAccess("admin", authToken)) {
			if(customerCollection.containsKey(customerId)) {
				Customer customer =this.customerCollection.get(customerId);
				customer.setLocation(locationId);
				response= "Customer location Updated Successfully!!!";
			}else {
				throw new StoreModelServiceException(" No Customer Exists with the given customerId  ","updateCustomer");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to updateCustomer ", "updateCustomer");
		}
		return response;
	}
	
	//This method is responsible to define new basket with a given basketId
	public Basket defineBasket(String basketId, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Basket basket = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			basket = new Basket(basketId);
			basketCollection.put(basketId, basket);
		}else {
			throw new AuthenticationException("User does not have permissions to defineBasket ", "defineBasket");
		}
		
		return basket;
	}
	
	//This method is responsible for assigning a given basket to a specific customer.
	public String assignBasket(String basketId, String customerId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		String response = "";
		if(authenticationService.checkAccess("admin",authToken)) {
			if(!customerBasketMapping.containsKey(customerId) && customerCollection.containsKey(customerId)) {
				customerBasketMapping.put(customerId, basketId);
				response = "Basket mapped to customer succcessfully";
			}else {
				throw new StoreModelServiceException(" A Basket is already assigned to the given customer has another basket assigned.","assignBasket");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to assignBasket ", "assignBasket");
		}
		
		return response;
	}
	
	//This method is responsible to get the associated basket to given customer, if none then create a new basket and assign it to the customer.
	public String getCustomerBasket(String customerId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		String basketId = UUID.randomUUID().toString();
		if(authenticationService.checkAccess("admin", authToken)) {
			if(customerId!= null && customerBasketMapping.containsKey(customerId)) {
				basketId = customerBasketMapping.get(customerId);
			}else {
				Basket basket  = new Basket(basketId);
				customerBasketMapping.put(customerId, basketId);
				basketCollection.put(basketId, basket);
			}
		}else {
			throw new AuthenticationException("User does not have permissions to getCustomerBasket ", "getCustomerBasket");
		}
		
		return basketId;
	}
	
	//This method is responsible to Add a new basket.
	public String addBasketItem(String basketId, String productId , int itemCount, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		String response = "";
		Map<Product, Integer>productList = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			if(basketCollection.containsKey(basketId)) {
				Basket basket = basketCollection.get(basketId);
				productList =  basket.getItem();
				if(productList!= null && productList.size()>0) {
					boolean productExist = false;
					for(Map.Entry<Product, Integer> entry: productList.entrySet()) {
						if(entry.getKey().getProduct_id().equalsIgnoreCase(productId)) {
							int initialValue = entry.getValue();
							productExist = true;
							productList.put(entry.getKey(), initialValue+itemCount);
							response = "Item count added successfully from Inventory";
							break;
						}
					}
					if(!productExist) {
						productList.put(productCollection.get(productId), itemCount);
						response = "Item count added successfully from Inventory";
					}
				}else {
					productList= new HashMap<>();
					productList.put(productCollection.get(productId), itemCount);
					response = "Item count added successfully from Inventory";
				}
				basket.setItem(productList);
				
			}else {
				throw new StoreModelServiceException("Error while adding items to the basket.","addBasket");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to addBasketItem ", "addBasketItem");
		}
		
		return response;
	}
	
	//This method is responsible to remove an specific item with a specific count from a specific basket.
	public String removeBasketItem(String basketId, String productId , int itemCount, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		String response = "";
		if(authenticationService.checkAccess("admin", authToken)) {
			if(basketCollection.containsKey(basketId)) {
				Basket basket = basketCollection.get(basketId);
				Map<Product, Integer>productList =  basket.getItem();
				for(Map.Entry<Product, Integer> entry: productList.entrySet()) {
					if(entry.getKey().getProduct_id().equalsIgnoreCase(productId)) {
						int initialValue = entry.getValue();
						if(initialValue>=itemCount) {
							productList.put(entry.getKey(), initialValue-itemCount);
							response = "Itemcount removed successfully from Inventory";
							break;
						}else {
							throw new StoreModelServiceException("Error while removing items to the basket.", "removeBasketItem");
						}
					}
				}
			}else {
				throw new StoreModelServiceException("Error while removing items to the basket.", "removeBasketItem");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to removeBasketItem ", "removeBasketItem");
		}
		return response;
	}
	
	//This method is responsible for removing all items from the basket with a given basketID.
	public String clearBasket(String basketId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		String response = "";
		if(authenticationService.checkAccess("admin", authToken)) {
			if(basketCollection.containsKey(basketId)) {
				Basket basket = basketCollection.get(basketId);
				Map<Product, Integer> itemMap= new HashMap<>();
				basket.setItem(itemMap);
				response = "Basket cleared successfully";
			}else {
				throw new StoreModelServiceException("Invalid BasketId","clearBasket");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to clearBasket ", "clearBasket");
		}
		return response;
	}
	//This method is responsible for returning all items in the basket and show their details.
	public String showBasketItems(String basketId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		StringBuilder response = new StringBuilder("BasketItems with a basketId").append(basketId);
		if(authenticationService.checkAccess("admin", authToken)) {
			if(basketId!= null && basketCollection.containsKey(basketId)) {
				Basket basket = basketCollection.get(basketId);
				Map<Product, Integer>productList =  basket.getItem();
				if(productList !=null && productList.size()>0) {
					for(Map.Entry<Product, Integer> entry: productList.entrySet()) {
						response.append("Product with productId: " + entry.getKey().getProduct_id()+ " with an item count of: " + entry.getValue());
					}
				}else {
					response = new StringBuilder("No Items in the basket");
				}
				
			}else {
				throw new StoreModelServiceException("Invalid BasketId.","showBasketItems");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to showBasketItems ", "showBasketItems");
		}
		
		return response.toString();
	}
	
	//This method is responsible for defining a new device based on its type.
	public Object defineDevice(String type, String name, String location, String deviceId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Object response = null;
		String storeId = location.substring(0,location.indexOf(":"));
		String aisleId = location.substring(location.indexOf(":")+1).trim();
		Store store = storeCollection.get(storeId);
		if(authenticationService.checkAccess("admin",authToken)) {
			if(type.equalsIgnoreCase("turnstile")) {
				response = new Turnstile(deviceId, name, type, storeId);
				List<Turnstile> turnstileList= store.getTurnstiles();
				if(turnstileList ==null) {
					turnstileList = new ArrayList<>();
					turnstileList.add((Turnstile)response);
				}else {
					turnstileList.add((Turnstile)response);
				}
				store.setTurnstiles(turnstileList);
				turnstileMapping.put(deviceId,(Turnstile)response);
				appliancesMapping.put(deviceId,(Appliances)response);
			}else if(type.equalsIgnoreCase("robot")){
					response = new Robot(deviceId, name, type, storeId);
					List<Robot> robotList = store.getRobots();
					if(robotList ==null) {
						robotList = new ArrayList<>();
						robotList.add((Robot)response);
					}else {
						robotList.add((Robot)response);
					}
					store.setRobots(robotList);
					robotMapping.put(deviceId,(Robot)response);
					appliancesMapping.put(deviceId,(Appliances)response);
					
			}else if(type.equalsIgnoreCase("speaker")){
				response = new Speaker(deviceId, name, type, storeId);
				List<Speaker> speakersList = store.getSpeakers();
				if(speakersList ==null) {
					speakersList = new ArrayList<>();
					speakersList.add((Speaker)response);
				}else {
					speakersList.add((Speaker)response);
				}
				store.setSpeakers(speakersList);
				speakerMapping.put(deviceId,(Speaker)response);
				appliancesMapping.put(deviceId,(Appliances)response);
			
		}else if(type.equalsIgnoreCase("microphone") || type.equalsIgnoreCase("camera")) {
				response = new Sensors(deviceId, name, type, aisleId);
				List<Aisle> aisleList = store.getStoreAisles();
				Aisle currentAisle = null;
				for(Aisle aisle : aisleList) {
					if(aisle.getAisle_number().equalsIgnoreCase(aisleId)) {
						currentAisle = aisle;
						List<Sensors> sensorsList = aisle.getSensors();
						if(sensorsList ==null) {
							sensorsList = new ArrayList<>();
							sensorsList.add((Sensors)response);
						}else {
							sensorsList.add((Sensors)response);
						}
						aisle.setSensors(sensorsList);
						break;
					}
				}
				sensorsMapping.put(deviceId,(Sensors)response);
			}else {
				throw new StoreModelServiceException("Invalid DeviceId.","showBasketItems");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to defineDevice ", "defineDevice");
		}
		
		return response;
	}
	//This method is responsible for returning a particular device based on the device
	public Object showDevice(String deviceId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Object response = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			if(deviceId!= null ) {
				if(sensorsMapping.containsKey(deviceId)) {
					response = sensorsMapping.get(deviceId);
				}else if(appliancesMapping.containsKey(deviceId)) {
					response = appliancesMapping.get(deviceId);
				}
			}else {
				throw new StoreModelServiceException(" No Sensor Type or Appliance Type Exists with the given Device Id.","showDevice");
			}
		}else {
			throw new AuthenticationException("User does not have permissions to showDevice ", "showDevice");
		}
		return response;
	}
	
	//This method is responsible to send command to a specific device
	public String sendApplianceCommand(String device_id, String command, Ledger ledger) throws AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		deviceLastCommand.put(device_id, command);
		if(observersList.size()==0) {
			this.addObserver(new StoreController(this, ledger, authenticationService,authToken));
		}
		for(Observer observer: observersList) {
			this.invokeUpdate(command);
		}
		return "Successfully processed command of : " + command;
	}
	
	//This method is responsible to create to a specific event for a specific device.
	public String createEvent(String device_id, String eventDescription, Ledger ledger) throws AccessDeniedException, AuthenticationException, InvalidAccessTokenException {
		deviceLastEvent.put(device_id, eventDescription);
		if(observersList.size()==0) {
			this.addObserver(new StoreController(this, ledger,authenticationService,authToken));
		}
		for(Observer observer: observersList) {
			this.invokeUpdate(eventDescription);
		}
		return "Successfully created event of : "  + eventDescription;
	}

	@Override
	//This method is an implementation of observable interface method to add a new observer
	public void addObserver(Observer device) {
		observersList.add(device);	
	}

	@Override
	//This method is an implementation of observable interface method to remove a existing observer
	public void removeObserver(Observer device) {
		observersList.remove(device);
	}

	@Override
	//This method is an implementation of observable interface method to to notify all the observers.
	public void invokeUpdate(String event) throws AccessDeniedException, AuthenticationException, InvalidAccessTokenException{
		for (Observer device : observersList) {
			device.update(event);
        }
	}
	
	//This method is responsible for fetching the Inventory object for a given product.
	public Inventory getProductInventory(String productId, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Inventory response = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			if(producyInventoryMapping.containsKey(productId)) {
				 response = inventoryCollection.get(producyInventoryMapping.get(productId));
			 }
		}else {
			throw new AuthenticationException("User does not have permissions to getProductInventory ", "getProductInventory");
		}
		 return response;
	}
	
	//This method is responsible for getting the Map of all customers in store
	public Map<String, Customer> getCustomerCollection(AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException{
		Map<String, Customer> response = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			response = this.customerCollection;
		}else {
			throw new AuthenticationException("User does not have permissions to getCustomerCollection ", "getCustomerCollection");
		}
		return response;
	}
	
	//This method is responsible for getting the customer account balance.
	public int getCustomerAccountBalance(Customer customer, Ledger ledger, AuthToken authToken) throws LedgerException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		int balance =0;
		if(authenticationService.checkAccess("admin",authToken)) {
			balance =ledger.getAccountBalance(customer.getAccount_address());
		}else {
			throw new AuthenticationException("User does not have permissions to getCustomerAccountBalance ", "getCustomerAccountBalance");
		}
		return balance;
	}
	
	//This method is responsible for getting the available basket to assign to a customer.
	public String getAvailablebasket(AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Basket availableBasket = null;
		String finalBasketId = "";
		if(authenticationService.checkAccess("admin",authToken)) {
			for(Map.Entry<String, Basket> entry: this.basketCollection.entrySet()) {
				if(!customerBasketMapping.containsValue(entry.getKey())) {
					availableBasket = entry.getValue();
					break;
				}
			}
			if(availableBasket !=null) {
				finalBasketId = availableBasket.getBasket_id();
			}
		}else {
			throw new AuthenticationException("User does not have permissions to getAvailablebasket ", "getAvailablebasket");
		}
		return finalBasketId;
	}
	
	//This method is responsible for returning the current store discussed in the store model service.
	public Store getCurrentStore(AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Store store = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			store =this.currentStore;
		}else {
			throw new AuthenticationException("User does not have permissions to getCurrentStore", "getCurrentStore");
		}
		return store ;
	}
	
	//THis method is to return a Basket object based on the basketID
	public Basket getBasket(String basketId, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		Basket basket = null;
		if(authenticationService.checkAccess("admin",authToken)) {
			basket =  this.basketCollection.get(basketId);
		}else {
			throw new AuthenticationException("User does not have permissions to getBasket ", "getBasket");
		}
		return basket;
	}
	
	// This method is responsible for opening/closing turnstile.
	public void openTurnstiles(String message, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		if(authenticationService.checkAccess("admin",authToken)) {
			List<Turnstile> turnstiles= this.currentStore.getTurnstiles();
			for(Turnstile turnstile :turnstiles) {
				turnstile.setState("open");
				turnstile.setLastCustomerConveration(message);
			}
		}else {
			throw new AuthenticationException("User does not have permissions to openTurnstiles ", "openTurnstiles");
		}
		
	}
	
	// This method is responsible for action to be executed by first robot.
	public void firstRobotAction(String message, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		if(authenticationService.checkAccess("admin",authToken)) {
			List<Robot> robots= this.currentStore.getRobots();
			robots.get(0).setLastTask(message);
		}else {
			throw new AuthenticationException("User does not have permissions to firstRobotAction ", "firstRobotAction");
		}
	}
	
	//This method is responsible for action to be executed by 2nd onwards all robots.
	public void remainingRobotActions(String message, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		if(authenticationService.checkAccess("admin",authToken)) {
			List<Robot> robots= this.currentStore.getRobots();
			for(int i=1; i <robots.size(); i++) {
				robots.get(i).setLastTask(message);
			}
		}else {
			throw new AuthenticationException("User does not have permissions to remainingRobotActions ", "remainingRobotActions");
		}
		
	}
	//This method is responsible for action to be executed by all robots.
	public void allRobotsActions(String message, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		if(authenticationService.checkAccess("admin",authToken)) {
			List<Robot> robots = this.currentStore.getRobots();
			for(Robot robot: robots) {
				robot.setLastTask(message);
			}
		}else {
			throw new AuthenticationException("User does not have permissions to allRobotsActions ", "allRobotsActions");
		}
		
	}
	
	//This method returns the size or weight of the basket.
	public double getBasketSize(String customerId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, NumberFormatException, InvalidAccessTokenException {
		double basketProductsSize = 0;
		if(authenticationService.checkAccess("admin",authToken)) {
			Customer customer = showCustomer(customerId,authToken);
			String basketId = getCustomerBasket(customerId,authToken);
			Basket basket= this.getBasket(basketId,authToken);
			Map<Product, Integer> basketItem = basket.getItem();
			for(Map.Entry<Product, Integer> entry: basketItem.entrySet()) {
				basketProductsSize = (int) (basketProductsSize + (Double.valueOf(entry.getKey().getSize())*entry.getValue()));
			}
		}else {
			throw new AuthenticationException("User does not have permissions to getBasketSize ", "getBasketSize");
		}
		
		return basketProductsSize;
	}
	
	//This method returns the total cost of items in the basket.
	public double getBasketPrice(String customerId, AuthToken authToken) throws StoreModelServiceException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		double basketPrice = 0;
		if(authenticationService.checkAccess("admin",authToken)) {
			Customer customer = showCustomer(customerId, authToken);
			String basketId = getCustomerBasket(customerId, authToken);
			Basket basket= this.getBasket(basketId, authToken);
			Map<Product, Integer> basketItem = basket.getItem();
			for(Map.Entry<Product, Integer> entry: basketItem.entrySet()) {
				basketPrice = (int) (basketPrice + (entry.getKey().getPrice()*entry.getValue()));
			}
		}else {
			throw new AuthenticationException("User does not have permissions to getBasketPrice ", "getBasketPrice");
		}
		
		return basketPrice;
	}
	
	//This method assigns task message to all speakers.
	public void speakerTask(String speakerMessage, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		if(authenticationService.checkAccess("admin",authToken)) {
			List<Speaker> speakerslist= this.currentStore.getSpeakers();
			for(Speaker speaker : speakerslist) {
				speaker.setLastCustomerConveration(speakerMessage);
			}
		}else{
			throw new AuthenticationException("User does not have permissions to speakerTask ", "speakerTask");
		}
		
	}
	
	//This method is responsible for setting camera location for customer.
	public void setCustomerLocationFromCamera(String imagePath, String aisleLocation, AuthToken authToken) throws AuthenticationException, InvalidAccessTokenException {
		try {
			if(authenticationService.checkAccess("admin",authToken)) {
				Map<String, Customer> customerMap= this.getCustomerCollection(authToken);
				for(Map.Entry<String, Customer> entry: customerMap.entrySet()) {
					if(entry.getValue().getCustomer_photo() != null) {
						File file  = new File(imagePath);
						Image customerImage = ImageIO.read(file);
					if(entry.getValue().getCustomer_photo().equals(customerImage)) {
						entry.getValue().setLocation(aisleLocation);
					}
				} 
					}
			}else{
				throw new AuthenticationException("User does not have permissions to setCustomerLocationFromCamera ", "setCustomerLocationFromCamera");
			}
				
			}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	//This method is used to find customer location and return it.
	public String getCustomerLocation(String customerName, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		String customerLocation = "";
		if(authenticationService.checkAccess("admin",authToken)) {
			Map<String, Customer> customerCollection= this.getCustomerCollection(authToken);
			for(Map.Entry<String, Customer> entry : customerCollection.entrySet()) {
				if(entry.getValue().getFirst_name().equalsIgnoreCase(customerName) || entry.getValue().getLast_name().equalsIgnoreCase(customerName)) {
					customerLocation = entry.getValue().getLocation();
					break;
				}
			}
		}else{
			throw new AuthenticationException("User does not have permissions to getCustomerLocation ", "getCustomerLocation");
		}
		
		return customerLocation;
	}
	
public AuthToken login(String userId) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
	AuthToken authToken  = new AuthToken(userId);
	return authToken;
	}
	
public void createUser(String userId, String name,  AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
	if(authenticationService.checkAccess("admin", authToken)) {
		authenticationService.createUser(userId,name );
	}else{
		throw new AuthenticationException("User does not have permissions to createUser ", "createUser");
	}
}

public void assignUserCredentials(String user_id, String credential_type, String credential_details, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
	if(authenticationService.checkAccess("admin",authToken)) {
		authenticationService.assignUserCredentials(user_id,credential_type, credential_details);
	}else{
		throw new AuthenticationException("User does not have permissions to assignUserCredentials ", "assignUserCredentials");
	}
}

public void assignUserRoles(String user_id, String role_id, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
	if(authenticationService.checkAccess("admin",authToken)) {
		authenticationService.assignUserRoles(user_id,role_id);
	}else{
		throw new AuthenticationException("User does not have permissions to assignUserRoles ", "assignUserRoles");
	}
}

public void createResourseRole(String resource_role_name, String role, String resource, AuthToken authToken) throws AccessDeniedException, InvalidAccessTokenException, AuthenticationException {
	if(authenticationService.checkAccess("admin",authToken)) {
		authenticationService.createResourseRole(resource_role_name,role, resource);
	}else{
		throw new AuthenticationException("User does not have permissions to createResourseRole ", "createResourseRole");
	}	
}

public void addResourseRole(String user_id, String resource_role, AuthToken authToken) throws AccessDeniedException, InvalidAccessTokenException, AuthenticationException {
	if(authenticationService.checkAccess("admin",authToken)) {
		authenticationService.addResourseRole(user_id,resource_role);
	}else{
		throw new AuthenticationException("User does not have permissions to addResourseRole ", "addResourseRole");
	}
}

public void addPermissionToRole(String role_id, String permission_id, AuthToken authToken) throws AccessDeniedException, InvalidAccessTokenException, AuthenticationException {
	if(authenticationService.checkAccess("admin",authToken)) {
		authenticationService.addPermissionToRole(role_id,permission_id);
	}else{
		throw new AuthenticationException("User does not have permissions to addResourseRole ", "addResourseRole");
	}
}


public void defineRole(String role_id, String role_name, String role_description, AuthToken authToken) throws AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
	if(authenticationService.checkAccess("admin",authToken)) {
		authenticationService.defineRole(role_id,role_name, role_description);
	}else{
		throw new AuthenticationException("User does not have permissions to addResourseRole ", "addResourseRole");
	}
}

public void definePermission(String permission_id, String permission_name, String permission_description, AuthToken authToken) throws AccessDeniedException, InvalidAccessTokenException, AuthenticationException {
	if(authenticationService.checkAccess("admin",authToken)) {
		authenticationService.defineRole(permission_id,permission_name, permission_description);
	}else{
		throw new AuthenticationException("User does not have permissions to addResourseRole ", "addResourseRole");
	}
	
}
public void defineResource(String resource_id, String description, AuthToken authToken) throws AccessDeniedException, InvalidAccessTokenException, AuthenticationException {
	if(authenticationService.checkAccess("admin",authToken)) {
		authenticationService.defineResource(resource_id, description);
	}else{
		throw new AuthenticationException("User does not have permissions to addResourseRole ", "addResourseRole");
	}
}
public void checkInventory() throws AccessDeniedException, InvalidAccessTokenException, AuthenticationException {
		authenticationService.getInventory();
	
}


}
