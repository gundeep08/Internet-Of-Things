package cscie97.store.model;

public class Robot extends Appliances{

	private String lastCustomerConveration;
	private String lastTask;
	private Customer currentCustomer;
	private String appliance_id;
	private String name;
	private String type;
	private String storeId;

	
	public String getLastTask() {
		return lastTask;
	}
	public void setLastTask(String lastTask) {
		this.lastTask = lastTask;
	}

	public Customer getCurrentCustomer() {
		return currentCustomer;
	}

	public void setCurrentCustomer(Customer currentCustomer) {
		this.currentCustomer = currentCustomer;
	}
	
	public String getLastCustomerConveration() {
		return lastCustomerConveration;
	}

	public void setLastCustomerConveration(String lastCustomerConveration) {
		this.lastCustomerConveration = lastCustomerConveration;
	}
	
	public String getAppliance_id() {
		return appliance_id;
	}

	public void setAppliance_id(String appliance_id) {
		this.appliance_id = appliance_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public Robot(String appliance_id, String name, String type, String storeId) {
		super(appliance_id, name, type, storeId);
		this.appliance_id = appliance_id;
		this.name = name;
		this.type =type;
		this.storeId =storeId;
	}
	
	public void talkCustomer(String lastCustomerConveration) {
		this.lastCustomerConveration =lastCustomerConveration;
	}
	
	public void performTask(String task) {
		this.lastTask=task;
	}
	
	public void listenCustomer(Customer customer) {
		this.currentCustomer =customer;
	}

}
