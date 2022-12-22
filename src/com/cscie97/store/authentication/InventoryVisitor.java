package com.cscie97.store.authentication;

public class InventoryVisitor implements Visitor{

	@Override
	public String visit(User user) {
		return user.toString();
	}

	@Override
	public String visit(Role role) {
		return role.toString();
	}

	@Override
	public String visit(Permission permission) {
		return permission.toString();
	}

	@Override
	public String visit(AuthenticationService authenticationService) {
		return authenticationService.toString();
	}

	@Override
	public String visit(Entitlement entitlement) {
		return entitlement.toString();
	}

	@Override
	public String visit(AuthToken authToken) {
		return authToken.toString();
	}

}
