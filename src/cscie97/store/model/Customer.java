package cscie97.store.model;

import java.awt.Image;
import java.io.File;
import java.sql.Timestamp;

public class Customer {
	
	private String customer_id;
	private String first_name;
	private String last_name;
	private String email_address;
	private String account_address;
	private Timestamp lastSeen;
	private String guestType;
	private enum Type{
		registered,
		guest
	};
	private String location;
	private Image Customer_photo;
	
	public Customer(String customer_id, String first_name, String last_name, String type, String email_address, String account_address) {
		this.customer_id =customer_id;
		this.first_name =first_name;
		this.last_name =last_name;
		this.email_address =email_address;
		this.account_address = account_address;
		this.guestType =Type.valueOf(type).toString();
	}
	
	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getAccount_address() {
		return account_address;
	}

	public void setAccount_address(String account_address) {
		this.account_address = account_address;
	}

	public Timestamp getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Timestamp lastSeen) {
		this.lastSeen = lastSeen;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Image getCustomer_photo() {
		return Customer_photo;
	}

	public void setCustomer_photo(Image customer_photo) {
		Customer_photo = customer_photo;
	}
	
	public String getGuestType() {
		return guestType;
	}

	public void setGuestType(String guestType) {
		this.guestType = guestType;
	}
	public String toString() {
		String response= "Customer returned with customerId of "  + this.getCustomer_id()+ " with name  "+ this.getFirst_name() + " "+ this.getLast_name();
		return response;
	}

}
