package cscie97.store.model;

import java.util.Map;

public class Sensors {
	
	private String sensor_id;
	private String name;
	private  String aisle_id;
	private String type;
	
	public Sensors(String sensor_id, String name, String type, String aisleId) {
		this.sensor_id =sensor_id;
		this.name =name;
		this.type = type;
		this.aisle_id = aisleId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAisle_id() {
		return aisle_id;
	}

	public void setAisle_id(String aisle_id) {
		this.aisle_id = aisle_id;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSensor_id() {
		return sensor_id;
	}
	public void setSensor_id(String sensor_id) {
		this.sensor_id = sensor_id;
	}
	
	public String toString() {
		   String response= "Sensors returned with sensorId of "  + this.getSensor_id()+ " at the Aisle of aisleId of  "+ this.getAisle_id();
		   return response;
	}

}
