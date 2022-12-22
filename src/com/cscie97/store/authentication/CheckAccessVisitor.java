package com.cscie97.store.authentication;

import java.nio.file.AccessDeniedException;
import java.util.*;
import com.cscie97.store.authentication.InvalidAccessTokenException;


//This class is checking all authentication for a user trying to access a specific function.
public class CheckAccessVisitor implements Visitor{
	private AuthToken authToken;
	private String userId;
	
	public CheckAccessVisitor(String userId, AuthToken authToken) {
		this.authToken =authToken;
		this.userId =userId;
	}

	@Override
	public String visit(User user) throws AccessDeniedException {
		Set<Role> roles = user.getRoles();
		if(roles !=null) {
			for(Role role :roles) {
				this.visit(role);
			}
		}
		return "User validated successfully";
	}

	@Override
	public String visit(Role role) throws AccessDeniedException {
		List<Permission> permissions = role.getPermissions();
		if(permissions !=null) {
			for(Permission permission :permissions) {
				this.visit(permission);
			}
		}
		return "Role verified successfully";
	}

	@Override
	public String visit(Permission permission) throws AccessDeniedException {
		if(permission.getId().equalsIgnoreCase("user_admin")) {
			permission.accept(this);
		}else {
			throw new AccessDeniedException("User do not have permissions to access");
		}
		
		return "permission verified successfully";
	}

	@Override
	public String visit(AuthenticationService authenticationService) throws InvalidAccessTokenException, AccessDeniedException {
		if(authToken.getState() == "expired") {
			throw new InvalidAccessTokenException("TokenExpired", "visit");
		}else {
			Map<String, User> userMap =authenticationService.getallUsers();
			this.visit(userMap.get(userId));
		}
		return "Auth Token Succcessfully validated";
	}

	@Override
	public String visit(Entitlement entitlement) throws AccessDeniedException {
		if(entitlement instanceof Role) {
			((Role) entitlement).accept(this);
		}
		return "Entitlement Verified successfully";
	}

	@Override
	public String visit(AuthToken authToken) throws InvalidAccessTokenException {
		return authToken.accept(this);
	}

}
