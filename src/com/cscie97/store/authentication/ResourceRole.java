package com.cscie97.store.authentication;

//This class stores the mapping of resources to different roles, there can be multiple role access to resources.
public class ResourceRole extends Role {
	
	private String resource_role_name;
	private Role role;
	private Resource resource;
	
	public ResourceRole(String resource_role_name, Role role, Resource resource) {
		super(role.getId(), role.getName());
		this.resource_role_name =resource_role_name; 
		this.role =role;
		this.resource =resource;
	}
	
	public String getResource_role_name() {
		return resource_role_name;
	}
	public void setResource_role_name(String resource_role_name) {
		this.resource_role_name = resource_role_name;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public String getRoleDetails() {
		// TODO Auto-generated method stub
		return null;
	}


}
