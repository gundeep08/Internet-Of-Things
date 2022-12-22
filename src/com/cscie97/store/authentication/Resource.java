package com.cscie97.store.authentication;

//This class manages resources like store etc.
public class Resource implements Visitable{
	
	private String description;
	private String id;
	
	public Resource(String id) {
		this.id =id;
	}
	
	public Resource(String id, String description) {
		this.id =id;
		this.description =description;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String accept(Visitor visitor) {
		return this.toString();
	}
	
}
