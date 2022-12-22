package com.cscie97.store.authentication;

public class Credential implements Visitable  {
	
	private String type;
	private String credential_value;
	
	public Credential(String type, String credential_value) {
		this.type =type;
		this.credential_value =credential_value;
	}
	public String getCredential_value() {
		return credential_value;
	}
	public void setCredential_value(String credential_value) {
		this.credential_value = credential_value;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String accept(Visitor visitor) throws InvalidAccessTokenException {
		return this.toString();
	}
	
	
	

}
