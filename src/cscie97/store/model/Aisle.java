package cscie97.store.model;

import java.util.List;

public class Aisle {
	
	private String aisle_number;
	private String name;
	private String description;
	private String store_location;
	private String storeId;
	private List<Shelf> shelfs;
	private List<Sensors> sensors;
	

	public Aisle(String storeId, String aisle_number, String name, String description, String store_location) {
		this.storeId=storeId;
		this.aisle_number =aisle_number;
		this.name = name;
		this.description =description;
		this.store_location=store_location;
	}
	
	public String getAisle_number() {
		return aisle_number;
	}
	public void setAisle_number(String aisle_number) {
		this.aisle_number = aisle_number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public List<Shelf> getShelfs() {
		return shelfs;
	}
	public void setShelfs(List<Shelf> shelfs) {
		this.shelfs = shelfs;
	}
	public String getStore_location() {
		return store_location;
	}
	public void setStore_location(String store_location) {
		this.store_location = store_location;
	}
	
	public List<Sensors> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensors> sensors) {
		this.sensors = sensors;
	}

	public String toString() {
		   String response= "Aisle returned with AisleId of "  + this.getAisle_number()+ " for the store with storeId of "+ this.getStoreId();
		   return response;
	}
}
