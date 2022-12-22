package cscie97.store.model;

public class Shelf {
	
	private String name;
	private int capacity;
	private int count;
	private Product product;
	private String temperature;
	private String level;
	private String shelf_id;
	private String aisle_id;
	private String store_id;
	enum Level{
		high,
		medium,
		low
	};
	private String description;
	private enum Temperature{
		frozen,
		refrigerated,
		ambient,
		warm,
		hot
	};
	public Shelf(String store_id, String aisle_id, String shelf_id, String name, String level, String description, String temperature) {
		this.store_id = store_id;
		this.aisle_id = aisle_id;
		this.shelf_id = shelf_id;
		this.name = name;
		this.description = description;
		this.temperature =Temperature.valueOf(temperature).toString();
		this.level =Level.valueOf(level).toString();
	}
	public String getShelf_id() {
		return shelf_id;
	}
	public void setShelf_id(String shelf_id) {
		this.shelf_id = shelf_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAisle_id() {
		return aisle_id;
	}
	public void setAisle_id(String aisle_id) {
		this.aisle_id = aisle_id;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String toString() {
		   String response= "Shelf returned with shelfId of "  + this.getShelf_id()+ " at the Aisle with aisleId of  "+ this.getAisle_id() + " at the store with storeId of " + this.getStore_id();
		   return response;
	}
	
}
