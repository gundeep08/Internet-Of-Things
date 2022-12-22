package com.cscie97.store.authentication;

import java.nio.file.AccessDeniedException;

public interface Visitable {

	   public String accept(Visitor visitor) throws InvalidAccessTokenException, AccessDeniedException;
}
