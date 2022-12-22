package com.cscie97.store.interfaces;

import java.nio.file.AccessDeniedException;

import com.cscie97.ledger.LedgerException;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;

import cscie97.store.model.StoreModelServiceException;

public interface Command {

	public void execute() throws StoreModelServiceException, LedgerException, AccessDeniedException, AuthenticationException, InvalidAccessTokenException;
}
