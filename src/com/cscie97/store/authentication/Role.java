package com.cscie97.store.authentication;

import java.nio.file.AccessDeniedException;
import java.util.List;

//This class is for managing different Roles.
public class Role extends Entitlement implements Visitable{
	private String id;
	private String name;
	private String description;
	List<Permission> permissions;
	
	public Role(String id, String name) {
		this.id =id;
		this.name = name;
	}
	
	public Role(String id, String name, String description) {
		this.id =id;
		this.name = name;
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
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	
	@Override
	public String getRoleDetails() {
		return toString();
	}
		
	@Override
	public String accept(Visitor visitor) throws AccessDeniedException {
		return visitor.visit(this);
		
	}
	
	public String toString() {
		String response= "Role returned with roleId of "  + this.getId()+ " with name  "+ this.getName() + "with Details of " + this.getDescription();
		return response;
	}
}
