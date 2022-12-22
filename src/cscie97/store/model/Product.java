package cscie97.store.model;

public class Product {
	
	private String description;
	private String category;
	private String name;
	private String size;
	private double price;
	private String temperature;
	private String product_id;
	private enum Temperature{
		frozen,
		refrigerated,
		ambient,
		warm,
		hot
	};
	
	public Product(String product_id, String name, String description, String size, String category, double price, String temperature) {
		this.product_id =product_id;
		this.name = name;
		this.description =description;
		this.size =size;
		this.category =category;
		this.price =price;
		this.temperature =Temperature.valueOf(temperature).toString();
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String toString() {
		 String response= "Product returned with productId of "  + this.getProduct_id()+ " under the category of  "+ this.getCategory();
		 return response;
	}
}
