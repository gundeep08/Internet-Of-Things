package com.cscie97.store.authentication;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

//This class is responsible to implement all the Authentication related function i.e. users, roles, permission etc.
public class AuthenticationService implements Visitable{
	
	private Map<String, User> storeUsers = new HashMap<>();
	private Map<String, Role> roles = new HashMap<>();
	private Map<String, Resource> resources = new HashMap<>();
	private Map<String, ResourceRole> resourceRoles = new HashMap<>();
	private Map<String, Permission> permissions = new HashMap<>();
	
	public AuthenticationService() {
		//Bootstrapping the admin user.
		User adminUser = new User("admin", "admin");
		storeUsers.put("admin", adminUser);
	}

	public void createUser(String userId, String name) {
		User user = new User(userId,name );
		storeUsers.put(user.getId(), user);
	}
	
	public void assignUserCredentials(String user_id, String credential_type, String credential_details) {
		User user = storeUsers.get(user_id);
		Credential credential = new Credential(credential_type, credential_details);
		Set<Credential> credentials = user.getCredentials();
		if(credentials == null) {
			credentials = new HashSet<>();
		}
		credentials.add(credential);
		user.setCredentials(credentials);
	}
	
	public void assignUserRoles(String user_id, String role_id) {
		User user = storeUsers.get(user_id);
		Role role = roles.get(role_id);
		Set<Role> roles = user.getRoles();
		if(roles ==null) {
			roles = new HashSet<>();
		}
		roles.add(role);
		user.setRoles(roles);
	}

	public void createResourseRole(String resource_role_name, String role, String resource) {
		Resource existingResource = resources.get(resource);
		if(existingResource ==null) {
			existingResource = new Resource(resource);
			resources.put(resource, existingResource);
		}
		Role existingRole = roles.get(role);
		ResourceRole resourceRole = new ResourceRole(resource_role_name, existingRole, existingResource);
		resourceRoles.put(resource_role_name, resourceRole);	
	}

	public void addResourseRole(String user_id, String resource_role) {
		User user = storeUsers.get(user_id);
		Set<ResourceRole> existingResourceRoles= user.getResourceRoles();
		ResourceRole resourceRole= resourceRoles.get(resource_role);
		if(existingResourceRoles == null) {
			existingResourceRoles = new HashSet<>();
		}
		existingResourceRoles.add(resourceRole);
		user.setResourceRoles(existingResourceRoles);
	}

	public void addPermissionToRole(String role_id, String permission_id) {
		Role role = roles.get(role_id);
		List<Permission> permissionList = role.getPermissions();
		Permission permission = permissions.get(permission_id);
		if(permissionList ==null) {
			permissionList = new ArrayList<>();
		}
		permissionList.add(permission);
		role.setPermissions(permissionList);
	}
	

public void defineRole(String role_id, String role_name, String role_description) {
	Role role = new Role(role_id,role_name, role_description);
	roles.put(role_id, role);
}

public void definePermission(String permission_id, String permission_name, String permission_description) {
	Permission permission = new Permission(permission_id, permission_name, permission_description);
	permissions.put(permission_id, permission);
}


public void defineResource(String resource_id, String description) {
	Resource resource = new Resource(resource_id, description);
	resources.put(resource_id, resource);
}

public AuthToken getAuthToken(String credentials) {
	String authId = UUID.randomUUID().toString();
	AuthToken authToken = new AuthToken(authId);
	return authToken;
}

@Override
public String accept(Visitor visitor) throws InvalidAccessTokenException, AccessDeniedException {
	String response = visitor.visit(this);
	return response;
}

public boolean checkAccess(String userId, AuthToken authToken) throws AccessDeniedException, InvalidAccessTokenException{
	CheckAccessVisitor checkAccessVisitor = new CheckAccessVisitor(userId, authToken);
	checkAccessVisitor.visit(this);
	return true;
}

public void getInventory() throws AccessDeniedException {
	/*
	 * Use the Visitor Pattern to a. support traversing the objects of the
	 * Authentication Service to provide an inventory of all Users, Resources,
	 * Accesses, Roles, and Permissions. b. Checking for access
	 */
	  InventoryVisitor inventoryVisitor = new InventoryVisitor();
	  for(Map.Entry<String, User> user : storeUsers.entrySet()) {
		  user.getValue().accept(inventoryVisitor); 
	  }
	  
	  for(Map.Entry<String, Role> role : roles.entrySet()) {
		  role.getValue().accept(inventoryVisitor); 
	  }
	  
	  for(Map.Entry<String, Permission> permission : permissions.entrySet()) {
		  permission.getValue().accept(inventoryVisitor); 
	  }
	 
	
}

public Map<String, User> getallUsers() {
	return storeUsers;
}

public  Map<String, Role> getallRoles() {
	return roles;
}

public Map<String, Permission> getallPermissions() {
	return permissions;
}

public Map<String, ResourceRole> getallResourceRoles() {
	return resourceRoles;
}



}
