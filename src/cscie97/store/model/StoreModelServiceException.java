package cscie97.store.model;

public class StoreModelServiceException extends Exception {
	
	private String reason;
	private String methodName;
	
	public StoreModelServiceException(String reason, String methodName) {
		super("StoreModelService Exception because "+ reason + " while Executing method of:  " + methodName ); 
		this.reason = reason;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
}
