package cscie97.store.model;

import java.util.Map;

public class Basket {

	private String basket_id;
	private Map<Product, Integer>item;
	
	public Basket(String basket_id) {
		this.basket_id =basket_id;
	}
	
	public String getBasket_id() {
		return basket_id;
	}
	public void setBasket_id(String basket_id) {
		this.basket_id = basket_id;
	}
	public Map<Product, Integer> getItem() {
		return item;
	}
	public void setItem(Map<Product, Integer> item) {
		this.item = item;
	}
	
	public String toString() {
		   String response= "Basket Created with basketId of: "  + this.getBasket_id();
		   return response;
	}

}
