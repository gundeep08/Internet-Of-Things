package com.cscie97.store.authentication;

import java.nio.file.AccessDeniedException;

public interface Visitor {

	public String visit(AuthenticationService authenticationService) throws InvalidAccessTokenException, AccessDeniedException;
	public String visit(Entitlement entitlement) throws AccessDeniedException;
    public String visit(User user) throws AccessDeniedException;
    public String visit(Role role) throws AccessDeniedException;
    public String visit(Permission  permission) throws AccessDeniedException;
    public String visit(AuthToken  authToken) throws InvalidAccessTokenException;
}
