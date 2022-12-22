package cscie97.store.model;


public class Inventory {
	
	private String inventory_id;
	private Product product;
	private int count;
	private int capacity;
	private String storeId;
	private String aisleId;
	private String shelfId;
	public Inventory(String inventory_id, String location, int capacity, int count, Product product) {
		this.inventory_id =inventory_id;
		this.count=count;
		this.capacity =capacity;
		this.product = product;
		this.storeId=location.substring(0, location.indexOf(":")).trim();
		this.aisleId=location.substring(location.indexOf(":")+1, location.lastIndexOf(":")).trim();
		this.shelfId=location.substring(location.lastIndexOf(":")+1).trim();
	}
	
	public String getInventory_id() {
		return inventory_id;
	}
	public void setInventory_id(String inventory_id) {
		this.inventory_id = inventory_id;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getAisleId() {
		return aisleId;
	}

	public void setAisleId(String aisleId) {
		this.aisleId = aisleId;
	}

	public String getShelfId() {
		return shelfId;
	}

	public void setShelfId(String shelfId) {
		this.shelfId = shelfId;
	}
	
	public String toString() {
		   String response= "Inventory returned with inventoryId of "  + this.getInventory_id()+ " at the shelfId of  "+ this.getShelfId();
		   return response;
	}
}
