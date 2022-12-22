package com.cscie97.store.authentication;

import java.nio.file.AccessDeniedException;

public class Permission extends Entitlement implements Visitable {
	
	private String id;
	private String name;
	private String description;
	
	public Permission(String id, String name, String description) {
		this.id =id;
		this.name =name;
		this.description =description;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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

	@Override
	public String getRoleDetails() {
		return this.toString();
	}
	
	@Override
	public String accept(Visitor visitor) throws AccessDeniedException {
		return visitor.visit(this);
		
	}
	
	public String toString() {
		String response= "Permission returned with permissionId of "  + this.getId()+ " with name  "+ this.getName() + "with description of " + this.getDescription();
		return response;
	}

	

}
