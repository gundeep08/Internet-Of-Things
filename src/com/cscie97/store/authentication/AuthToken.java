package com.cscie97.store.authentication;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

//This class manages the Authtoken for each user.
public class AuthToken implements Visitable {
	
	private String id;
	private Timestamp expirationTime;
	private String state;
	
	public AuthToken(String id) {
		this.id =id;
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date());               // sets calendar time/date
		cal.add(Calendar.HOUR_OF_DAY, 2);      // adds one hour
		Date date = cal.getTime();
		this.expirationTime = new Timestamp(date.getTime()); 
		this.state = "active";
		
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Timestamp getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(Timestamp expirationTime) {
		this.expirationTime = expirationTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String accept(Visitor visitor) throws InvalidAccessTokenException {
		return this.toString();
	}
	
	
}
