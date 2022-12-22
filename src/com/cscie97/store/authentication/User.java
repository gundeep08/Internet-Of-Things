package com.cscie97.store.authentication;

import java.nio.file.AccessDeniedException;
import java.util.Set;

//This class manages different properties of each indiviual User.
public class User extends Entitlement implements Visitable {
	
	private String id;
	private String name;
	private Set<Role> roles;
	private Set<Credential> credentials;
	private Set<ResourceRole> resourceRoles;
	private AuthToken authToken;
	
	
	public User(String id, String name) {
		this.id= id;
		this.name = name;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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
	public Set<Credential> getCredentials() {
		return credentials;
	}
	public void setCredentials(Set<Credential> credentials) {
		this.credentials = credentials;
	}
	public Set<ResourceRole> getResourceRoles() {
		return resourceRoles;
	}
	public void setResourceRoles(Set<ResourceRole> resourceRoles) {
		this.resourceRoles = resourceRoles;
	}
	public AuthToken getAuthToken() {
		return authToken;
	}
	public void setAuthToken(AuthToken authToken) {
		this.authToken = authToken;
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
	String credentialString ="";
	if(this.getCredentials()!=null) {
		credentialString = this.getCredentials().toArray().toString();
	}
	String response= "User returned with userId of "  + this.getId()+ " with name  "+ this.getName() + "with Credentials of " + credentialString;
	return response;
}	
}
