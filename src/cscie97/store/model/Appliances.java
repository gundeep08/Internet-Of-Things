package cscie97.store.model;

public class Appliances {
	
	private String appliance_id;
	private String name;
	private String store_id;
	private String type;
	
	public Appliances(String appliance_id, String name, String type, String storeId) {
		this.appliance_id =appliance_id;
		this.name =name;
		this.type = type;
		this.store_id = storeId;
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
	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String toString() {
		   String response= "Appliance returned with applianceId of "  + this.getAppliance_id()+ " at the Store with storeId of  "+ this.getStore_id();
		   return response;
	}
}
