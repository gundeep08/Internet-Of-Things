package com.cscie97.store.interfaces;

import java.nio.file.AccessDeniedException;

import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;

public interface Observable {

	public void addObserver(Observer device);
	public void removeObserver(Observer device);
	void invokeUpdate(String event) throws AccessDeniedException, AuthenticationException, InvalidAccessTokenException;
}
