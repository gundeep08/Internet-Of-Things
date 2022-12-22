package cscie97.store.model;

import java.util.List;

public class Store {
	
	private String store_id;
	private String store_name;
	private String address;
	private List<Aisle> storeAisles;
	private int current_customer_count;
	private double daily_revenue;
	private List<Appliances> appliances;
	private List<Speaker> speakers;
	private List<Robot> robots;
	private List<Turnstile> turnstiles;
	
	public List<Speaker> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(List<Speaker> speakers) {
		this.speakers = speakers;
	}

	public List<Robot> getRobots() {
		return robots;
	}

	public void setRobots(List<Robot> robots) {
		this.robots = robots;
	}

	public List<Turnstile> getTurnstiles() {
		return turnstiles;
	}

	public void setTurnstiles(List<Turnstile> turnstiles) {
		this.turnstiles = turnstiles;
	}
	
	
	public List<Appliances> getAppliances() {
		return appliances;
	}

	public void setAppliances(List<Appliances> appliances) {
		this.appliances = appliances;
	}

	public Store(String store_id, String store_name, String address) {
		this.store_id=store_id;
		this.store_name =store_name;
		this.address =address;	
	}
	
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCurrent_customer_count() {
		return current_customer_count;
	}
	public void setCurrent_customer_count(int current_customer_count) {
		this.current_customer_count = current_customer_count;
	}
	public double getDaily_revenue() {
		return daily_revenue;
	}
	public void setDaily_revenue(double daily_revenue) {
		this.daily_revenue = daily_revenue;
	}
	public List<Aisle> getStoreAisles() {
		return storeAisles;
	}

	public void setStoreAisles(List<Aisle> storeAisles) {
		this.storeAisles = storeAisles;
	}
	
	public String toString() {
		   String response= "Store returned with storeId of "  + this.getStore_id()+  " name of: " + this.getStore_name() + " and at an address of  "+ this.getAddress();
		   return response;
	}
}
