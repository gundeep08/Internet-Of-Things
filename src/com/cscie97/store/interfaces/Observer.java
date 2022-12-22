package com.cscie97.store.interfaces;

import java.nio.file.AccessDeniedException;

import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;

public interface Observer {

	public void update(String event) throws AccessDeniedException, AuthenticationException, InvalidAccessTokenException;
}
